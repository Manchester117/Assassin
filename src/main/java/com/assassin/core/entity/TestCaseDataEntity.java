package com.assassin.core.entity;

import com.assassin.core.entity.correlate.CorrelateEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng.Zhao on 2017/7/24.
 * @description 测试用例(流程)实体
 */
public class TestCaseDataEntity {
    private String businessName;
    private List<TestStepDataEntity> testStepDataEntityList;
    private int stepLength;
    private List<CorrelateEntity> correlateEntityList;      // 用例关联上下文

    public TestCaseDataEntity() {
        // 通过构造方法初始化关联上下文
        correlateEntityList = new ArrayList<>();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public List<TestStepDataEntity> getTestStepDataEntityList() {
        return testStepDataEntityList;
    }

    public void setTestStepDataEntityList(List<TestStepDataEntity> testStepDataEntityList) {
        this.testStepDataEntityList = testStepDataEntityList;
    }

    public int getStepLength() {
        return stepLength;
    }

    public void setStepLength(int stepLength) {
        this.stepLength = stepLength;
    }

    public List<CorrelateEntity> getCorrelateEntityList() {
        return correlateEntityList;
    }

    public void setCorrelateEntityList(List<CorrelateEntity> correlateEntityList) {
        this.correlateEntityList = correlateEntityList;
    }

    @Override
    public String toString() {
        return "TestCaseDataEntity{" +
                "businessName='" + businessName + '\'' +
                ", testStepDataEntityList=" + testStepDataEntityList +
                ", stepLength=" + stepLength +
                ", correlateEntityList=" + correlateEntityList +
                '}';
    }
}
