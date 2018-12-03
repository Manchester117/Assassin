package com.assassin.core.utility;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamGeneratorTools {
    /**
     * @description 替换请求参数中的唯一值(唯一值由@ltime@/@stime@/@uuid@)
     * @param param 参数
     * @return      返回替换后的参数
     */
    private static String replaceParameter(String param) {
        String replaceString = null;
        String replaceFlag = null;

        Pattern pattern = Pattern.compile("@(\\w+)@");
        Matcher matcher = pattern.matcher(param);
        while (matcher.find()) {
            // 对参数进行遍历,找出所有替换唯一值的标示,并进行替换
            replaceString = matcher.group(0);
            replaceFlag = matcher.group(1);

            if (replaceFlag.equals("time")) {
                String time = CommonTools.getNowTime();
                param = param.replaceAll(replaceString, time);
            }
            if (replaceFlag.equals("ltime")) {
                String longTime = CommonTools.getNowTimeByLong();
                param = param.replaceAll(replaceString, longTime);
            }
            if (replaceFlag.equals("stime")) {
                String shortTime = CommonTools.getNowTimeByShort();
                param = param.replaceAll(replaceString, shortTime);
            }
            if (replaceFlag.equals("uuid")) {
                String uuid = CommonTools.getUUID();
                param = param.replaceAll(replaceString, uuid);
            }
        }
        return param;
    }

    /**
     * @description         替换URL/JSON中的唯一参数
     * @param sequence      参数内容
     * @return              返回替换后的字符串
     */
    public static String replaceSequenceParameter(String sequence) {
        return replaceParameter(sequence);
    }

    /**
     * @description         替换GET/POST参数中的唯一参数
     * @param paramsList    参数内容
     * @return              返回替换后参数列表
     */
    public static List<Map<String, String>> replaceBodyParameter(List<Map<String, String>> paramsList) {
        for (Map<String, String> paramMap: paramsList) {
            for (Map.Entry<String, String> paramItem: paramMap.entrySet()) {
                String afterReplaceParam = replaceParameter(paramItem.getValue());
                // 替换原有参数,并存放回参数的数据结构中
                paramMap.put(paramItem.getKey(), afterReplaceParam);
            }
        }
        return paramsList;
    }
}
