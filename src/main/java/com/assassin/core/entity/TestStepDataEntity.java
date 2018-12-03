package com.assassin.core.entity;

import com.assassin.core.entity.correlate.CorrelateEntity;
import com.assassin.core.entity.sql.ExecuteSQLEntity;
import com.assassin.core.entity.verify.DataVerifyEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/24.
 * @description 测试步骤实体
 */
public class TestStepDataEntity {
    private String stepName;
    private String stepUrl;
    private String protocol;
    private String method;
    private Map<String, String> cookiesMap;
    private Map<String, String> headersMap;
    private List<Map<String, String>> getParamsList;
    private List<Map<String, String>> postParamsList;
    private String jsonParams;
    private List<CorrelateEntity> correlateEntityList;      // 关联的上下文参数
    private List<DataVerifyEntity> dataVerifyEntityList;    // 验证点List
    private List<ExecuteSQLEntity> fetchSQLList;            // SQL查询检查
//    private String isCaptureImage;                          // 是否截图

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepUrl() {
        return stepUrl;
    }

    public void setStepUrl(String stepUrl) {
        this.stepUrl = stepUrl;
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

    public List<CorrelateEntity> getCorrelateEntityList() {
        return correlateEntityList;
    }

    public void setCorrelateEntityList(List<CorrelateEntity> correlateEntityList) {
        this.correlateEntityList = correlateEntityList;
    }

    public List<DataVerifyEntity> getDataVerifyEntityList() {
        return dataVerifyEntityList;
    }

    public void setDataVerifyEntityList(List<DataVerifyEntity> dataVerifyEntityList) {
        this.dataVerifyEntityList = dataVerifyEntityList;
    }

    public List<ExecuteSQLEntity> getFetchSQLList() {
        return fetchSQLList;
    }

    public void setFetchSQLList(List<ExecuteSQLEntity> fetchSQLList) {
        this.fetchSQLList = fetchSQLList;
    }

    @Override
    public String toString() {
        return "TestStepDataEntity{" +
                "stepName='" + stepName + '\'' +
                ", stepUrl='" + stepUrl + '\'' +
                ", protocol='" + protocol + '\'' +
                ", method='" + method + '\'' +
                ", cookiesMap=" + cookiesMap +
                ", headersMap=" + headersMap +
                ", getParamsList=" + getParamsList +
                ", postParamsList=" + postParamsList +
                ", jsonParams='" + jsonParams + '\'' +
                ", correlateEntityList=" + correlateEntityList +
                ", dataVerifyEntityList=" + dataVerifyEntityList +
                ", fetchSQLList=" + fetchSQLList +
                '}';
    }
}
