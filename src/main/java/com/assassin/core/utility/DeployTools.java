package com.assassin.core.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by Peng.Zhao on 2017/8/17.
 */
public class DeployTools {
    private static Logger logger = LogManager.getLogger(DeployTools.class.getName());
    /**
     * @description: 创建用例和报告的文件夹
     * @return 返回当前路径
     */
    public String createFolder() {
        boolean caseCreateFlag = false;
        boolean reportCreateFlag = false;

        CommonTools commonTools = new CommonTools();
        String jarFolderPath = commonTools.getJarFolderPath();

        File caseFolder = new File(jarFolderPath + "case");
        if (!caseFolder.exists()) {
            caseCreateFlag = caseFolder.mkdirs();
        }

        File reportFolder = new File(jarFolderPath +  "report");
        if (!reportFolder.exists()) {
            reportCreateFlag = reportFolder.mkdirs();
        }

        if (caseCreateFlag && reportCreateFlag) {
            logger.info("测试文件目录初始化成功");
        }

        return jarFolderPath;
    }
}
