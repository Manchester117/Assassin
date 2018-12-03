package com.assassin.core;


import com.google.common.base.Objects;
import com.assassin.core.entity.TestCaseResultEntity;
import com.assassin.core.entity.TestStepResultEntity;
import com.assassin.core.entity.verify.ResponseVerifyEntity;

import java.util.List;

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
        List<ResponseVerifyEntity> vEntityList = tsResultEntity.getResponseVerifyList();
        // 如果验证点列表不为空
        if (!vEntityList.isEmpty()) {
            for (ResponseVerifyEntity vEntity: vEntityList) {
                String actualValue = vEntity.getActualValue().trim();
                String expectValue = vEntity.getExpectValue().trim();
                if (expectValue.isEmpty()) {
                    vEntity.setResultValue("未设置验证点");
                } else {
                    // 进行取值验证
                    if (Objects.equal(expectValue, actualValue)) {
                        vEntity.setResultValue("验证通过");
                    } else {
                        vEntity.setResultValue("验证失败");
                    }
                }
            }
        } else {
            ResponseVerifyEntity vEntity = new ResponseVerifyEntity();
            vEntity.setVerifyField("");
            vEntity.setExpectValue("");
            vEntity.setActualValue("");
            vEntity.setResultValue("未设置验证点");
            vEntityList.add(vEntity);
        }
    }

    /**
     * @description 将单步验证实体加入到流程实体的列表中,方便后面结果输出
     * @param tsResultEntity    单步验证实体
     */
    public void addStepResultInTCResultEntity(TestStepResultEntity tsResultEntity) {
        this.tcResultEntity.getTsResultEntityList().add(tsResultEntity);
    }
}
