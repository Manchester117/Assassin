package com.assassin.core.entity.verify;

public class ResponseVerifyEntity {
    private String verifyField;         // 验证字段
    private String expectValue;         // 预期值
    private String actualValue;         // 实际值
    private String resultValue;         // 验证结果

    public String getVerifyField() {
        return verifyField;
    }

    public void setVerifyField(String verifyField) {
        this.verifyField = verifyField;
    }

    public String getExpectValue() {
        return expectValue;
    }

    public void setExpectValue(String expectValue) {
        this.expectValue = expectValue;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    @Override
    public String toString() {
        return "ResponseVerifyEntity{" +
                "verifyField='" + verifyField + '\'' +
                ", expectValue='" + expectValue + '\'' +
                ", actualValue='" + actualValue + '\'' +
                ", resultValue='" + resultValue + '\'' +
                '}';
    }
}
