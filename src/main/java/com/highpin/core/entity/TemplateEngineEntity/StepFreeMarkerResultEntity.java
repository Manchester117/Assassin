package com.highpin.core.entity.TemplateEngineEntity;

import java.util.List;

/**
 * Created by Peng.Zhao on 2017/7/30.
 * @description FreeMarker报告展示实体(这里单独使用一个包主要是为了考虑后面可能会有很多种模板引擎)
 */
public class StepFreeMarkerResultEntity {
    private String stepName;
    private String stepUrl;
    private int httpStatusCode;
    private String httpResponseTime;
    private int verifyLength;
    private List<String> resultKeyList;
    private List<String> actualValueList;
    private List<String> expectValueList;
    private List<String> resultValueList;

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

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getHttpResponseTime() {
        return httpResponseTime;
    }

    public void setHttpResponseTime(String httpResponseTime) {
        this.httpResponseTime = httpResponseTime;
    }

    public int getVerifyLength() {
        return verifyLength;
    }

    public void setVerifyLength(int verifyLength) {
        this.verifyLength = verifyLength;
    }

    public List<String> getResultKeyList() {
        return resultKeyList;
    }

    public void setResultKeyList(List<String> resultKeyList) {
        this.resultKeyList = resultKeyList;
    }

    public List<String> getActualValueList() {
        return actualValueList;
    }

    public void setActualValueList(List<String> actualValueList) {
        this.actualValueList = actualValueList;
    }

    public List<String> getExpectValueList() {
        return expectValueList;
    }

    public void setExpectValueList(List<String> expectValueList) {
        this.expectValueList = expectValueList;
    }

    public List<String> getResultValueList() {
        return resultValueList;
    }

    public void setResultValueList(List<String> resultValueList) {
        this.resultValueList = resultValueList;
    }
}
