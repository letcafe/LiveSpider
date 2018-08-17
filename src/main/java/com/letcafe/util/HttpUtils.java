package com.letcafe.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;

public abstract class HttpUtils {
    private static CookieStore cookieStore = new BasicCookieStore();

    public static HttpResponse getRawHtml(HttpClient client, String personalUrl) {
        //获取响应文件，即html，采用get方法获取响应数据
        HttpGet getMethod = new HttpGet(personalUrl);
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        try {
            //执行get方法
            response = client.execute(getMethod);
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            // getMethod.abort();
        }
        return response;
    }

    public static HttpResponse doPost(String url, Map<String, String> map) throws UnsupportedEncodingException {
        HttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(url);
        if (map != null) {
            List<NameValuePair> pairs = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
        }
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        List<Cookie> cookies = cookieStore.getCookies();
        System.out.println("cookie length = " + cookies.size());
        for (Cookie cookie : cookies) {
            System.out.println("[Cookie] : " + cookie);
        }
        try {
            // post
            response = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
