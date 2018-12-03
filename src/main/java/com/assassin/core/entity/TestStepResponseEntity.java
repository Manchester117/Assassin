package com.assassin.core.entity;


/**
 * Created by Peng.Zhao on 2017/7/24.
 * @description 响应实体
 */
public class TestStepResponseEntity {
    private String stepName;                    // 步骤名称
    private String stepUrl;                     // 请求地址
    private int httpStatusCode;                 // 响应状态码
    private String httpResponseContentType;     // 响应类型
    private String httpResponseContent;         // 响应正文
    private long httpResponseTime;              // 响应时间

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getStepUrl() {
        return stepUrl;
    }

    public void setStepUrl(String stepUrl) {
        this.stepUrl = stepUrl;
    }

    public String getHttpResponseContentType() {
        return httpResponseContentType;
    }

    public void setHttpResponseContentType(String httpResponseContentType) {
        this.httpResponseContentType = httpResponseContentType;
    }

    public String getHttpResponseContent() {
        return httpResponseContent;
    }

    public void setHttpResponseContent(String httpResponseContent) {
        this.httpResponseContent = httpResponseContent;
    }

    public long getHttpResponseTime() {
        return httpResponseTime;
    }

    public void setHttpResponseTime(long httpResponseTime) {
        this.httpResponseTime = httpResponseTime;
    }

    @Override
    public String toString() {
        return "TestStepResponseEntity{" +
                "stepName='" + stepName + '\'' +
                ", stepUrl='" + stepUrl + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                ", httpResponseContentType='" + httpResponseContentType + '\'' +
                ", httpResponseContent='" + httpResponseContent + '\'' +
                ", httpResponseTime=" + httpResponseTime +
                '}';
    }
}
