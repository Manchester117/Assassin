package com.highpin.core;


import com.highpin.core.entity.TestCaseResultEntity;
import com.highpin.core.entity.TestStepResultEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/28.
 */
public class ResultVerify {
    private TestCaseResultEntity tcResultEntity;

    /**
     * @description 构造方法,获取测试流程结果验证实体
     * @param tcResultEntity    验证实体
     */
    public ResultVerify(TestCaseResultEntity tcResultEntity) {
        this.tcResultEntity = tcResultEntity;
    }

    /**
     * @description 在获得单步响应结果后,对比用例中设定的预期值和实际获取到的值,进行验证
     * @param tsResultEntity    单步验证实体
     */
    public void stepValueVerify(TestStepResultEntity tsResultEntity) {
        Map<String, String> expectMap = tsResultEntity.getStepExpectMap();
        Map<String, String> actualMap = tsResultEntity.getStepActualMap();

        Map<String, String> stepResultMap = new HashMap<>();
        for (Map.Entry<String, String> entry: expectMap.entrySet()) {
            String verifyKey = entry.getKey();
            String verifyValue = actualMap.get(verifyKey);
            String expectValue = expectMap.get(verifyKey);
            // 这里使用期望值去比较待验证的值,避免待验证的值为null而导致空指针的情况
            if (expectValue.equals(verifyValue)) {
                stepResultMap.put(verifyKey, "验证通过");
            } else {
                stepResultMap.put(verifyKey, "验证失败");
            }
        }
        tsResultEntity.setStepResultMap(stepResultMap);
    }

    /**
     * @description 将单步验证实体加入到流程实体的列表中,方便后面结果输出
     * @param tsResultEntity    单步验证实体
     */
    public void addStepResultInTCResultEntity(TestStepResultEntity tsResultEntity) {
        this.tcResultEntity.getTsResultEntityList().add(tsResultEntity);
    }
}
