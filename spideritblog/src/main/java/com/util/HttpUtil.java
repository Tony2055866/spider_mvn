package com.util;

import com.hust.HustUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2014/12/28.
 */
public class HttpUtil {

    public static  String sendPost(String data,String url){
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);

        HttpClient client = new DefaultHttpClient(httpParams);

        // (可选)上下文信息，如果用到session信息的用。
        //HttpContext context = new BasicHttpContext();
        HttpPost post = new HttpPost(data);
        HustUtil.initPost(post, "application/json, text/javascript, */*; q=0.01", "application/x-www-form-urlencoded");
        try {
            HttpResponse response = client.execute(post);
            int stat = response.getStatusLine().getStatusCode();
            String json = "", line = null;
            System.out.println(stat);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            while ((line = reader.readLine()) != null)
                json += line + "\n";
//            System.out.println(json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
