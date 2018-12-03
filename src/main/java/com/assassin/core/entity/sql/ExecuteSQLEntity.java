package com.assassin.core.entity.sql;

import java.util.List;

/**
 * Created by Peng.Zhao on 2017/9/6.
 */
public class ExecuteSQLEntity {
    private String sql;
    private List<String> columnList;
    private List<List<String>> singleFetchResultList;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }

    public List<List<String>> getSingleFetchResultList() {
        return singleFetchResultList;
    }

    public void setSingleFetchResultList(List<List<String>> singleFetchResultList) {
        this.singleFetchResultList = singleFetchResultList;
    }

    @Override
    public String toString() {
        return "ExecuteSQLEntity{" +
                "sql='" + sql + '\'' +
                ", columnList=" + columnList +
                ", singleFetchResultList=" + singleFetchResultList +
                '}';
    }
}
