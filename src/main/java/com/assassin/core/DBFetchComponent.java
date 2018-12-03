package com.assassin.core;

import com.assassin.core.entity.TestStepDataEntity;
import com.assassin.core.entity.sql.ExecuteSQLEntity;
import com.assassin.core.utility.ConfigureTools;
import com.assassin.core.entity.TestStepResultEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/9/4.
 */
public class DBFetchComponent {
    private static Logger logger = LogManager.getLogger(DBFetchComponent.class.getName());
    private static DBFetchComponent CONN_INSTANCE = null;
    private Map<String, String> dbConf = null;
    private Connection conn = null;

    private String dbDriver;
    private String dbUrl;
    private String dbUserName;
    private String dbPassWord;

    /**
     * @description         私有构造
     */
    private DBFetchComponent() {
        this.dbConf = ConfigureTools.getActiveEnvironment();
    }

    /**
     * @description         单例设计
     * @return              返回DB操作类的单例对象
     */
    public static DBFetchComponent getInstance() {
        if (CONN_INSTANCE == null) {
            synchronized (DBFetchComponent.class) {
                if (CONN_INSTANCE == null) {
                    CONN_INSTANCE = new DBFetchComponent();
                }
            }
        }
        return CONN_INSTANCE;
    }

    /**
     * @description         创建数据库连接
     */
    public void createDBConnection() {
        this.dbDriver = this.dbConf.get("driver");
        this.dbUrl = this.dbConf.get("url");
        this.dbUserName = this.dbConf.get("username");
        this.dbPassWord = this.dbConf.get("password");

        try {
            Class.forName(this.dbDriver);
            this.conn = DriverManager.getConnection(this.dbUrl, this.dbUserName, this.dbPassWord);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description         关闭数据库连接
     */
    public void closeDBConnection() {
        if (this.conn != null) try { this.conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * @description         批量执行单步请求中的SQL语句,并返回结果List
     */
    public void executeSQLFetch(TestStepDataEntity tsDataEntity, TestStepResultEntity tsResultEntity) {
        List<ExecuteSQLEntity> sqlEntityList = tsDataEntity.getFetchSQLList();
        if (sqlEntityList != null) {
            for (ExecuteSQLEntity entity : sqlEntityList) {
                String sql = entity.getSql();
                List<String> columnList = entity.getColumnList();
                List<List<String>> singleFetchResultList = this.fetchSQLResult(sql, columnList);

                entity.setSingleFetchResultList(singleFetchResultList);
            }
            tsResultEntity.setFetchResultList(sqlEntityList);
        } else {
            tsResultEntity.setFetchResultList(null);
        }
    }

    /**
     * @description         执行单条SQL查询,并获取结果
     * @param sql           SQL语句
     * @param columnList    要查询的字段
     * @return              查询结果List
     */
    private List<List<String>> fetchSQLResult(String sql, List<String> columnList) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<List<String>> singleFetchResultList = new ArrayList<>();
        // 如果SQL/columnList不为空且不为空字符串则执行查询
        if (sql != null && !sql.isEmpty() && !columnList.isEmpty()) {
            try {
                ps = this.conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    List<String> singleLineResult = new ArrayList<>();
                    // 获取对应列的数据
                    for (String column : columnList) {
                        singleLineResult.add(rs.getString(column));
                    }
                    singleFetchResultList.add(singleLineResult);
                }
            } catch (SQLException e) {
                logger.error("SQL语句不正确! SQL:{}", sql);
                e.printStackTrace();
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
                if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        } else {
            return null;
        }
        // System.out.println(singleFetchResultList);
        return singleFetchResultList;
    }
}
