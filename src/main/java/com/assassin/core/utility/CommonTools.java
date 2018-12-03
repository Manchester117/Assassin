package com.assassin.core.utility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Peng.Zhao on 2017/8/1.
 */
public class CommonTools {
    /**
     * @description 获取当前时间的字符串
     * @return      返回时间字符串
     */
    public static String getNowTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(date);
    }

    /**
     * @description 获取当前时间的字符串
     * @return      返回时间字符串
     */
    public static String  getNowTimeByShort() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分ss秒");
        String shortTime = sdf.format(date);
        return numberToString(shortTime);
    }

    /**
     * @description 获取当前时间的字符串
     * @return      返回时间字符串(Long)
     */
    public static String getNowTimeByLong() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        String longTime = sdf.format(date);
        return numberToString(longTime);
    }

    /**
     * @description 获取UUID
     * @return      返回UUID字符串
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return uuidStr.substring(0, 8) +
                uuidStr.substring(9, 13) +
                uuidStr.substring(14, 18) +
                uuidStr.substring(19, 23) +
                uuidStr.substring(24);
    }

    /**
     * @description         将阿拉伯数字转成中文
     * @param numberStr     内容为数字的字符串
     * @return              返回中文数字的字符串
     */
    private static String numberToString(String numberStr) {
        Map<String, String> numTable = new LinkedHashMap<>();
        numTable.put("0", "零");numTable.put("1", "一");
        numTable.put("2", "二");numTable.put("3", "三");
        numTable.put("4", "四");numTable.put("5", "五");
        numTable.put("6", "六");numTable.put("7", "七");
        numTable.put("8", "八");numTable.put("9", "九");

        StringBuilder numStrLine = new StringBuilder();

        // 对待转换字符串挨个分割字符
        for (String s: numberStr.split("")) {
            if (numTable.get(s) != null) {
                numStrLine.append(numTable.get(s));
            } else {
                numStrLine.append(s);
            }
        }
        return numStrLine.toString();
    }

    /**
     * @description 暂停方法
     * @param ms    毫秒
     */
    public static void sleepWait(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 创建文件夹方法
     * @param businessName  用流程名称当做报告文件夹名称
     * @return  返回创建文件夹boolean值
     */
    public static String createReportFolder(String businessName) {
        String reportFolderPath = "report" + File.separator + businessName + "_" + CommonTools.getNowTime();
        File caseReportFolder = new File(reportFolderPath);
        if (!caseReportFolder.exists()) {
            caseReportFolder.mkdirs();
        }
        return reportFolderPath;
    }

    /**
     * @description 删除文件方法
     * @param deleteFile    待删除的文件
     * @return  返回删除文件boolean值
     */
    public static boolean deleteFile(File deleteFile) {
        boolean deleteFileFlag = false;
        if (deleteFile.exists() && deleteFile.isFile()) {
            deleteFileFlag = deleteFile.delete();
        }
        return deleteFileFlag;
    }

    /**
     * @description 获取JAR文件当前存放文件夹路径
     * @return      返回JAR的当前文件夹路径
     */
    public String getJarFolderPath() {
        // 获取JAR包完全路径名
        String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            // 路径中存在中文则必须对路径进行转码
            jarPath = URLDecoder.decode(jarPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 根据路径名获取存放目录
        return new File(jarPath).getParentFile().getAbsolutePath() + File.separator;
    }

//    public static void main(String[] args) {
//        System.out.println(CommonTools.getNowTimeByShort());
//    }
}
