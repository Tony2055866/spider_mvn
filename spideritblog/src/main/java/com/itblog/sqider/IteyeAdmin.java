package com.itblog.sqider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.htmlparser.tags.InputTag;

import com.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IteyeAdmin {
	private static Logger logger = LoggerFactory.getLogger(IteyeAdmin.class);

	HttpContext context;
	 HttpClient client;
	 String keyToken;
	 String nickname;
	  //CookieStore cookieStore = null;  
	 Header[] cookie;
	 public boolean isForbiden = false;
	
	public  void setNickname(String name){
		if(name.contains("840601860")) nickname = "acmerblog";
		else if(name.contains("gaotong")) nickname = "gaotong1991";
	}
	
	public  boolean check(String html){
		if(html.contains("您所在的IP地址对ITeye网站访问过于频繁"))
			isForbiden = true;
		else
			isForbiden = false;
		return isForbiden;
	}
	
	public  boolean login(String name, String password){
		
		setNickname(name);
		
		 BasicHttpParams httpParams = new BasicHttpParams();  
		 HttpConnectionParams.setConnectionTimeout(httpParams, 10000);  
		 HttpConnectionParams.setSoTimeout(httpParams, 10000);  
		 client = new DefaultHttpClient(httpParams);
		 context = new BasicHttpContext();
		 
		String html = MyUtil.getPage("http://www.iteye.com/login",false, client, context, new String[]{
				"Referer","http://"+nickname+".iteye.com/",
				"user-agent","Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)"
		}).html;
		if(check(html)) return false;
		
		//if(html.contains("Redirect")) return true;
		
		List<InputTag> list = MyUtil.parseTags(html, InputTag.class, "name", "authenticity_token");
		String key = null;
		if(list.size()  > 0){
			key = list.get(0).getAttribute("value");
			logger.info("key : " + key);
			keyToken = key;
		}
		key.toString();
		
		try {
			Thread.sleep(3000);
				//HttpResponse responseTest = client.execute(get, context);
				//EntityUtils.consume(responseTest.getEntity());
				HttpPost post = new HttpPost("http://www.iteye.com/login");
				 //构造最简单的字符串数据   
			     StringEntity reqEntity = new StringEntity("authenticity_token=" +
			    		 URLEncoder.encode(key) +
			     		"&name=" +
			     		URLEncoder.encode(name) +
			     		"&password=" +
			     		URLEncoder.encode(password) +
			     		"&button=%E7%99%BB%E3%80%80%E5%BD%95");   
			     reqEntity.setContentType("application/x-www-form-urlencoded");   
			    //设置请求的数据   
			     
			     post.setEntity(reqEntity);
			     post.setHeader("user-agent", "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
			     post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
			     post.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			     // 执行   
			     HttpResponse response = client.execute(post, context);
			     
			     String total = MyUtil.getStringFromResponse(response);
			     if(check(total)) return false;
			     
			     logger.info("---------after login -----");
				logger.info(total);
				cookie = response.getHeaders("Set-Cookie");
				for(int i=0; i<cookie.length; i++){
					logger.info("cokie :" + cookie[i]);
				}
					 EntityUtils.consume(response.getEntity());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
	}
	
	

	public  void submitArticle(String title,String content,boolean isUp){
		if(nickname == null){
			return;
		}
		/*String html = MyUtil.getPage("http://"+ nickname +".iteye.com/admin/blogs/new",false, client, context, new String[]{
				"Referer","http://"+ nickname +".iteye.com/"
		}).html;
		if(check(html)) return;
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
			
		HttpPost post = new HttpPost("http://"+ nickname +".iteye.com/admin/blogs");
		 //构造最简单的字符串数据   
	     StringEntity reqEntity;
	     logger.info("keyToken :" + keyToken);
		try {
			reqEntity = new StringEntity("authenticity_token=" +
					 URLEncoder.encode(keyToken) +
			 		"blog%5Bblog_type%5D=0&blog%5Bblog_type%5D=1&blog%5Bwhole_category_id%5D=5&blog%5Btitle%5D=" +
			 		URLEncoder.encode(title) +
			 		"&blog%5Bcategory_list%5D=Java&" +
			 		"auto_save_id=" +
			 		"&blog%5Bbbcode%5D=false&blog%5Bbody%5D=" +
			 		URLEncoder.encode(content) +
			 		"&blog%5Btag_list%5D=&blog%5Bdiggable%5D=0&topic%5Bforum_id%5D=&commit=%E5%8F%91%E5%B8%83");
			post.setHeader("user-agent", "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
		     post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		     post.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");		
			 reqEntity.setContentType("application/x-www-form-urlencoded");
			 post.setEntity(reqEntity);
			 
			 HttpResponse response = client.execute(post, context);
			 String total = MyUtil.getStringFromResponse(response);
			 logger.info("---------after submitArticle -----");
				logger.info(total);
				if(check(total)) return ;
				 EntityUtils.consume(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}   
	}

	
	
	public static void main(String[] args) {
		IteyeAdmin admin = new IteyeAdmin();
		
		admin.login("840601860@qq.com","QWER1234");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		admin.submitArticle("Hello world java Test1", "These is the content, hahah " , true);
		try {
			Thread.sleep(300 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//admin.submitArticle("Hello world java Test2", "These is the content, hahah 222222" , true);
	}

}
