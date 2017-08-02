package com.highpin.core;

import com.highpin.core.entity.*;
import com.highpin.core.utitlity.ResponseTools;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Peng.Zhao on 2017/7/24.
 */
public class ResponseParser {
    private TestCaseDataEntity tcDataEntity;
    private TestCaseResponseEntity tcResponseEntity;

    public ResponseParser(TestCaseDataEntity tcDataEntity, TestCaseResponseEntity tcResponseEntity) {
        this.tcDataEntity = tcDataEntity;
        this.tcResponseEntity = tcResponseEntity;
    }

    /**
     * @description 将响应实体放置到测试用例响应实体当中
     * @param tsResponseEntity 单个测试步骤的响应实体
     */
    public void addStepRespInTCRespEntity(TestStepResponseEntity tsResponseEntity) {
        this.tcResponseEntity.getTsResponseEntityList().add(tsResponseEntity);
    }

    /**
     * @description 接收响应的路由方法,根据不同类型的响应执行解析响应操作
     * @param tsDataEntity      单个测试步骤
     * @param responseEntity    响应实体
     * @return                  返回单步验证实体
     */
    public TestStepResultEntity responseWrapper(TestStepDataEntity tsDataEntity, TestStepResponseEntity responseEntity) {
        List<Map<String, String>> verifyList = tsDataEntity.getVerifyList();
        Map<String, String> stepActualValueMap = new HashMap<>();
        Map<String, String> stepExpectValueMap = new HashMap<>();

        TestStepResultEntity tsResultEntity = new TestStepResultEntity();

        String stepName = tsDataEntity.getStepName();
        String url = tsDataEntity.getUrl();
        int httpStatusCode = responseEntity.getHttpStatusCode();
        String httpResponseTime = responseEntity.getHttpResponseTime();
        String responseContentType = responseEntity.getHttpResponseContentType();
        String responseContent = responseEntity.getHttpResponseContent();

        for (Map<String, String> verifyItem: verifyList) {

            Map<String, String> expectValueMapItem = new HashMap<>();
            expectValueMapItem.put(verifyItem.get("VerifyField"), verifyItem.get("CorrectValue"));
            stepExpectValueMap.putAll(expectValueMapItem);

            Map<String, String> actualValueMap = null;
            if (responseContentType.contains("text/html")) {
                actualValueMap = ResponseTools.contentSearchValueInHtml(this, verifyItem, responseContent);
                stepActualValueMap.putAll(actualValueMap);
            }
            if (responseContentType.contains("application/json")) {
                actualValueMap = ResponseTools.contentSearchValueInJson(this, verifyItem, responseContent);
                stepActualValueMap.putAll(actualValueMap);
            }
            if (responseContentType.contains("application/xml")) {
                actualValueMap = ResponseTools.contentSearchValueInXml(this, verifyItem, responseContent);
                stepActualValueMap.putAll(actualValueMap);
            }
        }

        // 如果响应获取到null(未获取正确响应),则将结果置为"未从响应获取值",避免模板引擎因为结果为null报错.
        stepActualValueMap = ResponseTools.noneValueConvertToString(stepActualValueMap);

        // 从响应信息转换为验证信息
        tsResultEntity.setStepName(stepName);
        tsResultEntity.setUrl(url);
        tsResultEntity.setHttpStatusCode(httpStatusCode);
        tsResultEntity.setHttpResponseContent(responseContent);
        tsResultEntity.setHttpResponseTime(httpResponseTime);
        // 将待验证值Map存入单步测试验证实体类中
        tsResultEntity.setStepActualMap(stepActualValueMap);
        tsResultEntity.setStepExpectMap(stepExpectValueMap);

        return tsResultEntity;
    }

    /**
     * @description 使用正则表达式对响应正文进行匹配,获取待验证值
     * @param verifyItem        单个验证Key-Value
     * @param responseContent   响应正文
     * @return                  返回待验证值的Key-Value
     */
    public Map<String, String> catchVerifyValueByRegex(Map<String, String> verifyItem, String responseContent) {
        Map<String, String> catchValueMap = new HashMap<>();
        String verifyField = verifyItem.get("VerifyField");

        Pattern pattern = Pattern.compile(verifyItem.get("Expression"));
        Matcher matcher = pattern.matcher(responseContent);
        if (matcher.find()) {
            String expectValue = matcher.group(1);
            catchValueMap.put(verifyField, expectValue);
        } else {
            catchValueMap.put(verifyField, null);
        }
        return catchValueMap;
    }

    /**
     * @description 使用JsonPath对响应正文进行匹配,获取待验证值
     * @param verifyItem        单个验证Key-Value
     * @param responseContent   响应正文
     * @return                  返回待验证值的Key-Value
     */
    public Map<String, String> catchVerifyValueByJsonPath(Map<String, String> verifyItem, String responseContent) {
        Map<String, String> catchValueMap = new HashMap<>();
        String verifyField = verifyItem.get("VerifyField");

        ReadContext ctx = JsonPath.parse(responseContent);
        // 如果从JSON中解析出来的值是整型,那么就做类型转换
        String expectValue = null;
        Object expectObject = ctx.read(verifyItem.get("Expression"));
        if (expectObject instanceof Integer) {
            expectValue = String.valueOf(expectObject);
        } else {
            expectValue = String.valueOf(expectObject);
        }
        if (expectValue != null) {
            catchValueMap.put(verifyField, expectValue);
        } else {
            catchValueMap.put(verifyField, null);
        }
        return catchValueMap;
    }

    /**
     * @description 使用CSS选择器对响应正文进行匹配,获取待验证值
     * @param verifyItem        单个验证Key-Value
     * @param responseContent   响应正文
     * @return                  返回待验证值的Key-Value
     */
    public Map<String, String> catchVerifyValueBySelector(Map<String, String> verifyItem, String responseContent) {
        Map<String, String> catchValueMap = new HashMap<>();
        String verifyField = verifyItem.get("VerifyField");

        Document doc = Jsoup.parse(responseContent);
        Element element = doc.select(verifyItem.get("Expression")).first();
        if (element != null) {
            String expectValue = element.text();
            catchValueMap.put(verifyField, expectValue);
        } else {
            catchValueMap.put(verifyField, null);
        }
        return catchValueMap;
    }

    /**
     * @description 使用XPath对响应正文进行匹配,获取待验证值
     * @param verifyItem        单个验证Key-Value
     * @param responseContent   响应正文
     * @return                  返回待验证值的Key-Value
     */
    public Map<String, String> catchVerifyValueByXpath(Map<String, String> verifyItem, String responseContent) {
        Map<String, String> catchValueMap = new HashMap<>();
        String verifyField = verifyItem.get("VerifyField");

        XML xml = new XMLDocument(responseContent);
        String expectValue = xml.xpath(verifyItem.get("Expression")).get(0);
        if (expectValue != null) {
            catchValueMap.put(verifyField, expectValue);
        } else {
            catchValueMap.put(verifyField, null);
        }
        return catchValueMap;
    }

    /**
     * @description 如果服务器没有返回正确的页面响应(没有返回带抓取值),则返回Value为空的Map
     * @param verifyItem    单个验证Key-Value
     * @return              返回Value为null的Map
     */
    public Map<String, String> otherVerifyValueNotMatch(Map<String, String> verifyItem) {
        Map<String, String> notCatchValueMap = new HashMap<>();

        String verifyField = verifyItem.get("VerifyField");
        notCatchValueMap.put(verifyField, null);
        return notCatchValueMap;
    }

    /**
     * @description 通过测试步骤中表明正则表达式从请求响应中获取上下文要关联的值
     * @param tsDataEntity      步骤实体
     * @param responseEntity    响应实体
     */
    public void catchCorrelationParamsByRegex(TestStepDataEntity tsDataEntity, TestStepResponseEntity responseEntity) {
        // 关联的结果--初始化一个Map
        Map<String, String> corrResultMap = new HashMap<>();
        // 用例中的关联参数名称和关联取值正则表达式
        Map<String, String> corrParamsMap = tsDataEntity.getCorrelationParamsMap();
        // 如果当前测试步骤中的关联参数不为null,则使用正则查找关联值
        if (corrParamsMap != null) {
            for (Map.Entry<String, String> entry : corrParamsMap.entrySet()) {
                // 载入正则表达式
                Pattern pattern = Pattern.compile(entry.getValue());
                // 正则表达式匹配响应
                Matcher matcher = pattern.matcher(responseEntity.getHttpResponseContent());
                if (matcher.find()) {
                    corrResultMap.put(entry.getKey(), matcher.group(1));
                    // 打印关联值
                    //System.out.println(matcher.group(1));
                }
            }
            // 把关联到的参数全部存入用例实体中的关联参数Map中
            this.tcDataEntity.getCorrelationParamKeyValue().putAll(corrResultMap);
        }
    }
}
