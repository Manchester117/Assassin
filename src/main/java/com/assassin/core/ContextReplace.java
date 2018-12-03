package com.assassin.core;

import com.google.common.base.Strings;
import com.google.common.base.Objects;
import com.assassin.core.entity.TestCaseDataEntity;
import com.assassin.core.entity.TestStepDataEntity;
import com.assassin.core.entity.TestStepResponseEntity;
import com.assassin.core.entity.correlate.CorrelateEntity;
import com.assassin.core.entity.sql.ExecuteSQLEntity;
import com.assassin.core.utility.ResponseTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/25.
 */
public class ContextReplace {
    private static Logger logger = LogManager.getLogger(ContextReplace.class.getName());
    private TestCaseDataEntity tcDataEntity;

    public ContextReplace(TestCaseDataEntity tcDataEntity) {
        this.tcDataEntity = tcDataEntity;
    }

    /**
     * @description             关联参数的入口方法
     * @param tsDataEntity      单步测试步骤的对象
     * @param responseEntity    请求响应的对象
     */
    public void catchCorrelationParams(TestStepDataEntity tsDataEntity, TestStepResponseEntity responseEntity) {
        // 用例中的关联参数名称和关联取值正则表达式
        List<CorrelateEntity> correlateEntityList = tsDataEntity.getCorrelateEntityList();
        // 请求返回正文
        String httpResponseContent = responseEntity.getHttpResponseContent();

        // 如果URL响应不为空
        if (!Strings.isNullOrEmpty(httpResponseContent)) {
            // 遍历关联参数列表
            for (CorrelateEntity correlateEntity : correlateEntityList) {
                if (isCorrelateExist(correlateEntity)) {
                    String patternMode = correlateEntity.getCorrPattern();
                    // 选择相应的关联参数方式
                    if (Objects.equal("regex", patternMode)) {
                        this.catchCorrelationParamsByRegex(correlateEntity, httpResponseContent);
                    } else if (Objects.equal("jsonPath", patternMode)) {
                        this.catchCorrelationParamsByJsonPath(correlateEntity, httpResponseContent);
                    } else if (Objects.equal("selector", patternMode)) {
                        this.catchCorrelationParamsBySelector(correlateEntity, httpResponseContent);
                    } else if (Objects.equal("xpath", patternMode)) {
                        this.catchCorrelationParamsByXPath(correlateEntity, httpResponseContent);
                    } else {
                        logger.error("关联上下文参数匹配类型不正确");
                    }
                }
            }
        } else {
            logger.warn("请求响应内容为空!URL--{}", tsDataEntity.getStepUrl());
        }
    }

    /**
     * @description                 判断关联对象中是否缺少元素
     * @param correlateEntity       关联对象
     * @return                      如果关联对象的必填项没有确实则返回true,否则返回false.
     */
    private boolean isCorrelateExist(CorrelateEntity correlateEntity) {
        String corrField = correlateEntity.getCorrField();
        String patternMode = correlateEntity.getCorrPattern();
        String expression = correlateEntity.getCorrExpression();

        if (Strings.isNullOrEmpty(corrField)) {
            return false;
        }
        if (Strings.isNullOrEmpty(patternMode)) {
            return false;
        }
        if (Strings.isNullOrEmpty(expression)) {
            return false;
        }
        return true;
    }

    /**
     * @description 通过测试步骤中表明正则表达式从请求响应中获取上下文要关联的值
     * @param correlateEntity   关联实体
     * @param responseContent   响应正文
     */
    public void catchCorrelationParamsByRegex(CorrelateEntity correlateEntity, String responseContent) {
        String corrExpression = correlateEntity.getCorrExpression();
        String corrIndex = correlateEntity.getCorrIndex();
        String corrValue = null;

        if (!Strings.isNullOrEmpty(corrIndex) && corrIndex.matches("\\d*")) {
            int index = Integer.parseInt(corrIndex);
            corrValue = ResponseTools.catchValueByRegex(corrExpression, index, responseContent);
        } else {
            corrValue = ResponseTools.catchValueByRegex(corrExpression, 1, responseContent);
        }
        // 把获取到的关联参数值放置到对象中
        correlateEntity.setCorrValue(corrValue);
        // 打印关联值
        // System.out.println(corrValue);
        // 把关联到的参数全部存入用例实体中的关联参数Map中
        this.tcDataEntity.getCorrelateEntityList().add(correlateEntity);
    }

    public void catchCorrelationParamsByJsonPath(CorrelateEntity correlateEntity, String responseContent) {
        String corrExpression = correlateEntity.getCorrExpression();
        String corrValue = ResponseTools.catchValueByJsonPath(corrExpression, responseContent);

        correlateEntity.setCorrValue(corrValue);
        this.tcDataEntity.getCorrelateEntityList().add(correlateEntity);
    }

    public void catchCorrelationParamsBySelector(CorrelateEntity correlateEntity, String responseContent) {
        String corrExpression = correlateEntity.getCorrExpression();
        String corrValue = ResponseTools.catchValueBySelector(corrExpression, responseContent);

        correlateEntity.setCorrValue(corrValue);
        this.tcDataEntity.getCorrelateEntityList().add(correlateEntity);
    }

    public void catchCorrelationParamsByXPath(CorrelateEntity correlateEntity, String responseContent) {
        String corrExpression = correlateEntity.getCorrExpression();
        String corrValue = ResponseTools.catchValueByXPath(corrExpression, responseContent);

        correlateEntity.setCorrValue(corrValue);
        this.tcDataEntity.getCorrelateEntityList().add(correlateEntity);
    }

    /**
     * @description 使用从页面返回的值作为SQL的查询条件
     */
    public void replaceSQLParameters() {
        List<CorrelateEntity> correlateEntityList = this.tcDataEntity.getCorrelateEntityList();
        List<TestStepDataEntity> testStepDataEntityList = this.tcDataEntity.getTestStepDataEntityList();

        for (CorrelateEntity correlateEntity: correlateEntityList) {
            String isUseConditionValueFlag = correlateEntity.getIsUseForFetchDB();
            if (Objects.equal("true", isUseConditionValueFlag)) {
                this.replaceSQLConditionValue(correlateEntity, testStepDataEntityList);
            }
        }
    }

    /**
     * @description                     遍历测试步骤中的SQL列表,对条件进行替换
     * @param correlateEntity           单个关联实体
     * @param testStepDataEntityList    测试步骤列表
     */
    private void replaceSQLConditionValue(CorrelateEntity correlateEntity, List<TestStepDataEntity> testStepDataEntityList) {
        String replaceField = correlateEntity.getCorrField();
        String replaceValue = correlateEntity.getCorrValue();

        String findParam = "{" + replaceField + "}";
        for (TestStepDataEntity testStepDataEntity: testStepDataEntityList) {
            for (ExecuteSQLEntity executeSQLEntity: testStepDataEntity.getFetchSQLList()) {
                String sql = executeSQLEntity.getSql();
                if (sql.contains(findParam)) {
                    try {
                        sql = sql.replace(findParam, replaceValue);
                    } catch (NullPointerException e) {
                        logger.error("没有从响应中获取关联值,SQL条件替换失败.");
                        e.printStackTrace();
                    }
                    executeSQLEntity.setSql(sql);
                }
            }
        }
    }

    /**
     * @description 执行上下文参数替换,替换Get/Post/Json/URL中标注的参数
     */
    public void replaceRequestParameters() {
        String url = null;
        List<Map<String, String>> getParametersList = null;
        List<Map<String, String>> postParametersList = null;
        String jsonParameter = null;

        List<CorrelateEntity> correlateEntityList = this.tcDataEntity.getCorrelateEntityList();
        List<TestStepDataEntity> tsDataEntityList = this.tcDataEntity.getTestStepDataEntityList();
        for (TestStepDataEntity tsDataEntity: tsDataEntityList) {
            url = tsDataEntity.getStepUrl();
            getParametersList = tsDataEntity.getGetParamsList();
            postParametersList = tsDataEntity.getPostParamsList();
            jsonParameter = tsDataEntity.getJsonParams();

            if (url != null) {
                this.replaceUrlParameter(correlateEntityList, tsDataEntity);
            }
            if (getParametersList != null) {
                this.replaceKeyValueParameters(correlateEntityList, getParametersList);
            }
            if (postParametersList != null) {
                this.replaceKeyValueParameters(correlateEntityList, postParametersList);
            }
            if (jsonParameter != null) {
                this.replaceJsonParameter(correlateEntityList, tsDataEntity);
            }
        }
        // 在替换完毕后,清空测试用例中的关联参数Map,避免重复替换.
        this.tcDataEntity.setCorrelateEntityList(new ArrayList<>());
    }

    /**
     * @description 对Get/Post请求中的Key-Value关联参数进行替换
     * @param correlateEntityList   已经找到的关联参数
     * @param parametersList        待替换的Key-Value参数
     */
    private void replaceKeyValueParameters(List<CorrelateEntity> correlateEntityList, List<Map<String, String>> parametersList) {
        // 遍历已经取到的关联值
        for (CorrelateEntity correlateEntity: correlateEntityList) {
            // 遍历单个测试步骤中Get/Post参数列表
            for (Map<String, String> parameters : parametersList) {
                // 因为参数列表中存放的是单元素Key-Value,所以下面只会循环一次
                for (Map.Entry<String, String> replaceEntry : parameters.entrySet()) {
                    // 已经取到关联参数
                    String corrField = correlateEntity.getCorrField();
                    String corrValue = correlateEntity.getCorrValue();
                    // 待替换的关联参数
                    String replaceField = replaceEntry.getKey();
                    String replaceValue = replaceEntry.getValue();
                    // 如果在待替换的关联参数中找到了参数,则进行替换
                    if (Objects.equal(replaceValue, "{" + corrField + "}")) {
                        parameters.put(replaceField, corrValue);
                    }
                }
            }
        }
    }

    /**
     * @description 对Post请求中的Json参数进行替换
     * @param correlateEntityList   已经找到的关联参数
     * @param tsDataEntity          单个测试步骤
     */
    private void replaceJsonParameter(List<CorrelateEntity> correlateEntityList, TestStepDataEntity tsDataEntity) {
        String jsonParameter = tsDataEntity.getJsonParams();
        for (CorrelateEntity correlateEntity: correlateEntityList) {
            String findParam = "{" + correlateEntity.getCorrField() + "}";
            if (jsonParameter.contains(findParam)) {
                jsonParameter = jsonParameter.replace(findParam, correlateEntity.getCorrValue());
            }
        }
        tsDataEntity.setJsonParams(jsonParameter);
    }

    /**
     * @description 对请求中的URL参数进行替换
     * @param correlateEntityList   已经找到的关联参数
     * @param tsDataEntity          单个测试步骤
     */
    private void replaceUrlParameter(List<CorrelateEntity> correlateEntityList, TestStepDataEntity tsDataEntity) {
        String url = tsDataEntity.getStepUrl();
        for (CorrelateEntity correlateEntity: correlateEntityList) {
            String findParam = "{" + correlateEntity.getCorrField() + "}";
            if (url.contains(findParam)) {
                url = url.replace(findParam, correlateEntity.getCorrValue());
            }
        }
        tsDataEntity.setStepUrl(url);
    }
}
