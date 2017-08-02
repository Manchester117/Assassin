package com.highpin.core;

import com.highpin.core.entity.TestCaseDataEntity;
import com.highpin.core.entity.TestStepDataEntity;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/23.
 */
public class ConvertJsonToStep {
    private File caseFolder = null;

    /**
     * @description 构造方法,实例化读取用例的文件夹
     */
    public ConvertJsonToStep(String folderPath) {
        this.caseFolder = new File(folderPath);
    }

    /**
     * @description 从制定文件夹中获取所有用例
     * @return 返回用例JSON的列表
     */
    public List<String> getTestCaseJson() {
        File [] caseList = this.caseFolder.listFiles();
        BufferedReader br = null;
        String line = null;
        StringBuilder sBuffer = new StringBuilder();
        List<String> testCaseJsonList = new ArrayList<String>();

        if (caseList != null && caseList.length > 0) {
            for (File jsonTestCaseFile: caseList) {
                try {
                    br = new BufferedReader(new FileReader(jsonTestCaseFile));
                    while ((line = br.readLine()) != null) {
                        sBuffer.append(line.trim());
                    }
                    testCaseJsonList.add(sBuffer.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testCaseJsonList;
    }

    /**
     * @description 获取某个用例的JSON字符串,并将字符串转换为用例实体
     * @param testCaseJson Json用例字符串
     * @return 用例实体
     */
    public TestCaseDataEntity parseJsonCaseByJsonPath(String testCaseJson) {
        // 定义用例实体对象
        TestCaseDataEntity tcDataEntity = new TestCaseDataEntity();
        // 定义JsonPath对象
        ReadContext ctx = JsonPath.parse(testCaseJson);

        String businessName = ctx.read("$.BusinessName");
        tcDataEntity.setBusinessName(businessName);
        // System.out.println(businessName);

        List<String> stepNameList = ctx.read("$.TestStep[*].StepName");
        // 获取用例步骤长度
        tcDataEntity.setStepLength(stepNameList.size());

        List<TestStepDataEntity> tsDataEntityList = new ArrayList<>();

        for (int i = 0; i < tcDataEntity.getStepLength(); ++i) {
            // 实例化单个步骤的实体
            TestStepDataEntity tsDataEntity = new TestStepDataEntity();

            String stepName = ctx.read("$.TestStep[" + i + "].StepName");
            tsDataEntity.setStepName(stepName);

            String protocol = ctx.read("$.TestStep[" + i + "].RequestData.Protocol");
            tsDataEntity.setProtocol(protocol);

            String url = ctx.read("$.TestStep[ " + i + "].RequestData.URL");
            tsDataEntity.setUrl(url);

            String method = ctx.read("$.TestStep[ " + i + "].RequestData.Method");
            tsDataEntity.setMethod(method);

            Map<String, String> cookiesMap = ctx.read("$.TestStep[" + i + "].RequestData.Cookies");
            tsDataEntity.setCookiesMap(cookiesMap);

            Map<String, String> headersMap = ctx.read("$.TestStep[" + i + "].RequestData.Headers");
            tsDataEntity.setHeadersMap(headersMap);

            List<Map<String, String>> getParamsList = ctx.read("$.TestStep[" + i + "].RequestData.GetParams");
            tsDataEntity.setGetParamsList(getParamsList);

            List<Map<String, String>> postParamsList = ctx.read("$.TestStep[" + i + "].RequestData.PostParams");
            tsDataEntity.setPostParamsList(postParamsList);

            String jsonParams = ctx.read("$.TestStep[" + i + "].RequestData.JsonParams");
            tsDataEntity.setJsonParams(jsonParams);

            Map<String, String> correlationParamsMap = ctx.read("$.TestStep[" + i + "].CorrelationParams");
            tsDataEntity.setCorrelationParamsMap(correlationParamsMap);

            List<Map<String, String>> verifyList = ctx.read("$.TestStep[" + i + "].VerifyList");
            tsDataEntity.setVerifyList(verifyList);

//            System.out.println(stepName);
//            System.out.println(protocol);
//            System.out.println(url);
//            System.out.println(method);
//            System.out.println(cookiesMap);
//            System.out.println(headersMap);
//            System.out.println(getParamsList);
//            System.out.println(postParamsList);
//            System.out.println(jsonParams);
//            System.out.println(correlationParamsMap);
//            System.out.println(verifyList);

            tsDataEntityList.add(tsDataEntity);
        }
        tcDataEntity.setTestStepDataEntityList(tsDataEntityList);

        return tcDataEntity;
    }

    public static void main(String[] args) {
        ConvertJsonToStep convertJsonToStep = new ConvertJsonToStep("case");
        List<String> caseJsonList = convertJsonToStep.getTestCaseJson();

        List<TestCaseDataEntity> tcDataEntityList = new ArrayList<>();
        for (String caseJson: caseJsonList) {
            TestCaseDataEntity testCaseDataEntity = convertJsonToStep.parseJsonCaseByJsonPath(caseJson);
            tcDataEntityList.add(testCaseDataEntity);
        }
    }
}
