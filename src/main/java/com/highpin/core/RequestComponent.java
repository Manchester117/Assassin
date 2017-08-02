package com.highpin.core;

import com.highpin.core.entity.TestStepResponseEntity;
import com.highpin.core.entity.TestStepDataEntity;
import com.highpin.core.utitlity.RequestTools;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Peng.Zhao on 2017/7/10.
 */
public class RequestComponent {
    /**
     * @description 发送请求的路由方法,根据不同类型的请求执行请求
     * @param tsDataEntity 单个测试步骤
     * @return 返回响应
     */
    public TestStepResponseEntity requestWrapper(TestStepDataEntity tsDataEntity) {
        CloseableHttpClient httpClient = null;
        TestStepResponseEntity responseEntity = null;

        if ("http".equals(tsDataEntity.getProtocol())){
            // 代理方法调用,用于Fiddler截取请求,方便调试
            httpClient = HttpClients.custom().setRoutePlanner(RequestTools.setProxy()).build();
//            httpClient = HttpClients.createDefault();
            if ("get".equals(tsDataEntity.getMethod())) {
                responseEntity = this.getFunction(tsDataEntity, httpClient);
            } else if ("post".equals(tsDataEntity.getMethod())) {
                responseEntity = RequestTools.selectContentTypeForHttpRequest(this, httpClient, tsDataEntity);
            }
        } else if ("https".equals(tsDataEntity.getProtocol())) {
            System.out.println("使用Https, 后续实现.");
        }

        try {
            if (httpClient != null)
                httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseEntity;
    }

    /**
     * @description 执行Get请求,并获取返回
     * @param tsDataEntity  单个测试步骤
     * @param httpClient    Http请求对象
     * @return  响应对象
     */
    public TestStepResponseEntity getFunction(TestStepDataEntity tsDataEntity, CloseableHttpClient httpClient) {
        HttpGet httpGet = new HttpGet();
        CloseableHttpResponse httpResponse = null;
        // 响应时间计算
        long startTime = 0L;
        long endTime = 0L;
        long responseTime = 0L;
        // 定义响应实体
        TestStepResponseEntity responseEntity = new TestStepResponseEntity();

        // 设置cookie
        Map<String, String> cookiesMap = tsDataEntity.getCookiesMap();
        String cookieContent = RequestTools.setRequestCookie(cookiesMap);
        httpGet.setHeader("Cookie", cookieContent);

        // 设置请求头
        Map<String, String> headersMap = tsDataEntity.getHeadersMap();
        if (headersMap != null) {
            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 设置请求参数
        List<NameValuePair> getParams = new ArrayList<>();
        if (tsDataEntity.getGetParamsList() != null) {
            for (int i = 0; i < tsDataEntity.getGetParamsList().size(); ++i) {
                Map<String, String> getParamsMap = tsDataEntity.getGetParamsList().get(i);
                for (Map.Entry<String, String> entry: getParamsMap.entrySet()) {
                    getParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
        }
        String getParamsValue = null;
        // 发送请求
        try {
            // 对Get请求的参数进行编码,形成请求实体
            getParamsValue = EntityUtils.toString(new UrlEncodedFormEntity(getParams, Consts.UTF_8));
            // 创建Get请求
            httpGet.setURI(URI.create(tsDataEntity.getUrl() + "?" + getParamsValue));
            // 打印URL
            System.out.println("执行Get请求: " + httpGet.getURI());

            // 发送请求并计算响应时间
            startTime = System.currentTimeMillis();
            httpResponse = httpClient.execute(httpGet);
            endTime = System.currentTimeMillis();
            responseTime = endTime - startTime;

            // 将请求步骤名称放置到响应实体中
            responseEntity.setHttpStepName(tsDataEntity.getStepName());
            // 将请求URL放置到响应实体中
            responseEntity.setHttpUrl(tsDataEntity.getUrl());
            // 将响应时间放置到响应实体中
            responseEntity.setHttpResponseTime("响应时间: " + responseTime + "毫秒");
            // 将响应状态码放置到响应实体中
            responseEntity.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
            // 将响应类型放置到响应实体中
            responseEntity.setHttpResponseContentType(httpResponse.getEntity().getContentType().getValue());
            // 将响应正文放置到响应实体中
            responseEntity.setHttpResponseContent(EntityUtils.toString(httpResponse.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("响应时间: " + responseTime + "毫秒");

        return responseEntity;
    }

    /**
     * @description 执行Post请求,并获取返回
     * @param tsDataEntity  单个测试步骤
     * @param httpClient    Http请求对象
     * @return  响应对象
     */
    public TestStepResponseEntity postFunction(TestStepDataEntity tsDataEntity, CloseableHttpClient httpClient) {
        HttpPost httpPost = new HttpPost(tsDataEntity.getUrl());
        CloseableHttpResponse httpResponse = null;
        // 响应时间计算
        long startTime = 0L;
        long endTime = 0L;
        long responseTime = 0L;
        // 定义响应实体
        TestStepResponseEntity responseEntity = new TestStepResponseEntity();

        // 设置cookie
        Map<String, String> cookiesMap = tsDataEntity.getCookiesMap();
        String cookieContent = RequestTools.setRequestCookie(cookiesMap);
        httpPost.setHeader("Cookie", cookieContent);

        // 设置请求头
        Map<String, String> headersMap = tsDataEntity.getHeadersMap();
        if (headersMap != null) {
            for (Map.Entry<String, String> entry: headersMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // 设置请求参数
        List<NameValuePair> postParams = new ArrayList<>();
        if (tsDataEntity.getPostParamsList() != null) {
            for (int i = 0; i < tsDataEntity.getPostParamsList().size(); ++i) {
                Map<String, String> postParamsMap = tsDataEntity.getPostParamsList().get(i);
                for (Map.Entry<String, String> entry: postParamsMap.entrySet()) {
                    postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
        }

        // 发送请求
        UrlEncodedFormEntity ueFormEntity = null;
        try {
            // 对参数进行编码
            ueFormEntity = new UrlEncodedFormEntity(postParams, Consts.UTF_8);
            // 将参数放置在请求中
            httpPost.setEntity(ueFormEntity);
            // 打印URL
            System.out.println("执行Post请求: " + httpPost.getURI());

            // 发送请求并计算响应时间
            startTime = System.currentTimeMillis();
            httpResponse = httpClient.execute(httpPost);
            endTime = System.currentTimeMillis();
            responseTime = endTime - startTime;

            // 将请求步骤名称放置到响应实体中
            responseEntity.setHttpStepName(tsDataEntity.getStepName());
            // 将请求URL放置到响应实体中
            responseEntity.setHttpUrl(tsDataEntity.getUrl());
            // 将响应时间放置到响应实体中
            responseEntity.setHttpResponseTime("响应时间: " + responseTime + "毫秒");
            // 将响应状态码放置到响应实体中
            responseEntity.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
            // 将响应类型放置到响应实体中
            responseEntity.setHttpResponseContentType(httpResponse.getEntity().getContentType().getValue());
            // 将响应正文放置到响应实体中
            responseEntity.setHttpResponseContent(EntityUtils.toString(httpResponse.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("响应时间: " + responseTime + "毫秒");

        return responseEntity;
    }

    /**
     * @description 执行Post(Json参数)请求,并获取返回
     * @param tsDataEntity  单个测试步骤
     * @param httpClient    Http请求对象
     * @return  响应对象
     */
    public TestStepResponseEntity jsonFunction(TestStepDataEntity tsDataEntity, CloseableHttpClient httpClient) {
        HttpPost httpPost = new HttpPost(tsDataEntity.getUrl());
        CloseableHttpResponse httpResponse = null;
        // 响应时间计算
        long startTime = 0L;
        long endTime = 0L;
        long responseTime = 0L;
        // 定义响应实体
        TestStepResponseEntity responseEntity = new TestStepResponseEntity();

        // 对Json进行做UTF-8编码
        StringEntity requestEntity = new StringEntity(tsDataEntity.getJsonParams(), Consts.UTF_8);
        requestEntity.setContentEncoding("UTF-8");

        // 设置cookie
        Map<String, String> cookiesMap = tsDataEntity.getCookiesMap();
        String cookieContent = RequestTools.setRequestCookie(cookiesMap);
        httpPost.setHeader("Cookie", cookieContent);

        // 设置请求头
        Map<String, String> headersMap = tsDataEntity.getHeadersMap();
        if (headersMap != null) {
            for (Map.Entry<String, String> entry: headersMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 将Json放置在请求体中
        httpPost.setEntity(requestEntity);

        // 发送带有Json的Post请求
        try {
            // 打印请求URL
            System.out.println("执行Json请求: " + httpPost.getURI());
            // 发送请求并计算响应时间
            startTime = System.currentTimeMillis();
            httpResponse = httpClient.execute(httpPost);
            endTime = System.currentTimeMillis();
            responseTime = endTime - startTime;

            // 将请求步骤名称放置到响应实体中
            responseEntity.setHttpStepName(tsDataEntity.getStepName());
            // 将请求URL放置到响应实体中
            responseEntity.setHttpUrl(tsDataEntity.getUrl());
            // 将响应时间放置到响应实体中
            responseEntity.setHttpResponseTime("响应时间: " + responseTime + "毫秒");
            // 将响应状态码放置到响应实体中
            responseEntity.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
            // 将响应类型放置到响应实体中
            responseEntity.setHttpResponseContentType(httpResponse.getEntity().getContentType().getValue());
            // 将响应正文放置到响应实体中
            responseEntity.setHttpResponseContent(EntityUtils.toString(httpResponse.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("响应时间: " + responseTime + "毫秒");

        return responseEntity;
    }

    /**
     * @description 执行Post(上传文件)请求,并获取返回
     * @param tsDataEntity  单个测试步骤
     * @param httpClient    Http请求对象
     * @return  响应对象
     */
    public TestStepResponseEntity uploadFunction(TestStepDataEntity tsDataEntity, CloseableHttpClient httpClient) {
        HttpPost httpPost = new HttpPost(tsDataEntity.getUrl());
        CloseableHttpResponse httpResponse = null;
        // 响应时间计算
        long startTime = 0L;
        long endTime = 0L;
        long responseTime = 0L;
        // 定义响应实体
        TestStepResponseEntity responseEntity = new TestStepResponseEntity();

        // 设置cookie
        Map<String, String> cookiesMap = tsDataEntity.getCookiesMap();
        String cookieContent = RequestTools.setRequestCookie(cookiesMap);
        httpPost.setHeader("Cookie", cookieContent);

        // 设置请求头
        Map<String, String> headersMap = tsDataEntity.getHeadersMap();
        if (headersMap != null) {
            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // 这里定义的列表,主要是考虑到后面可能会出现多个文件同时上传的情况,正常情况下,两个列表的长度一致.
        List<String> fileNameList = new ArrayList<>();
        List<String> filePathList = new ArrayList<>();

        List<Map<String, String>> postParams = tsDataEntity.getPostParamsList();
        for (Map<String, String> params: postParams) {
            for (Map.Entry<String, String> entry: params.entrySet()) {
                // 对参数进行文件路径的匹配.
                boolean isPath = Pattern.matches("^[a-zA-Z]:(((\\\\(?! )[^/:*?<>\\\"\"|\\\\]+)+\\\\?)|(\\\\)?)\\s*$", entry.getValue());
                if (isPath) {
                    fileNameList.add(entry.getKey());
                    filePathList.add(entry.getValue());
                }
            }
        }

        // 删除原来参数列表中上传文件的参数.只保留普通Key-Value参数
        Iterator<Map<String, String>> iter = postParams.iterator();
        while (iter.hasNext()) {
            for (Map.Entry<String, String> entry: iter.next().entrySet()) {
                for (String fileName : fileNameList) {
                    if (fileName.equals(entry.getKey())) {
                        iter.remove();
                    }
                }
            }
        }

        // 将上传文件的参数转为FileBody,以列表的形式存储.
        List<FileBody> fileBodyList = new ArrayList<>();
        for (String filePath: filePathList) {
            File file = new File(filePath);
            FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
            fileBodyList.add(fileBody);
        }

        // 将传递的参数转为StringBody,放置到List中,并发参数名也放到List中
        List<String> paramNameList = new ArrayList<>();
        List<StringBody> valueBodyList = new ArrayList<>();
        for (Map<String, String> param: postParams) {
            for (Map.Entry<String, String> entry: param.entrySet()) {
                paramNameList.add(entry.getKey());
                StringBody valueBody = new StringBody(entry.getValue(), ContentType.MULTIPART_FORM_DATA);
                valueBodyList.add(valueBody);
            }
        }

        // 创建MultipartEntityBuilder对象用于上传操作.
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // 设置浏览器兼容模式,避免文件乱码
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 加入上传文件的请求参数.
        for (int i = 0; i < fileNameList.size(); ++i) {
            builder.addPart(fileNameList.get(i), fileBodyList.get(i));
        }
        // 加入其它的请求参数
        for (int i = 0; i < paramNameList.size(); ++i) {
            builder.addPart(paramNameList.get(i), valueBodyList.get(i));
        }

        try {
            // 建立请求实体
            HttpEntity requestEntity = builder.build();
            httpPost.setEntity(requestEntity);
            // 发送请求并计算响应时间
            System.out.println("执行上传请求: " + httpPost.getURI());
            startTime = System.currentTimeMillis();
            httpResponse = httpClient.execute(httpPost);
            endTime = System.currentTimeMillis();
            responseTime = endTime - startTime;

            // 将请求步骤名称放置到响应实体中
            responseEntity.setHttpStepName(tsDataEntity.getStepName());
            // 将请求URL放置到响应实体中
            responseEntity.setHttpUrl(tsDataEntity.getUrl());
            // 将响应时间放置到响应实体中
            responseEntity.setHttpResponseTime("响应时间: " + responseTime + "毫秒");
            // 将响应状态码放置到响应实体中
            responseEntity.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
            // 将响应类型放置到响应实体中
            responseEntity.setHttpResponseContentType(httpResponse.getEntity().getContentType().getValue());
            // 将响应正文放置到响应实体中
            responseEntity.setHttpResponseContent(EntityUtils.toString(httpResponse.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("响应时间: " + responseTime + "毫秒");

        return responseEntity;
    }
}
