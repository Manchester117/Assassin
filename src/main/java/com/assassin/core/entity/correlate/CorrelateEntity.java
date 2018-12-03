package com.assassin.core.entity.correlate;

public class CorrelateEntity {
    private String corrField;
    private String corrPattern;
    private String corrExpression;
    private String corrIndex;
    private String corrValue;
    private String isUseForFetchDB;

    public String getCorrField() {
        return corrField;
    }

    public void setCorrField(String corrField) {
        this.corrField = corrField;
    }

    public String getCorrPattern() {
        return corrPattern;
    }

    public void setCorrPattern(String corrPattern) {
        this.corrPattern = corrPattern;
    }

    public String getCorrExpression() {
        return corrExpression;
    }

    public void setCorrExpression(String corrExpression) {
        this.corrExpression = corrExpression;
    }

    public String getCorrIndex() {
        return corrIndex;
    }

    public void setCorrIndex(String corrIndex) {
        this.corrIndex = corrIndex;
    }

    public String getCorrValue() {
        return corrValue;
    }

    public void setCorrValue(String corrValue) {
        this.corrValue = corrValue;
    }

    public String getIsUseForFetchDB() {
        return isUseForFetchDB;
    }

    public void setIsUseForFetchDB(String isUseForFetchDB) {
        this.isUseForFetchDB = isUseForFetchDB;
    }

    @Override
    public String toString() {
        return "CorrelateEntity{" +
                "corrField='" + corrField + '\'' +
                ", corrPattern='" + corrPattern + '\'' +
                ", corrExpression='" + corrExpression + '\'' +
                ", corrIndex='" + corrIndex + '\'' +
                ", corrValue='" + corrValue + '\'' +
                ", isUseForFetchDB='" + isUseForFetchDB + '\'' +
                '}';
    }
}
