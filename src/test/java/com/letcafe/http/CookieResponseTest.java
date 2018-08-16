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
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class CookieResponseTest {

    @Test
    public void getResCookies() throws IOException {
        HttpClient client = HttpClients.createDefault();
        String url="https://lgn.yy.com/lgn/oauth/x2/s/login_asyn.do?username=1656777876&pwdencrypt=70ed0bd00d2c54e9cb87c0e21d8fef055bf89b28782d6bbe5f9e14278c867b44ad4750f162d4c140a81da3c1f963ac772783f8b834a3e6e2bd259b150fa06bac370136f15d45b6339ac38827b1fad5351c5a956de4025eac561c5580fb8d10d7e15e830234731651823c06c432c5525a0fbc8bef41d26504109f7c6bf6dd80dd&oauth_token=f2b67f324974704968ca6e77286e384e42ed21c5bce2b71288513e1dbc543465c0b8e39353802e3332fd8573b1792221c863bbc471edea5b40e714a039e82795&denyCallbackURL=&UIStyle=xelogin&appid=5216&mxc=&vk=&isRemMe=1&mmc=&vv=&hiido=1";
        HttpResponse response = HttpUtils.postRawHtml(client, url);

        //获取响应状态码
        int StatusCode = response.getStatusLine().getStatusCode();
        Header[] headers = response.getAllHeaders();
        for(Header header : headers) {
            System.out.println(header);
        }
        System.out.println(StatusCode);
        if(StatusCode == 200){
            String entity = EntityUtils.toString (response.getEntity(),"utf-8");
            System.out.println("[Entity] = " + entity);
        }else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
    }
}
