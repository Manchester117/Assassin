package com.highpin.core.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/24.
 * @description 测试用例(流程)实体
 */
public class TestCaseDataEntity {
    private String businessName;
    private List<TestStepDataEntity> testStepDataEntityList;
    private int stepLength;
    private Map<String, String> correlationParamKeyValue;     // 用例关联上下文

    public TestCaseDataEntity() {
        // 通过构造方法初始化关联上下文
        this.correlationParamKeyValue = new HashMap<>();
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

    public Map<String, String> getCorrelationParamKeyValue() {
        return correlationParamKeyValue;
    }

    public void setCorrelationParamKeyValue(Map<String, String> correlationParamKeyValue) {
        this.correlationParamKeyValue = correlationParamKeyValue;
    }

    @Override
    public String toString() {
        return "TestCaseDataEntity{" +
                "businessName='" + businessName + '\'' +
                ", testStepDataEntityList=" + testStepDataEntityList +
                ", stepLength=" + stepLength +
                ", correlationParamKeyValue=" + correlationParamKeyValue +
                '}';
    }
}
