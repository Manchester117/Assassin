package com.highpin.core.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/24.
 * @description 测试步骤实体
 */
public class TestStepDataEntity {
    private String stepName;

    private String url;
    private String protocol;
    private String method;
    private Map<String, String> cookiesMap;
    private Map<String, String> headersMap;
    private List<Map<String, String>> getParamsList;
    private List<Map<String, String>> postParamsList;
    private String jsonParams;

    private Map<String, String> correlationParamsMap;

    private List<Map<String, String>> VerifyList;


    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getCookiesMap() {
        return cookiesMap;
    }

    public void setCookiesMap(Map<String, String> cookiesMap) {
        this.cookiesMap = cookiesMap;
    }

    public Map<String, String> getHeadersMap() {
        return headersMap;
    }

    public void setHeadersMap(Map<String, String> headersMap) {
        this.headersMap = headersMap;
    }

    public List<Map<String, String>> getGetParamsList() {
        return getParamsList;
    }

    public void setGetParamsList(List<Map<String, String>> getParamsList) {
        this.getParamsList = getParamsList;
    }

    public List<Map<String, String>> getPostParamsList() {
        return postParamsList;
    }

    public void setPostParamsList(List<Map<String, String>> postParamsList) {
        this.postParamsList = postParamsList;
    }

    public String getJsonParams() {
        return jsonParams;
    }

    public void setJsonParams(String jsonParams) {
        this.jsonParams = jsonParams;
    }

    public Map<String, String> getCorrelationParamsMap() {
        return correlationParamsMap;
    }

    public void setCorrelationParamsMap(Map<String, String> correlationParamsMap) {
        this.correlationParamsMap = correlationParamsMap;
    }

    public List<Map<String, String>> getVerifyList() {
        return VerifyList;
    }

    public void setVerifyList(List<Map<String, String>> verifyList) {
        VerifyList = verifyList;
    }

    @Override
    public String toString() {
        return "TestStepDataEntity{" +
                "stepName='" + stepName + '\'' +
                ", url='" + url + '\'' +
                ", protocol='" + protocol + '\'' +
                ", method='" + method + '\'' +
                ", cookiesMap=" + cookiesMap +
                ", headersMap=" + headersMap +
                ", getParamsList=" + getParamsList +
                ", postParamsList=" + postParamsList +
                ", jsonParams='" + jsonParams + '\'' +
                ", correlationParamsMap=" + correlationParamsMap +
                ", VerifyList=" + VerifyList +
                '}';
    }
}
