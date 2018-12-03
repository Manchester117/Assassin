package com.assassin.core.entity.template;

import com.assassin.core.entity.sql.ExecuteSQLEntity;

import java.util.List;

/**
 * Created by Peng.Zhao on 2017/7/30.
 * @description FreeMarker报告展示实体(这里单独使用一个包主要是为了考虑后面可能会有很多种模板引擎)
 */
public class StepFreeMarkerResultEntity {
    private String stepName;
    private String stepUrl;
    private int httpStatusCode;
    private long httpResponseTime;
    private int verifyLength;
    private List<String> VerifyFieldList;
    private List<String> actualValueList;
    private List<String> expectValueList;
    private List<String> resultValueList;
    private String imageName;
    private List<ExecuteSQLEntity> sqlFetchEntityList;

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

    public long getHttpResponseTime() {
        return httpResponseTime;
    }

    public void setHttpResponseTime(long httpResponseTime) {
        this.httpResponseTime = httpResponseTime;
    }

    public int getVerifyLength() {
        return verifyLength;
    }

    public void setVerifyLength(int verifyLength) {
        this.verifyLength = verifyLength;
    }

    public List<String> getVerifyFieldList() {
        return VerifyFieldList;
    }

    public void setVerifyFieldList(List<String> verifyFieldList) {
        this.VerifyFieldList = verifyFieldList;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public List<ExecuteSQLEntity> getSqlFetchEntityList() {
        return sqlFetchEntityList;
    }

    public void setSqlFetchEntityList(List<ExecuteSQLEntity> sqlFetchEntityList) {
        this.sqlFetchEntityList = sqlFetchEntityList;
    }

    @Override
    public String toString() {
        return "StepFreeMarkerResultEntity{" +
                "stepName='" + stepName + '\'' +
                ", stepUrl='" + stepUrl + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                ", httpResponseTime=" + httpResponseTime +
                ", verifyLength=" + verifyLength +
                ", VerifyFieldList=" + VerifyFieldList +
                ", actualValueList=" + actualValueList +
                ", expectValueList=" + expectValueList +
                ", resultValueList=" + resultValueList +
                ", imageName='" + imageName + '\'' +
                ", sqlFetchEntityList=" + sqlFetchEntityList +
                '}';
    }
}
