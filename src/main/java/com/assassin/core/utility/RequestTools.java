package com.assassin.core.utility;

import com.assassin.core.RequestComponent;
import com.assassin.core.entity.TestStepResponseEntity;
import com.assassin.core.entity.TestStepDataEntity;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Peng.Zhao on 2017/7/26.
 */
public class RequestTools {
    /**
     * @description 设置本地代理,用于调试抓取数据包
     * @return 返回代理设置
     */
    public static DefaultProxyRoutePlanner setProxy() {
        HttpHost proxy = new HttpHost("127.0.0.1", 8888);
        return new DefaultProxyRoutePlanner(proxy);
    }

    /**
     * @description 根据测试步骤中的Content-Type选择发送Post的方法
     * @param reqComponent  请求封装类对象
     * @param httpClient    请求对象
     * @param tsDataEntity  单个测试步骤
     * @return  返回响应实体
     */
    public static TestStepResponseEntity selectContentTypeForHttpRequest(RequestComponent reqComponent, CloseableHttpClient httpClient, TestStepDataEntity tsDataEntity) {
        TestStepResponseEntity responseEntity = null;
        Map<String, String> headersMap = tsDataEntity.getHeadersMap();

        String contentTypeVal = headersMap.get("Content-Type");
        if (Objects.equals(contentTypeVal, "application/x-www-form-urlencoded")) {
            responseEntity = reqComponent.postFunction(tsDataEntity, httpClient);
        } else if (Objects.equals(contentTypeVal, "application/json")){
            responseEntity = reqComponent.jsonFunction(tsDataEntity, httpClient);
        } else if (Objects.equals(contentTypeVal, "multipart/form-data")) {
            responseEntity = reqComponent.uploadFunction(tsDataEntity, httpClient);
        } else {
            responseEntity = reqComponent.postFunction(tsDataEntity, httpClient);
        }
        return responseEntity;
    }

    /**
     * @description Cookie的拼接
     * @param cookiesMap 测试步骤的Cookie
     * @return 返回拼接后Cookie字符串
     */
    public static String setRequestCookie(Map<String, String> cookiesMap) {
        StringBuilder cookieContent = new StringBuilder();
        if (cookiesMap != null) {
            for (Map.Entry<String, String> entry: cookiesMap.entrySet()) {
                String cookieKey = entry.getKey();
                String cookieValue = entry.getValue();
                cookieContent.append(cookieKey).append("=").append(cookieValue).append("; ");
            }
        }
        return cookieContent.toString();
    }
}
