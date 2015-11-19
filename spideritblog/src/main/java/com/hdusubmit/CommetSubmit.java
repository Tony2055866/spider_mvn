package com.hdusubmit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.hust.HustUtil;
import com.itblog.sqider.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommetSubmit {
	private static Logger logger = LoggerFactory.getLogger(CommetSubmit.class);

	public static void main(String[] args) {
		/*String data="short_name=51itong&sercret=d6d143d5c83dfde9d7ccd726639ec462" +
				"&posts[0][post_key]=19&posts[0][thread_key]=200&posts[0][message]=nihao,womenyoujianmianle";
		data = "http://api.duoshuo.com/posts/import.json?" + data;*/
		String data="short_name=51itong&sercret=d6d143d5c83dfde9d7ccd726639ec462" +
		"&users[0][user_key]=100&users[0][name]=abcdefg";
data = "http://api.duoshuo.com/users/import.json?" + URLEncoder.encode(data);


		sendPost(data);
	}
	
	public static void submit(String title, String text) {
		// http://acm.hdu.edu.cn/discuss/public/post/new.php?action=post
		// Status Code:302 Moved Temporarily
		// post_title
		// post_content
//		List<Posts> posts = new ArrayList<Posts>();
//		Map<String, String> data = new HashMap<String, String>();
//		data.put("short_name", "apitest");
		//data.put("users", posts);

	}

	public static List<Comment> getComments() {

		return null;
	}
	
	public static  void sendPost(String data){
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			while ((line = reader.readLine()) != null)
				json += line + "\n";
			logger.info(json);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
	}

	private String httpBuildQuery(Map<String, String> data)
			throws UnsupportedEncodingException {
		QueryStringBuilder builder = new QueryStringBuilder();
		for (Entry<String, String> pair : data.entrySet()) {
			builder.addQueryParameter(pair.getKey(), pair.getValue());
		}
		return builder.encode("UTF-8");
	}
}
