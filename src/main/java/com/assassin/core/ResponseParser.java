package com.assassin.core;

import com.assassin.core.entity.*;
import com.google.common.base.Strings;
import com.assassin.core.entity.verify.DataVerifyEntity;
import com.assassin.core.entity.verify.ResponseVerifyEntity;
import com.assassin.core.utility.ResponseTools;

import java.util.ArrayList;
import java.util.List;

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
        List<DataVerifyEntity> dataVerifyEntityList = tsDataEntity.getDataVerifyEntityList();

        // 初始化单步验证结果实体
        TestStepResultEntity tsResultEntity = new TestStepResultEntity();
        List<ResponseVerifyEntity> vEntityList = new ArrayList<>();

        String stepName = tsDataEntity.getStepName();
        String url = tsDataEntity.getStepUrl();
        int httpStatusCode = responseEntity.getHttpStatusCode();
        long httpResponseTime = responseEntity.getHttpResponseTime();
        String responseContentType = responseEntity.getHttpResponseContentType();
        String responseContent = responseEntity.getHttpResponseContent();

        if (dataVerifyEntityList != null && dataVerifyEntityList.size() > 0) {
            // 如果验证列表不为空,并且列表长度不为0的情况,则进行验证.
            for (DataVerifyEntity dataVerifyEntity: dataVerifyEntityList) {
                ResponseVerifyEntity vEntity = new ResponseVerifyEntity();

                vEntity.setVerifyField(dataVerifyEntity.getVerifyField());
                vEntity.setExpectValue(dataVerifyEntity.getExpectValue());

                String actualValue = null;
                // 根据响应的Content-Type来判断使用哪种验证数据获取方式
                if (responseContentType.contains("text/html")) {
                    actualValue = this.contentSearchValueInHtml(dataVerifyEntity, responseContent);
                } else if (responseContentType.contains("application/json")) {
                    actualValue = this.contentSearchValueInJson(dataVerifyEntity, responseContent);
                } else if (responseContentType.contains("application/xml")) {
                    actualValue = this.contentSearchValueInXml(dataVerifyEntity, responseContent);
                }
                vEntity.setActualValue(actualValue);
                vEntityList.add(vEntity);
            }
        }

        // 从响应信息转换为验证信息
        tsResultEntity.setStepName(stepName);
        tsResultEntity.setStepUrl(url);
        tsResultEntity.setHttpStatusCode(httpStatusCode);
        tsResultEntity.setHttpResponseContent(responseContent);
        tsResultEntity.setHttpResponseTime(httpResponseTime);
        // 将待验证值Map存入单步测试验证实体类中
        tsResultEntity.setResponseVerifyList(vEntityList);

        return tsResultEntity;
    }

    /**
     * @description                 判断验证点中是否缺少必要元素
     * @param dataVerifyEntity      验证点实体对象
     * @return
     */
    private boolean isVerifyExist(DataVerifyEntity dataVerifyEntity) {
        String patternMode = dataVerifyEntity.getPattern();
        String expression = dataVerifyEntity.getExpression();
        String expectValue = dataVerifyEntity.getExpectValue();

        if (Strings.isNullOrEmpty(patternMode)) {
            return false;
        }
        if (Strings.isNullOrEmpty(expression)) {
            return false;
        }
        if (Strings.isNullOrEmpty(expectValue)) {
            return false;
        }
        return true;
    }

    /**
     * @description 当响应的Content-Type为text/html时调用此方法,根据正则或者Css选择器的方式获取待验证的文本
     * @param dataVerifyEntity          验证点实体对象
     * @param responseContent           响应正文
     * @return                          返回一个待验证值的Key-Value
     */
    private String contentSearchValueInHtml(DataVerifyEntity dataVerifyEntity, String responseContent) {
        String actualValue = null;

        if (this.isVerifyExist(dataVerifyEntity) && !Strings.isNullOrEmpty(responseContent)) {
            String patternMode = dataVerifyEntity.getPattern();
            if (patternMode.equals("regex")) {
                actualValue = ResponseTools.catchVerifyValueByRegex(dataVerifyEntity, responseContent);
            } else if (patternMode.equals("selector")) {
                actualValue = ResponseTools.catchVerifyValueBySelector(dataVerifyEntity, responseContent);
            } else {
                actualValue = "";       // 字段用例中没有检查点的查找方式
            }
        } else {
            actualValue = "";           // 如果响应正文为空
        }
        return actualValue;
    }

    /**
     * @description 当响应的Content-Type为application/json时调用此方法,根据正则或者JsonPath的方式获取待验证的文本
     * @param dataVerifyEntity          验证点实体对象
     * @param responseContent           响应正文
     * @return                          返回一个待验证值的Key-Value
     */
    private String contentSearchValueInJson(DataVerifyEntity dataVerifyEntity, String responseContent) {
        String actualValue = null;

        if (this.isVerifyExist(dataVerifyEntity) && !Strings.isNullOrEmpty(responseContent)) {
            String patternMode = dataVerifyEntity.getPattern();
            if (patternMode.equals("regex")) {
                actualValue = ResponseTools.catchVerifyValueByRegex(dataVerifyEntity, responseContent);
            } else if (patternMode.equals("jsonPath")) {
                actualValue = ResponseTools.catchVerifyValueByJsonPath(dataVerifyEntity, responseContent);
            } else {
                actualValue = "";       // 字段用例中没有检查点的查找方式
            }
        } else {
            actualValue = "";           // 如果响应正文为空
        }
        return actualValue;
    }

    /**
     * @description 当响应的Content-Type为application/xml时调用此方法,根据正则或者Xpath的方式获取待验证的文本
     * @param dataVerifyEntity          验证点实体对象
     * @param responseContent           响应正文
     * @return                          返回一个待验证值的Key-Value
     */
    private String contentSearchValueInXml(DataVerifyEntity dataVerifyEntity, String responseContent) {
        String actualValue = null;

        if (this.isVerifyExist(dataVerifyEntity) && !Strings.isNullOrEmpty(responseContent)) {
            String patternMode = dataVerifyEntity.getPattern();
            if (patternMode.equals("regex")) {
                actualValue = ResponseTools.catchVerifyValueByRegex(dataVerifyEntity, responseContent);
            } else if (patternMode.equals("xpath")) {
                actualValue = ResponseTools.catchVerifyValueByXpath(dataVerifyEntity, responseContent);
            } else {
                actualValue = "";       // 字段用例中没有检查点的查找方式
            }
        } else {
            actualValue = "";           // 如果响应正文为空
        }
        return actualValue;
    }
}
