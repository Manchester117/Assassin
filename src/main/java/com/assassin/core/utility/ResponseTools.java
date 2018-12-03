package com.assassin.core.utility;

import com.assassin.core.CoreEngine;
import com.google.common.base.Strings;
import com.assassin.core.entity.verify.DataVerifyEntity;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Peng.Zhao on 2017/7/27.
 * @description 对响应进行抓取处理的工具方法类
 */
public class ResponseTools {
    private static Logger logger = LogManager.getLogger(CoreEngine.class.getName());
    /**
     * @description                 正则表达式基础方法
     * @param expression            正则表达式字符串
     * @param index                 正则表达式元素索引位置
     * @param responseContent       请求响应正文
     * @return
     */
    public static String catchValueByRegex(String expression, int index, String responseContent) {
        String extractValue = null;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(responseContent);

        if (matcher.find()) {
            try {
                extractValue = matcher.group(index);
            } catch (IndexOutOfBoundsException e) {
                logger.error("正则表达式不正确: {}", expression);
                e.printStackTrace();
            }
        }
        return extractValue;
    }

    /**
     * @description                 JsonPath基础方法
     * @param expression            JsonPath表达式字符串
     * @param responseContent       请求响应正文
     * @return                      返回获取的值
     */
    public static String catchValueByJsonPath(String expression, String responseContent) {
        String extractValue = null;
        ReadContext ctx = JsonPath.parse(responseContent);

        // 如果从JSON中解析出来的值是整型,那么就做类型转换
        Object extractObject = ctx.read(expression);
        extractValue = String.valueOf(extractObject);

        return extractValue;
    }

    /**
     * @description                 CssSelector基础方法
     * @param expression            CssSelector表达式字符串
     * @param responseContent       请求响应正文
     * @return                      返回获取的值
     */
    public static String catchValueBySelector(String expression, String responseContent) {
        String extractValue = null;
        Document doc = Jsoup.parse(responseContent);
        Element element = doc.select(expression).first();

        if (element != null) {
            extractValue = element.text();
        }
        return extractValue;
    }

    /**
     * @description                 XPath基础方法
     * @param expression            XPath表达式字符串
     * @param responseContent       请求响应正文
     * @return                      返回获取的值
     */
    public static String catchValueByXPath(String expression, String responseContent) {
        String extractValue = null;
        XML xml = new XMLDocument(responseContent);
        extractValue = xml.xpath(expression).get(0);

        return extractValue;
    }

    /**
     * @description 使用正则表达式对响应正文进行匹配,获取待验证值
     * @param dataVerifyEntity          验证点对象
     * @param responseContent           响应正文
     * @return                          实际获取到的值
     */
    public static String catchVerifyValueByRegex(DataVerifyEntity dataVerifyEntity, String responseContent) {
        String actualValue = null;                          // 存放实际获取的结果
        String expression = dataVerifyEntity.getExpression();
        String indexString = dataVerifyEntity.getIndex();

        if (!Strings.isNullOrEmpty(indexString) && indexString.matches("\\d*")) {
            int index = Integer.parseInt(indexString);
            actualValue = catchValueByRegex(expression, index, responseContent);
        } else {
            actualValue = catchValueByRegex(expression, 1, responseContent);
        }

        if (Strings.isNullOrEmpty(actualValue)) {
            // 如果未能匹配到,这个地方不要给null,给空字符串,否则FreeMarker会因为字段为空的情况而导致异常
            actualValue = "";
        }

        return actualValue;
    }

    /**
     * @description 使用JsonPath对响应正文进行匹配,获取待验证值
     * @param dataVerifyEntity          验证点对象
     * @param responseContent           响应正文
     * @return                          实际获取到的值
     */
    public static String catchVerifyValueByJsonPath(DataVerifyEntity dataVerifyEntity, String responseContent) {
        String actualValue = null;                          // 存放实际获取的结果
        String expression = dataVerifyEntity.getExpression();

        actualValue = catchValueByJsonPath(expression, responseContent);
        if (Strings.isNullOrEmpty(actualValue)) {
            // 这个地方不要给null,给空字符串,否则FreeMarker会因为字段为空的情况而导致异常
            actualValue = "";
        }
        return actualValue;
    }

    /**
     * @description 使用CSS选择器对响应正文进行匹配,获取待验证值
     * @param dataVerifyEntity          验证点对象
     * @param responseContent           响应正文
     * @return                          实际获取到的值
     */
    public static String catchVerifyValueBySelector(DataVerifyEntity dataVerifyEntity, String responseContent) {
        String actualValue = null;                          // 存放实际获取的结果
        String expression = dataVerifyEntity.getExpression();

        actualValue = catchValueBySelector(expression, responseContent);
        if (Strings.isNullOrEmpty(actualValue)) {
            // 这个地方不要给null,给空字符串,否则FreeMarker会因为字段为空的情况而导致异常
            actualValue = "";
        }
        return actualValue;
    }

    /**
     * @description 使用XPath对响应正文进行匹配,获取待验证值
     * @param dataVerifyEntity          验证点对象
     * @param responseContent           响应正文
     * @return                          实际获取到的值
     */
    public static String catchVerifyValueByXpath(DataVerifyEntity dataVerifyEntity, String responseContent) {
        String actualValue = null;
        String expression = dataVerifyEntity.getExpression();

        actualValue = catchValueByXPath(expression, responseContent);
        if (Strings.isNullOrEmpty(actualValue)) {
            // 这个地方不要给null,给空字符串,否则FreeMarker会因为字段为空的情况而导致异常
            actualValue = "";
        }
        return actualValue;
    }
}
