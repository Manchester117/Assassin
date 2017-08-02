package com.highpin.core;

import com.highpin.core.entity.TemplateEngineEntity.StepFreeMarkerResultEntity;
import com.highpin.core.entity.TestCaseResultEntity;
import com.highpin.core.entity.TestStepResultEntity;
import com.highpin.core.utitlity.CommonTools;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/28.
 */
public class ResultRecorder {
    private Configuration cfg;
    private List<Map<String, Object>> caseFMResultMapList;

    /**
     * @description 初始化FreeMarker模板
     */
    public void initReportTemplate() {
        this.cfg = new Configuration(Configuration.VERSION_2_3_26);
        try {
            this.cfg.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
            this.cfg.setDefaultEncoding("UTF-8");
            this.cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
            this.cfg.setLogTemplateExceptions(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 将框架生成的结果转换为FreeMarker方便处理的数据实体
     * @param tcResultEntityList
     */
    public void readTestCaseResultEntityList(List<TestCaseResultEntity> tcResultEntityList) {
        this.caseFMResultMapList = new ArrayList<>();

        // 迭代测试用例结果实体
        for (TestCaseResultEntity tcResultEntity: tcResultEntityList) {
            Map<String, Object> caseFMResultMap = new HashMap<>();

            String businessName = tcResultEntity.getBusinessName();
            caseFMResultMap.put("businessName", businessName);

            List<TestStepResultEntity> tsResultEntityList = tcResultEntity.getTsResultEntityList();
            List<StepFreeMarkerResultEntity> stepFreeMarkerResultEntityList = new ArrayList<>();
            // 迭代测试步骤结果实体
            for (TestStepResultEntity tsResultEntity: tsResultEntityList) {
                StepFreeMarkerResultEntity stepFreeMarkerResultEntity = new StepFreeMarkerResultEntity();

                String stepName = tsResultEntity.getStepName();
                String stepUrl = tsResultEntity.getUrl();
                int httpStatusCode = tsResultEntity.getHttpStatusCode();
                String httpResponseTime = tsResultEntity.getHttpResponseTime();
                int stepVerifySize = tsResultEntity.getStepResultMap().size();

                List<String> resultKeyList = new ArrayList<>();
                List<String> actualValueList = new ArrayList<>();
                List<String> expectValueList = new ArrayList<>();
                List<String> resultValueList = new ArrayList<>();
                // 迭代测试步骤的检查点Map
                for (Map.Entry<String, String> entry: tsResultEntity.getStepResultMap().entrySet()) {
                    String resultKey = entry.getKey();
                    resultKeyList.add(resultKey);
                    actualValueList.add(tsResultEntity.getStepActualMap().get(resultKey));
                    expectValueList.add(tsResultEntity.getStepExpectMap().get(resultKey));
                    resultValueList.add(tsResultEntity.getStepResultMap().get(resultKey));
                }

                stepFreeMarkerResultEntity.setStepName(stepName);
                stepFreeMarkerResultEntity.setStepUrl(stepUrl);
                stepFreeMarkerResultEntity.setHttpStatusCode(httpStatusCode);
                stepFreeMarkerResultEntity.setHttpResponseTime(httpResponseTime);
                stepFreeMarkerResultEntity.setVerifyLength(stepVerifySize);

                stepFreeMarkerResultEntity.setResultKeyList(resultKeyList);
                stepFreeMarkerResultEntity.setActualValueList(actualValueList);
                stepFreeMarkerResultEntity.setExpectValueList(expectValueList);
                stepFreeMarkerResultEntity.setResultValueList(resultValueList);

                stepFreeMarkerResultEntityList.add(stepFreeMarkerResultEntity);
            }
            caseFMResultMap.put("stepFMResultList", stepFreeMarkerResultEntityList);

            this.caseFMResultMapList.add(caseFMResultMap);
        }
    }

    /**
     * @description 将测试结果写入到HTML报告中
     */
    public void writeToHtmlReport() {
        FileWriter out = null;
        for (Map<String, Object> caseFMResultMap: this.caseFMResultMapList) {
            Template template = null;
            File file = null;
            try {
                template = this.cfg.getTemplate("TestCaseResultTemplate.ftl");
                file = new File("report/" + caseFMResultMap.get("businessName") + "_" + CommonTools.getNowTime() + ".html");
                if (!file.exists()) {
                    if (file.createNewFile()) {
                        out = new FileWriter(file);
                        template.process(caseFMResultMap, out);
                    }
                }
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
}
