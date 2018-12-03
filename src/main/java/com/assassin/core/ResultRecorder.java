package com.assassin.core;

import com.assassin.core.entity.sql.ExecuteSQLEntity;
import com.assassin.core.entity.template.StepFreeMarkerResultEntity;
import com.assassin.core.entity.TestCaseResultEntity;
import com.assassin.core.entity.TestStepResultEntity;
import com.assassin.core.entity.verify.ResponseVerifyEntity;
import com.assassin.core.utility.CommonTools;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/28.
 */
public class ResultRecorder {
    private static Logger logger = LogManager.getLogger(ResultRecorder.class.getName());
    private Configuration cfg;
    private Map<String, Object> caseFMResultMap;
    private String reportFolderPath;

    /**
     * @description 初始化报告存放路径/初始化FreeMarker模板
     * @param reportFolderPath  报告路径
     */
    public ResultRecorder(String reportFolderPath) {
        this.reportFolderPath = reportFolderPath;
        this.cfg = new Configuration(Configuration.VERSION_2_3_26);
        // 这里使用类加载器的方式获取读取模板文件的路径.只有这样可以在JAR包中运行
        this.cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "templates");
        this.cfg.setDefaultEncoding("UTF-8");
        this.cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        this.cfg.setLogTemplateExceptions(false);
    }

    /**
     * @description             将框架生成的结果转换为FreeMarker方便处理的数据实体
     * @param tcResultEntity    测试结果实体
     */
    public void readTestCaseResultEntity(TestCaseResultEntity tcResultEntity) {
        this.caseFMResultMap = new HashMap<>();

        String businessName = tcResultEntity.getBusinessName();
        this.caseFMResultMap.put("businessName", businessName);

        List<TestStepResultEntity> tsResultEntityList = tcResultEntity.getTsResultEntityList();
        List<StepFreeMarkerResultEntity> stepFreeMarkerResultEntityList = new ArrayList<>();
        // 迭代测试步骤结果实体
        for (TestStepResultEntity tsResultEntity: tsResultEntityList) {
            StepFreeMarkerResultEntity stepFreeMarkerResultEntity = new StepFreeMarkerResultEntity();

            String stepName = tsResultEntity.getStepName();
            String stepUrl = tsResultEntity.getStepUrl();
            int httpStatusCode = tsResultEntity.getHttpStatusCode();
            long httpResponseTime = tsResultEntity.getHttpResponseTime();
            int stepVerifySize = tsResultEntity.getResponseVerifyList().size();      // 获取验证点的数量,方便在模板中展示

            List<String> verifyFieldList = new ArrayList<>();
            List<String> actualValueList = new ArrayList<>();
            List<String> expectValueList = new ArrayList<>();
            List<String> resultValueList = new ArrayList<>();
            // 迭代测试步骤的检查点实体List
            for (ResponseVerifyEntity vEntity: tsResultEntity.getResponseVerifyList()) {
                verifyFieldList.add(vEntity.getVerifyField());
                actualValueList.add(vEntity.getActualValue());
                expectValueList.add(vEntity.getExpectValue());
                resultValueList.add(vEntity.getResultValue());
            }
            // 单步执行截图
            String imageName = tsResultEntity.getImageName();
            // SQL查询结果
            List<ExecuteSQLEntity> sqlFetchResult = tsResultEntity.getFetchResultList();
            // 接口/请求的基本信息
            stepFreeMarkerResultEntity.setStepName(stepName);
            stepFreeMarkerResultEntity.setStepUrl(stepUrl);
            stepFreeMarkerResultEntity.setHttpStatusCode(httpStatusCode);
            stepFreeMarkerResultEntity.setHttpResponseTime(httpResponseTime);
            stepFreeMarkerResultEntity.setVerifyLength(stepVerifySize);
            // 检查点
            stepFreeMarkerResultEntity.setVerifyFieldList(verifyFieldList);
            stepFreeMarkerResultEntity.setActualValueList(actualValueList);
            stepFreeMarkerResultEntity.setExpectValueList(expectValueList);
            stepFreeMarkerResultEntity.setResultValueList(resultValueList);
            // SQL查询结果
            stepFreeMarkerResultEntity.setSqlFetchEntityList(sqlFetchResult);
            // 将单步结果放置到列表中
            stepFreeMarkerResultEntityList.add(stepFreeMarkerResultEntity);
        }
        // 指定测试结果列表,供模板引擎使用.
        this.caseFMResultMap.put("stepFMResultList", stepFreeMarkerResultEntityList);
    }

    /**
     * @description 将测试结果写入到HTML报告中
     */
    public void writeToHtmlReport() {
        FileWriter out = null;
        Template template = null;
        File file = null;

        try {
            template = this.cfg.getTemplate("TestCaseResultTemplate.ftl", "UTF-8");
            String reportFullName = this.reportFolderPath + File.separator + this.caseFMResultMap.get("businessName") + "_" + CommonTools.getNowTime() + ".html";
            file = new File(reportFullName);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    out = new FileWriter(file);
                    // 写入报告
                    template.process(this.caseFMResultMap, out);
                }
            }
            logger.info("报告写出: {}", reportFullName);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
