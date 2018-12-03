package com.assassin.core.entity.verify;

public class DataVerifyEntity {
    private String verifyField;
    private String pattern;
    private String expression;
    private String index;
    private String expectValue;

    public String getVerifyField() {
        return verifyField;
    }

    public void setVerifyField(String verifyField) {
        this.verifyField = verifyField;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getExpectValue() {
        return expectValue;
    }

    public void setExpectValue(String expectValue) {
        this.expectValue = expectValue;
    }

    @Override
    public String toString() {
        return "DataVerifyEntity{" +
                "verifyField='" + verifyField + '\'' +
                ", pattern='" + pattern + '\'' +
                ", expression='" + expression + '\'' +
                ", index='" + index + '\'' +
                ", expectValue='" + expectValue + '\'' +
                '}';
    }
}
