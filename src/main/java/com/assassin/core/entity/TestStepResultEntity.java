package com.assassin.core.entity;

import com.assassin.core.entity.sql.ExecuteSQLEntity;
import com.assassin.core.entity.verify.ResponseVerifyEntity;

import java.util.List;

/**
 * Created by Peng.Zhao on 2017/7/28.
 * @description 单个测试步骤验证实体
 */
public class TestStepResultEntity {
    private String stepName;                                // 步骤名称
    private String stepUrl;                                 // 请求URL
    private int httpStatusCode;                             // 响应状态码
    private String httpResponseContent;                     // 响应正文
    private long httpResponseTime;                          // 响应时间
    private List<ResponseVerifyEntity> responseVerifyList;  // 检查点验证列表
    private List<ExecuteSQLEntity> fetchResultList;         // SQL查询结果列表
    private String imageName;

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

    public List<ResponseVerifyEntity> getResponseVerifyList() {
        return responseVerifyList;
    }

    public void setResponseVerifyList(List<ResponseVerifyEntity> responseVerifyList) {
        this.responseVerifyList = responseVerifyList;
    }

    public List<ExecuteSQLEntity> getFetchResultList() {
        return fetchResultList;
    }

    public void setFetchResultList(List<ExecuteSQLEntity> fetchResultList) {
        this.fetchResultList = fetchResultList;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "TestStepResultEntity{" +
                "stepName='" + stepName + '\'' +
                ", stepUrl='" + stepUrl + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                ", httpResponseContent='" + httpResponseContent + '\'' +
                ", httpResponseTime=" + httpResponseTime +
                ", responseVerifyList=" + responseVerifyList +
                ", fetchResultList=" + fetchResultList +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
