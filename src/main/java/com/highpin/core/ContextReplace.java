package com.highpin.core;

import com.highpin.core.entity.TestCaseDataEntity;
import com.highpin.core.entity.TestStepDataEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/25.
 */
public class ContextReplace {
    private TestCaseDataEntity tcDataEntity;

    public ContextReplace(TestCaseDataEntity tcDataEntity) {
        this.tcDataEntity = tcDataEntity;
    }

    /**
     * @description 执行上下文参数替换,替换Get/Post/Json/URL中标注的参数
     */
    public void replaceParameters() {
        String url = null;
        List<Map<String, String>> getParametersList = null;
        List<Map<String, String>> postParametersList = null;
        String jsonParameter = null;

        Map<String, String> corrParamKeyValue = this.tcDataEntity.getCorrelationParamKeyValue();
        List<TestStepDataEntity> tsDataEntityList = this.tcDataEntity.getTestStepDataEntityList();
        for (TestStepDataEntity tsDataEntity: tsDataEntityList) {
            url = tsDataEntity.getUrl();
            getParametersList = tsDataEntity.getGetParamsList();
            postParametersList = tsDataEntity.getPostParamsList();
            jsonParameter = tsDataEntity.getJsonParams();

            if (url != null) {
                this.replaceUrlParameter(corrParamKeyValue, tsDataEntity);
            }
            if (getParametersList != null) {
                this.replaceKeyValueParameters(corrParamKeyValue, getParametersList);
            }
            if (postParametersList != null) {
                this.replaceKeyValueParameters(corrParamKeyValue, postParametersList);
            }
            if (jsonParameter != null) {
                this.replaceJsonParameter(corrParamKeyValue, tsDataEntity);
            }
        }
        // 在替换完毕后,清空测试用例中的关联参数Map,避免重复替换.
        this.tcDataEntity.setCorrelationParamKeyValue(new HashMap<>());
    }

    /**
     * @description 对Get/Post请求中的Key-Value关联参数进行替换
     * @param corrParamKeyValue 已经找到的关联参数
     * @param parametersList    待替换的Key-Value参数
     */
    private void replaceKeyValueParameters(Map<String, String> corrParamKeyValue, List<Map<String, String>> parametersList) {
        // 遍历已经取到的关联值
        for (Map.Entry<String, String> corrEntry: corrParamKeyValue.entrySet()) {
            // 遍历单个测试步骤中Get参数列表
            for (Map<String, String> parameters: parametersList) {
                // 因为参数列表中存放的是单元素Key-Value,所以下面只会循环一次
                for (Map.Entry<String, String> replaceEntry: parameters.entrySet()) {
                    // 已经取到关联参数
                    String corrKey = corrEntry.getKey();
                    String corrValue = corrEntry.getValue();
                    // 待替换的关联参数
                    String replaceKey = replaceEntry.getKey();
                    String replaceValue = replaceEntry.getValue();
                    // 如果在待替换的关联参数中找到了参数,则进行替换
                    if (replaceValue.equals("{" + corrKey + "}")) {
                        parameters.put(replaceKey, corrValue);
                    }
                }
            }
        }
    }

    /**
     * @description 对Post请求中的Json参数进行替换
     * @param corrParamKeyValue 已经找到的关联参数
     * @param tsDataEntity      单个测试步骤
     */
    private void replaceJsonParameter(Map<String, String> corrParamKeyValue, TestStepDataEntity tsDataEntity) {
        String jsonParameter = tsDataEntity.getJsonParams();
        for (Map.Entry<String, String> corrEntry: corrParamKeyValue.entrySet()) {
            String findParam = "{" + corrEntry.getKey() + "}";
            if (jsonParameter.contains(findParam)) {
                jsonParameter = jsonParameter.replace(findParam, corrEntry.getValue());
            }
        }
        tsDataEntity.setJsonParams(jsonParameter);
    }

    /**
     * @description 对请求中的URL参数进行替换
     * @param corrParamKeyValue 已经找到的关联参数
     * @param tsDataEntity      单个测试步骤
     */
    private void replaceUrlParameter(Map<String, String> corrParamKeyValue, TestStepDataEntity tsDataEntity) {
        String url = tsDataEntity.getUrl();
        for (Map.Entry<String, String> corrEntry: corrParamKeyValue.entrySet()) {
            String findParam = "{" + corrEntry.getKey() + "}";
            if (url.contains(findParam)) {
                url = url.replace(findParam, corrEntry.getValue());
            }
        }
        tsDataEntity.setUrl(url);
    }
}
