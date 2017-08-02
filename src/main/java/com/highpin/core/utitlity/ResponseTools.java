package com.highpin.core.utitlity;

import com.highpin.core.ResponseParser;

import java.util.Map;

/**
 * Created by Peng.Zhao on 2017/7/27.
 * @description 类中的三个方法本来可以合并为一个.但为了考虑处理类型的清晰程度,分为三个方法.
 */
public class ResponseTools {
    /**
     * @description 当响应的Content-Type为text/html时调用此方法,根据正则或者Css选择器的方式获取待验证的文本
     * @param parser            响应解析器对象
     * @param verifyItem        测试步骤中的验证列表
     * @param responseContent   响应正文
     * @return                  返回一个待验证值的Key-Value
     */
    public static Map<String, String> contentSearchValueInHtml(ResponseParser parser, Map<String, String> verifyItem, String responseContent) {
        String patternMode = verifyItem.get("PatternMode");
        Map<String, String> verifyValueMap = null;

        if (patternMode.equals("regex")) {
            verifyValueMap = parser.catchVerifyValueByRegex(verifyItem, responseContent);
        } else if (patternMode.equals("selector")) {
            verifyValueMap = parser.catchVerifyValueBySelector(verifyItem, responseContent);
        } else {
            verifyValueMap = parser.otherVerifyValueNotMatch(verifyItem);
        }
        return verifyValueMap;
    }

    /**
     * @description 当响应的Content-Type为application/json时调用此方法,根据正则或者JsonPath的方式获取待验证的文本
     * @param parser            响应解析器对象
     * @param verifyItem        测试步骤中的验证列表
     * @param responseContent   响应正文
     * @return                  返回一个待验证值的Key-Value
     */
    public static Map<String, String> contentSearchValueInJson(ResponseParser parser, Map<String, String> verifyItem, String responseContent) {
        String patternMode = verifyItem.get("PatternMode");
        Map<String, String> verifyValueMap = null;

        if (patternMode.equals("regex")) {
            verifyValueMap = parser.catchVerifyValueByRegex(verifyItem, responseContent);
        } else if (patternMode.equals("jsonPath")) {
            verifyValueMap = parser.catchVerifyValueByJsonPath(verifyItem, responseContent);
        } else {
            verifyValueMap = parser.otherVerifyValueNotMatch(verifyItem);
        }
        return verifyValueMap;
    }

    /**
     * @description 当响应的Content-Type为application/xml时调用此方法,根据正则或者Xpath的方式获取待验证的文本
     * @param parser            响应解析器对象
     * @param verifyItem        测试步骤中的验证列表
     * @param responseContent   响应正文
     * @return                  返回一个待验证值的Key-Value
     */
    public static Map<String, String> contentSearchValueInXml(ResponseParser parser, Map<String, String> verifyItem, String responseContent) {
        String patternMode = verifyItem.get("PatternMode");
        Map<String, String> verifyValueMap = null;

        if (patternMode.equals("regex")) {
            verifyValueMap = parser.catchVerifyValueByRegex(verifyItem, responseContent);
        } else if (patternMode.equals("xpath")) {
            verifyValueMap = parser.catchVerifyValueByXpath(verifyItem, responseContent);
        } else {
            verifyValueMap = parser.otherVerifyValueNotMatch(verifyItem);
        }
        return verifyValueMap;
    }

    /**
     * @description 如果响应获取到null(未获取正确响应),则将结果置为"未从响应获取值",避免模板引擎因为结果为null报错.
     * @param stepActualValueMap    待转换的Map
     * @return                      返回替换后的Map
     */
    public static Map<String, String> noneValueConvertToString(Map<String, String> stepActualValueMap) {
        for (Map.Entry<String, String> entry: stepActualValueMap.entrySet()) {
            if (entry.getValue() == null)
                stepActualValueMap.put(entry.getKey(), "未从响应获取值");
        }
        return stepActualValueMap;
    }
}
