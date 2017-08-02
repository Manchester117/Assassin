package com.highpin.core;

import com.highpin.core.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng.Zhao on 2017/7/24.
 */
public class CoreEngine {
    // 用例实体列表
    private List<TestCaseDataEntity> tcDataEntityList = null;
    private List<TestCaseResultEntity> tcResultEntityList = null;

    public CoreEngine() {
        this.tcDataEntityList = new ArrayList<>();
        this.tcResultEntityList = new ArrayList<>();
    }

    /**
     * @description 创建用例实体列表
     */
    public void createTestCaseEntityList() {
        ConvertJsonToStep convertJsonToStep = new ConvertJsonToStep("case");
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
            // 实例化测试用例响应实体
            TestCaseResponseEntity tcResponseEntity = new TestCaseResponseEntity(tcDataEntity.getBusinessName());
            // 实例化测试用例结果实体
            TestCaseResultEntity tcResultEntity = new TestCaseResultEntity(tcDataEntity.getBusinessName());
            // 将测试用例载入步骤迭代器
            StepIterator stepIterator = new StepIterator(tcDataEntity, tcResponseEntity, tcResultEntity);
            // 初始化上下文参数替换
            ContextReplace contextReplace = new ContextReplace(tcDataEntity);
            // 执行请求发送
            this.performTest(stepIterator, contextReplace);
            // 将每个测试流程验证结果存入列表
            this.tcResultEntityList.add(tcResultEntity);
        }
    }

    /**
     * @description 使用StepIterator迭代用例步骤,发送请求,并处理响应
     * @param stepIterator 用例迭代器
     */
    private void performTest(StepIterator stepIterator, ContextReplace contextReplace) {
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
            responseParser.catchCorrelationParamsByRegex(tsDataEntity, tsResponseEntity);
            // 将单步响应实体追加到测试用例响应实体当中
            responseParser.addStepRespInTCRespEntity(tsResponseEntity);
            // 对获取到的检查点进行验证
            resultVerify.stepValueVerify(tsResultEntity);
            // 将验证结果加入用例验证实体
            resultVerify.addStepResultInTCResultEntity(tsResultEntity);
            // 执行上下文参数替换
            contextReplace.replaceParameters();
        }
    }

    /**
     * @description 将测试结果输出值报告
     */
    public void writeTestResult() {
        ResultRecorder resultRecorder = new ResultRecorder();
        resultRecorder.initReportTemplate();
        resultRecorder.readTestCaseResultEntityList(this.tcResultEntityList);
        resultRecorder.writeToHtmlReport();
    }

    public static void main(String[] args) {
        CoreEngine coreEngine = new CoreEngine();
        coreEngine.createTestCaseEntityList();
        coreEngine.traversalTestCase();
        coreEngine.writeTestResult();
    }
}
