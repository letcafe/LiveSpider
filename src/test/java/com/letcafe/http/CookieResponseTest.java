package com.letcafe.http;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.parse.HuYaParser;
import com.letcafe.util.HttpUtils;
import com.letcafe.util.UrlFetcher;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CookieResponseTest {

    @Test
    public void getLoginInformation() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("username", "1656777876");
        params.put("pwdencrypt", "70ed0bd00d2c54e9cb87c0e21d8fef055bf89b28782d6bbe5f9e14278c867b44ad4750f162d4c140a81da3c1f963ac772783f8b834a3e6e2bd259b150fa06bac370136f15d45b6339ac38827b1fad5351c5a956de4025eac561c5580fb8d10d7e15e830234731651823c06c432c5525a0fbc8bef41d26504109f7c6bf6dd80dd");
        params.put("oauth_token", getOAuthToken());
        params.put("denyCallbackUR", "");
        params.put("UIStyle", "xelogin");
        params.put("appid", "5216");
        params.put("mxc", "");
        params.put("vk", "");
        params.put("isRemMe", "1");
        params.put("mmc", "");
        params.put("vv", "");
        params.put("hiido", "1");

        String url="https://lgn.yy.com/lgn/oauth/x2/s/login_asyn.do";
        HttpResponse response = HttpUtils.doPost(url, params);

        // get response code
        int StatusCode = response.getStatusLine().getStatusCode();
        Header[] headers = response.getAllHeaders();
        for(Header header : headers) {
            System.out.println("[H]: " + header);
        }
        if(StatusCode == 200){
            String entity = EntityUtils.toString (response.getEntity(),"utf-8");
            System.out.println(entity);
        }else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
    }

    // get huya oauth value
    public String getOAuthToken() throws IOException {
        String oAuthToken = "";
        String url="https://www.huya.com/udb_web/authorizeURL.php";
        Map<String, String> params = new HashMap<>();
        params.put("callbackURL", "https://www.huya.com/udb_web/udbport2.php?do=callback");
        params.put("do", "authorizeEmbedURL");
        HttpResponse response = HttpUtils.doPost(url, params);
        // get response code
        int StatusCode = response.getStatusLine().getStatusCode();
        if(StatusCode == 200){
            String entity = EntityUtils.toString (response.getEntity(),"utf-8");
            // remove first and last symbol(")
            entity = entity.substring(1, entity.length() - 1);
            // remove all symbol(\)
            entity = entity.replaceAll("\\\\", "");
            JSONObject oauthFullJson = new JSONObject(entity);
            oAuthToken = oauthFullJson.getString("ttoken");
            System.out.println("oAuthToken = " + oAuthToken);
        }else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
        return oAuthToken;
    }
}
