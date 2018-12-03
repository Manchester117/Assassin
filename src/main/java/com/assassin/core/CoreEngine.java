package com.assassin.core;

import com.assassin.core.entity.*;
import com.assassin.core.utility.CommonTools;
import com.assassin.core.utility.DeployTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng.Zhao on 2017/7/24.
 */
public class CoreEngine {
    private static Logger logger = LogManager.getLogger(CoreEngine.class.getName());
    // 用例实体列表
    private List<TestCaseDataEntity> tcDataEntityList = null;
    private String jarFolderPath = null;

    /**
     * @description 构造方法--初始化测试用例实体列表和测试结果实体列表--用于IDE运行
     */
    public CoreEngine() {
        this.tcDataEntityList = new ArrayList<>();
    }

    /**
     * @description 构造方法--初始化测试用例实体列表和测试结果实体列表以及创建文件夹路径--用于JAR包运行
     */
    public CoreEngine(String jarFolderPath) {
        this.tcDataEntityList = new ArrayList<>();
        this.jarFolderPath = jarFolderPath;
    }

    /**
     * @description 创建用例实体列表
     */
    public void createTestCaseEntityList() {
        ConvertJsonToStep convertJsonToStep = null;
        if (this.jarFolderPath == null) {
            // IDE运行
            convertJsonToStep = new ConvertJsonToStep("case");
        } else {
            // Jar包运行
            convertJsonToStep = new ConvertJsonToStep(this.jarFolderPath + "case");
        }
        List<String> jsonCaseList = convertJsonToStep.getTestCaseJson();

        // 将所有测试用例进行封装
        for (String jsonCase: jsonCaseList) {
            TestCaseDataEntity tcDataEntity = convertJsonToStep.parseJsonCaseByJsonPath(jsonCase);
            this.tcDataEntityList.add(tcDataEntity);
        }
    }

    /**
     * @description 遍历测试用例,内置请求发送
     */
    public void traversalTestCase() {
        for (TestCaseDataEntity tcDataEntity: this.tcDataEntityList) {
            DBFetchComponent dbFetchComponent = DBFetchComponent.getInstance();
            dbFetchComponent.createDBConnection();
            logger.info("-------------------------------------------------------");
            logger.info("执行测试用例: {}", tcDataEntity.getBusinessName());
            // 实例化测试用例响应实体
            TestCaseResponseEntity tcResponseEntity = new TestCaseResponseEntity(tcDataEntity.getBusinessName());
            // 实例化测试用例结果实体
            TestCaseResultEntity tcResultEntity = new TestCaseResultEntity(tcDataEntity.getBusinessName());
            // 将测试用例载入步骤迭代器
            StepIterator stepIterator = new StepIterator(tcDataEntity, tcResponseEntity, tcResultEntity);
            // 初始化上下文参数替换
            ContextReplace contextReplace = new ContextReplace(tcDataEntity);
            // 创建存放报告文件夹
            String reportFolderPath = CommonTools.createReportFolder(tcDataEntity.getBusinessName());
            // 执行请求测试
            this.performTest(stepIterator, contextReplace, dbFetchComponent, reportFolderPath);
            // 进行报告写入
            this.writeTestResult(tcResultEntity, reportFolderPath);
            logger.info("-------------------------------------------------------");
            dbFetchComponent.closeDBConnection();
        }
    }

    /**
     * @description 使用StepIterator迭代用例步骤,发送请求,并处理响应
     * @param stepIterator 用例迭代器
     */
    private void performTest(StepIterator stepIterator, ContextReplace contextReplace, DBFetchComponent dbFetchComponent, String reportFolderPath) {
        TestCaseDataEntity tcDataEntity = stepIterator.getTcDataEntity();
        TestCaseResponseEntity tcResponseEntity = stepIterator.getTcResponseEntity();
        TestCaseResultEntity tcResultEntity = stepIterator.getTcResultEntity();

        TestStepDataEntity tsDataEntity = null;
        TestStepResponseEntity tsResponseEntity = null;
        TestStepResultEntity tsResultEntity = null;

        RequestComponent requestComponent = null;
        ResponseParser responseParser = new ResponseParser(tcDataEntity, tcResponseEntity);
        ResultVerify resultVerify = new ResultVerify(tcResultEntity);

        for (int i = 0; i < tcDataEntity.getStepLength(); ++i) {
            // 迭代测试步骤
            tsDataEntity = stepIterator.getSingleStep(i);
            // 执行测试请求
            requestComponent = new RequestComponent();
            tsResponseEntity = requestComponent.requestWrapper(tsDataEntity);
            // 获取响应中的值
            tsResultEntity = responseParser.responseWrapper(tsDataEntity, tsResponseEntity);
            // 使用正则表达式查找关联值
            contextReplace.catchCorrelationParams(tsDataEntity, tsResponseEntity);
            // 将单步响应实体追加到测试用例响应实体当中
            responseParser.addStepRespInTCRespEntity(tsResponseEntity);
            // 对获取到的检查点进行验证
            resultVerify.stepValueVerify(tsResultEntity);
            // 将验证结果加入用例验证实体
            resultVerify.addStepResultInTCResultEntity(tsResultEntity);
            // 执行SQL条件参数替换
            contextReplace.replaceSQLParameters();
            // 执行DB查询
            dbFetchComponent.executeSQLFetch(tsDataEntity, tsResultEntity);
            // 执行上下文参数替换
            contextReplace.replaceRequestParameters();
        }
    }

    /**
     * @description 将测试结果输出值报告
     */
    public void writeTestResult(TestCaseResultEntity tcResultEntity, String reportFolderPath) {
        ResultRecorder resultRecorder = null;
        if (this.jarFolderPath == null) {
            // IDE运行
            resultRecorder = new ResultRecorder(reportFolderPath);
        } else {
            // JAR包运行
            resultRecorder = new ResultRecorder(this.jarFolderPath + reportFolderPath);
        }
        resultRecorder.readTestCaseResultEntity(tcResultEntity);
        resultRecorder.writeToHtmlReport();
    }

    public static void main(String[] args) {
        DeployTools deployTools = new DeployTools();
        String jarFolderPath = deployTools.createFolder();
        // IDE运行
        CoreEngine coreEngine = new CoreEngine();
        // JAR包运行
        // CoreEngine coreEngine = new CoreEngine(jarFolderPath);

        coreEngine.createTestCaseEntityList();
        coreEngine.traversalTestCase();
    }
}
