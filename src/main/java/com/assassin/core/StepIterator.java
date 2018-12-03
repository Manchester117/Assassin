package com.assassin.core;

import com.assassin.core.entity.TestCaseDataEntity;
import com.assassin.core.entity.TestCaseResponseEntity;
import com.assassin.core.entity.TestCaseResultEntity;
import com.assassin.core.entity.TestStepDataEntity;

import java.util.List;

/**
 * Created by Peng.Zhao on 2017/7/24.
 */
public class StepIterator {
    private TestCaseDataEntity tcDataEntity;
    private TestCaseResponseEntity tcResponseEntity;
    private TestCaseResultEntity tcResultEntity;

    /**
     * @description 构造方法,获取单个测试用例
     * @param tcDataEntity 用例实体
     */
    public StepIterator(TestCaseDataEntity tcDataEntity, TestCaseResponseEntity tcResponseEntity, TestCaseResultEntity tcResultEntity) {
        this.tcDataEntity = tcDataEntity;
        this.tcResponseEntity = tcResponseEntity;
        this.tcResultEntity = tcResultEntity;
    }

    /**
     * @description 获取单个测试用例的实体
     * @return 返回一个用例实体
     */
    public TestCaseDataEntity getTcDataEntity() {
        return tcDataEntity;
    }

    /**
     * @description 设置单个测试用例的实体
     * @param tcDataEntity 用例实体
     */
    public void setTcDataEntity(TestCaseDataEntity tcDataEntity) {
        this.tcDataEntity = tcDataEntity;
    }

    /**
     * @description 获取单个测试用例的响应实体
     * @return 测试用例响应实体
     */
    public TestCaseResponseEntity getTcResponseEntity() {
        return tcResponseEntity;
    }

    /**
     * @description 设置单个测试用例的响应实体
     * @param tcResponseEntity 测试用例的响应实体
     */
    public void setTcResponseEntity(TestCaseResponseEntity tcResponseEntity) {
        this.tcResponseEntity = tcResponseEntity;
    }

    public TestCaseResultEntity getTcResultEntity() {
        return tcResultEntity;
    }

    public void setTcResultEntity(TestCaseResultEntity tcResultEntity) {
        this.tcResultEntity = tcResultEntity;
    }

    /**
     * @description 用例步骤迭代器,获取测试用例中的单个步骤
     * @param i 步骤下标
     * @return 返回一个用例步骤,以实体封装
     */
    public TestStepDataEntity getSingleStep(int i) {
        List<TestStepDataEntity> tsDataEntity = this.tcDataEntity.getTestStepDataEntityList();
        return tsDataEntity.get(i);
    }
}
