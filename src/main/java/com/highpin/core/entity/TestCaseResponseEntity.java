package com.highpin.core.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng.Zhao on 2017/7/27.
 */
public class TestCaseResponseEntity {
    private String businessName;                                // 业务流程名称
    private List<TestStepResponseEntity> tsResponseEntityList;  // 测试步骤响应列表

    public TestCaseResponseEntity(String businessName) {
        this.businessName = businessName;
        this.tsResponseEntityList = new ArrayList<>();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public List<TestStepResponseEntity> getTsResponseEntityList() {
        return tsResponseEntityList;
    }

    public void setTsResponseEntityList(List<TestStepResponseEntity> tsResponseEntityList) {
        this.tsResponseEntityList = tsResponseEntityList;
    }

    @Override
    public String toString() {
        return "TestCaseResponseEntity{" +
                "businessName='" + businessName + '\'' +
                ", tsResponseEntityList=" + tsResponseEntityList +
                '}';
    }
}
