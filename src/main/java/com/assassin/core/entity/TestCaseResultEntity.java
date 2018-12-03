package com.assassin.core.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng.Zhao on 2017/7/28.
 * @description 测试用例的验证实体
 */
public class TestCaseResultEntity {
    private String businessName;
    private List<TestStepResultEntity> tsResultEntityList;

    public TestCaseResultEntity(String businessName) {
        this.businessName = businessName;
        this.tsResultEntityList = new ArrayList<>();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public List<TestStepResultEntity> getTsResultEntityList() {
        return tsResultEntityList;
    }

    public void setTsResultEntityList(List<TestStepResultEntity> tsResultEntityList) {
        this.tsResultEntityList = tsResultEntityList;
    }

    @Override
    public String toString() {
        return "TestCaseResultEntity{" +
                "businessName='" + businessName + '\'' +
                ", tsResultEntityList=" + tsResultEntityList +
                '}';
    }
}
