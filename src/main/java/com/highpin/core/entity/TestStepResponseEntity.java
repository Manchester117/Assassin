package com.highpin.core.entity;


/**
 * Created by Peng.Zhao on 2017/7/24.
 * @description 响应实体
 */
public class TestStepResponseEntity {
    private String httpStepName;                // 步骤名称
    private int httpStatusCode;                 // 响应状态码
    private String httpUrl;                     // 请求地址
    private String httpResponseContentType;     // 响应类型
    private String httpResponseContent;         // 响应正文
    private String httpResponseTime;            // 响应时间

    public String getHttpStepName() {
        return httpStepName;
    }

    public void setHttpStepName(String httpStepName) {
        this.httpStepName = httpStepName;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
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

    public String getHttpResponseTime() {
        return httpResponseTime;
    }

    public void setHttpResponseTime(String httpResponseTime) {
        this.httpResponseTime = httpResponseTime;
    }

    @Override
    public String toString() {
        return "TestStepResponseEntity{" +
                "httpStepName='" + httpStepName + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                ", httpUrl='" + httpUrl + '\'' +
                ", httpResponseContentType='" + httpResponseContentType + '\'' +
                ", httpResponseContent='" + httpResponseContent + '\'' +
                ", httpResponseTime='" + httpResponseTime + '\'' +
                '}';
    }
}
