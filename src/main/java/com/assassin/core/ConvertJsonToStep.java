package com.assassin.core;

import com.assassin.core.entity.TestCaseDataEntity;
import com.assassin.core.entity.TestStepDataEntity;
import com.assassin.core.entity.correlate.CorrelateEntity;
import com.assassin.core.entity.sql.ExecuteSQLEntity;
import com.assassin.core.entity.verify.DataVerifyEntity;
import com.assassin.core.utility.ConfigureTools;
import com.assassin.core.utility.ParamGeneratorTools;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by Peng.Zhao on 2017/7/23.
 */
public class ConvertJsonToStep {
    private static Logger logger = LogManager.getLogger(ConvertJsonToStep.class.getName());
    private File caseFolder = null;

    /**
     * @description 构造方法,实例化读取用例的文件夹
     */
    public ConvertJsonToStep(String folderPath) {
        this.caseFolder = new File(folderPath);
    }

    /**
     * @description         从Case文件夹中读取用例json,并与配置文件中的用例顺序进行对比,对用例进行筛选
     * @param caseArray     用例文件数组
     * @return runCaseList  要运行的用例文件对象
     */
    private List<File> getConfigCaseSequence(File [] caseArray) {
        boolean sequenceFlag = false;

        Map<String, Object> caseSequenceMap = ConfigureTools.getCaseSequence();

        List<File> runCaseList = new ArrayList<>();                     // 返回的实际用列表
        List sequenceList = (List) caseSequenceMap.get("sequence");     // 从配置文件读出来的用例列表
        List<File> folderCaseList = Arrays.asList(caseArray);           // Case文件夹中的用例列表

        if (caseSequenceMap.get("active") instanceof Boolean) {
            sequenceFlag = (Boolean) caseSequenceMap.get("active");
        } else {
            // 如果读取配置文件中用例序列的active类型不是boolean,则返回空列表,不运行任何测试用例
            return new ArrayList<>();
        }

        if (sequenceList == null) {
            // 如果active为true,但是列表为空,则不运行任何测试用例.
            return new ArrayList<>();
        }

        // 用例文件列表转成文件名-文件的Map
        Map<String, File> caseFileMap = new HashMap<>();
        for (File caseFile: folderCaseList) {
            caseFileMap.put(caseFile.getName(), caseFile);
        }

        if (sequenceFlag) {
            // 如果配置文件中读取序列用例为真则进行对比
            for (Object caseFileNameObj : sequenceList) {
                String caseFileName = caseFileNameObj.toString();
                if (caseFileMap.get(caseFileName) != null) {
                    runCaseList.add(caseFileMap.get(caseFileName));
                }
            }
        } else {
            // 如果位置文件中读取序列用例为假则直接运行文件夹中用例
            runCaseList = folderCaseList;
        }
        return runCaseList;
    }

    /**
     * @description 从指定文件夹中获取所有用例
     * @return 返回用例JSON的列表
     */
    public List<String> getTestCaseJson() {
        File [] caseArray = this.caseFolder.listFiles();
        List<File> runCaseList = null;
        BufferedReader br = null;
        String line = null;
        StringBuilder sBuffer = null;
        List<String> testCaseJsonList = new ArrayList<>();

        // 判断是否启用配置文件中用例列表
        runCaseList = this.getConfigCaseSequence(caseArray);

        if (runCaseList != null && !runCaseList.isEmpty()) {
            for (File jsonTestCaseFile: runCaseList) {
                logger.info("读取用例: {}", jsonTestCaseFile.getName());
                try {
                    sBuffer = new StringBuilder();
                    br = new BufferedReader(new FileReader(jsonTestCaseFile));
                    while ((line = br.readLine()) != null) {
                        sBuffer.append(line.trim());
                    }
                    testCaseJsonList.add(sBuffer.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            logger.info("读取测试用例完成");
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

        String businessName = ctx.read("$.businessName");
        tcDataEntity.setBusinessName(businessName);

        List<String> stepNameList = ctx.read("$.testStep[*].stepName");
        // 获取用例步骤长度
        tcDataEntity.setStepLength(stepNameList.size());

        List<TestStepDataEntity> tsDataEntityList = new ArrayList<>();

        for (int i = 0; i < tcDataEntity.getStepLength(); ++i) {
            // 实例化单个步骤的实体
            TestStepDataEntity tsDataEntity = new TestStepDataEntity();

            String stepName = ctx.read("$.testStep[" + i + "].stepName");
            tsDataEntity.setStepName(stepName);

            String protocol = ctx.read("$.testStep[" + i + "].requestData.protocol");
            tsDataEntity.setProtocol(protocol);

            String url = ctx.read("$.testStep[" + i + "].requestData.url");
            if (url != null) {
                url = ParamGeneratorTools.replaceSequenceParameter(url);
            }
            tsDataEntity.setStepUrl(url);

            String method = ctx.read("$.testStep[" + i + "].requestData.method");
            tsDataEntity.setMethod(method);

            Map<String, String> cookiesMap = ctx.read("$.testStep[" + i + "].requestData.cookies");
            tsDataEntity.setCookiesMap(cookiesMap);

            Map<String, String> headersMap = ctx.read("$.testStep[" + i + "].requestData.headers");
            tsDataEntity.setHeadersMap(headersMap);

            List<Map<String, String>> getParamsList = ctx.read("$.testStep[" + i + "].requestData.getParams");
            if (getParamsList != null) {
                getParamsList = ParamGeneratorTools.replaceBodyParameter(getParamsList);
            }
            tsDataEntity.setGetParamsList(getParamsList);

            List<Map<String, String>> postParamsList = ctx.read("$.testStep[" + i + "].requestData.postParams");
            if (postParamsList != null) {
                postParamsList = ParamGeneratorTools.replaceBodyParameter(postParamsList);
            }
            tsDataEntity.setPostParamsList(postParamsList);

            String jsonParams = ctx.read("$.testStep[" + i + "].requestData.jsonParams");
            if (jsonParams != null) {
                jsonParams = ParamGeneratorTools.replaceSequenceParameter(jsonParams);
            }
            tsDataEntity.setJsonParams(jsonParams);

            List<CorrelateEntity> correlateEntityList = this.parseCorrelationSection(ctx, i);
            tsDataEntity.setCorrelateEntityList(correlateEntityList);

            List<DataVerifyEntity> dataVerifyEntityList = this.parseDataVerifySection(ctx, i);
            tsDataEntity.setDataVerifyEntityList(dataVerifyEntityList);

            List<ExecuteSQLEntity> executeSQLEntityList = this.parseSQLSection(ctx, i);
            tsDataEntity.setFetchSQLList(executeSQLEntityList);

            tsDataEntityList.add(tsDataEntity);
        }
        tcDataEntity.setTestStepDataEntityList(tsDataEntityList);

        return tcDataEntity;
    }

    /**
     * @description                 将关联的表达式逻辑存放在实体中
     * @param ctx                   jsonPath的对象
     * @param i                     读取的用例步骤索引
     */
    private List<CorrelateEntity> parseCorrelationSection(ReadContext ctx, int i) {
        List<Map<String, String>> correlationList = ctx.read("$.testStep[" + i + "].correlationParams");
        List<CorrelateEntity> correlateEntityList = new ArrayList<>();

        if (correlationList != null) {
            for (int corrIndex = 0; corrIndex < correlationList.size(); ++corrIndex) {
                CorrelateEntity correlateEntity = new CorrelateEntity();

                String corrField = ctx.read("$.testStep[" + i + "].correlationParams[" + corrIndex + "].corrField");
                String corrPattern = ctx.read("$.testStep[" + i + "].correlationParams[" + corrIndex + "].corrPattern");
                String corrExpression = ctx.read("$.testStep[" + i + "].correlationParams[" + corrIndex + "].corrExpression");
                String corrValueIndex = ctx.read("$.testStep[" + i + "].correlationParams[" + corrIndex + "].corrIndex");
                String isUseForFetchDB = ctx.read("$.testStep[" + i + "].correlationParams[" + corrIndex + "].isUseForFetchDB");

                correlateEntity.setCorrField(corrField);
                correlateEntity.setCorrPattern(corrPattern);
                correlateEntity.setCorrExpression(corrExpression);
                correlateEntity.setCorrIndex(corrValueIndex);
                correlateEntity.setCorrValue("");                       // 由于模板中关联参数没有这个字段,所以先给空字符串.
                correlateEntity.setIsUseForFetchDB(isUseForFetchDB);

                correlateEntityList.add(correlateEntity);
            }
        } else {
            correlateEntityList = null;
        }
        return correlateEntityList;
    }

    /**
     * @description                 将验证点存放在实体中
     * @param ctx                   jsonPath的对象
     * @param i                     读取的用例步骤索引
     */
    private List<DataVerifyEntity> parseDataVerifySection(ReadContext ctx, int i) {
        List<Map<String, String>> verifyList = ctx.read("$.testStep[" + i + "].verifyList");
        List<DataVerifyEntity> dataVerifyEntityList = new ArrayList<>();

        if (verifyList != null) {
            for (int verifyIndex = 0; verifyIndex < verifyList.size(); ++verifyIndex) {
                DataVerifyEntity dataVerifyEntity = new DataVerifyEntity();

                String verifyField = ctx.read("$.testStep[" + i + "].verifyList[" + verifyIndex + "].verifyField");
                String pattern = ctx.read("$.testStep[" + i + "].verifyList[" + verifyIndex + "].pattern");
                String expression = ctx.read("$.testStep[" + i + "].verifyList[" + verifyIndex + "].expression");
                String index = ctx.read("$.testStep[" + i + "].verifyList[" + verifyIndex + "].index");
                String expectValue = ctx.read("$.testStep[" + i + "].verifyList[" + verifyIndex + "].expectValue");

                dataVerifyEntity.setVerifyField(verifyField);
                dataVerifyEntity.setPattern(pattern);
                dataVerifyEntity.setExpression(expression);
                dataVerifyEntity.setIndex(index);
                dataVerifyEntity.setExpectValue(expectValue);

                dataVerifyEntityList.add(dataVerifyEntity);
            }
        } else {
            dataVerifyEntityList = null;
        }
        return dataVerifyEntityList;
    }

    /**
     * @description             解析用例中的JSON部分
     * @param ctx               jsonPath的对象
     * @param i                 读取的用例步骤索引
     * @return
     */
    private List<ExecuteSQLEntity> parseSQLSection(ReadContext ctx, int i) {
        List<Map<String, List<String>>> sqlPerformList = ctx.read("$.testStep[" + i + "].fetchDB");
        List<ExecuteSQLEntity> executeSQLEntityList = new ArrayList<>();

        if (sqlPerformList != null) {
            for (int sqlIndex = 0; sqlIndex < sqlPerformList.size(); ++sqlIndex) {
                ExecuteSQLEntity sqlEntity = new ExecuteSQLEntity();

                String sql = ctx.read("$.testStep[" + i + "].fetchDB[" + sqlIndex + "].sql");
                List<String> columnList = ctx.read("$.testStep[" + i + "].fetchDB[" + sqlIndex + "].columnList");

                sqlEntity.setSql(sql);
                sqlEntity.setColumnList(columnList);

                executeSQLEntityList.add(sqlEntity);
            }
        } else {
            executeSQLEntityList = null;
        }
        return executeSQLEntityList;
    }

//    public static void main(String[] args) {
//        ConvertJsonToStep convertJsonToStep = new ConvertJsonToStep("case");
//        List<String> caseJsonList = convertJsonToStep.getTestCaseJson();
//
//        List<TestCaseDataEntity> tcDataEntityList = new ArrayList<>();
//        for (String caseJson: caseJsonList) {
//            TestCaseDataEntity testCaseDataEntity = convertJsonToStep.parseJsonCaseByJsonPath(caseJson);
//            tcDataEntityList.add(testCaseDataEntity);
//        }
//    }
}
