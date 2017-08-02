package com.highpin.core.entity;

import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/28.
 * @description 单个测试步骤验证实体
 */
public class TestStepResultEntity {
    private String stepName;                    // 步骤名称
    private String url;                         // 请求URL
    private int httpStatusCode;                 // 响应状态码
    private String httpResponseContent;         // 响应正文
    private String httpResponseTime;            // 响应时间
    private Map<String, String> stepExpectMap;  // 单步验证期望值
    private Map<String, String> stepActualMap;  // 单步验证抓取值
    private Map<String, String> stepResultMap;  // 单步验证结果

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

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getHttpResponseContent() {
        return httpResponseContent;
    }

    public void setHttpResponseContent(String httpResponseContent) {
        this.httpResponseContent = httpResponseContent;
    }

    public String getHttpResponseTime() {
        return httpResponseTime;
    }

    public void setHttpResponseTime(String httpResponseTime) {
        this.httpResponseTime = httpResponseTime;
    }

    public Map<String, String> getStepExpectMap() {
        return stepExpectMap;
    }

    public void setStepExpectMap(Map<String, String> stepExpectMap) {
        this.stepExpectMap = stepExpectMap;
    }

    public Map<String, String> getStepActualMap() {
        return stepActualMap;
    }

    public void setStepActualMap(Map<String, String> stepActualMap) {
        this.stepActualMap = stepActualMap;
    }

    public Map<String, String> getStepResultMap() {
        return stepResultMap;
    }

    public void setStepResultMap(Map<String, String> stepResultMap) {
        this.stepResultMap = stepResultMap;
    }
}
