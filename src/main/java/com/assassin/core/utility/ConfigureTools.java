package com.assassin.core.utility;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/9/14.
 */
public class ConfigureTools {
    private static Map CONF_MAP = null;
    private static Map<String, String> DATA_SOURCE = null;

    /**
     * @description 读取配置文件,并进行解析
     */
    private static void loadConf() {
        Yaml yaml = new Yaml();
        // JAR运行
//        CommonTools commonTools = new CommonTools();
//        File confFile = new File(commonTools.getJarFolderPath() + "TestConfig.yml");
        // IDE运行
        File confFile = new File("TestConfig.yml");

        InputStream is = null;
        try {
            is = new FileInputStream(confFile);
            CONF_MAP = (Map)yaml.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println(CONF_MAP);
    }

    /**
     * @description 获取可以使用的环境的DB连接
     * @return 以Map形式返回数据库连接配置参数
     */
    public static Map<String, String> getActiveEnvironment() {
        ConfigureTools.loadConf();
        List environmentList = (List) CONF_MAP.get("environment");
        for (Object environmentItem: environmentList) {
            Map environment = (Map)environmentItem;
            if ((boolean)environment.get("active")) {
//                System.out.println(environment.get("datasource"));
                DATA_SOURCE = (Map<String, String>) environment.get("datasource");
                break;
            }
        }
        return DATA_SOURCE;
    }

    /**
     * @description 获取是否启用代理
     * @return 是否开启代理的boolean值
     */
    public static boolean isUseProxy() {
        ConfigureTools.loadConf();
        boolean isProxy = (boolean) CONF_MAP.get("proxy");
        return isProxy;
    }

    /**
     * @description 获取是否启用用例执行顺序的配置
     * @return  获取用例执行顺序
     */
    public static Map<String, Object> getCaseSequence() {
        ConfigureTools.loadConf();
        return (Map<String, Object>) CONF_MAP.get("caseSequence");
    }

//    public static void main(String[] args) {
//        ConfigureTools.isUseProxy();
//        ConfigureTools.getActiveEnvironment();
//        ConfigureTools.getCaseSequence();
//    }
}
