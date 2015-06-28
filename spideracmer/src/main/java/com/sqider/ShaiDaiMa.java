package com.sqider;

import com.hdusubmit.HduSubmit;
import com.main.Util;
import com.model.WpPosts;
import com.model.WpTermTaxonomy;
import com.util.MyUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ShaiDaiMa extends Spider{
	
	
	
	static HttpContext context;
	static HttpClient client;
	
	public static void main(String[] args) {
		//login();
		//System.out.println(context + " " + client);
		//System.out.println ( getCode("http://shaidaima.com/source/view/4709") );
		
		ShaiDaiMa sdm = new ShaiDaiMa();
		String url = "http://shaidaima.com/source/view/3014";
		PageData pg = MyUtil.getPage(url, false);
		String searchKeys[] = new String[]{"hdu", "3511"};
		sdm.parseArticleSUrl(pg, searchKeys);
		
	}
	public static void login(){
		
		 BasicHttpParams httpParams = new BasicHttpParams();
		 HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		 HttpConnectionParams.setSoTimeout(httpParams, 10000);
		 client = new DefaultHttpClient(httpParams);
		 context = new BasicHttpContext();
		HttpGet get = new HttpGet("http://shaidaima.com");
		
		get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		get.setHeader("accept","application/json, text/javascript, */*; q=0.01");
		get.setHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		
			try {
				//HttpResponse responseTest = client.execute(get, context);
				//EntityUtils.consume(responseTest.getEntity());
				
				HttpPost post = new HttpPost("http://shaidaima.com/ajax/login");
				post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
				post.setHeader("accept","application/json, text/javascript, */*; q=0.01");
				post.setHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
				//构造最简单的字符串数据   
				 StringEntity reqEntity = new StringEntity("user_name=gaotong&password=19902055");
				 reqEntity.setContentType("application/x-www-form-urlencoded");   
				 	// 设置请求的数据   
				 post.setEntity(reqEntity);   
				 // 执行   
				 HttpResponse response = client.execute(post, context);
				 //EntityUtils.consume(response.getEntity());
				 //response = client.execute(get, context);
				InputStream in =  response.getEntity().getContent();
				String res = Util.convertStreamToString(in);
				//System.out.println(res);
				 EntityUtils.consume(response.getEntity());
				response = client.execute(get, context);
				 in =  response.getEntity().getContent();
				 res = Util.convertStreamToString(in);
				//System.out.println(res);
				 EntityUtils.consume(response.getEntity());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static String getCode(String url){
		PageData pd = MyUtil.getPage(url, client, context);
		if(pd.html.contains("请先登录晒代码才能看到这里的代码哦")){
			login();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//System.out.println("晒代码 getCode 失败");
		}
		 pd = MyUtil.getPage(url, client, context);
		 if(pd.html.contains("请先登录晒代码才能看到这里的代码哦")){
			 return null;
		 }
		List<PreTag> list = MyUtil.parseTags(pd.html, PreTag.class, "name", "code");
		if(list.size() > 0) return list.get(0).getStringText();
		
		return null;
	}
	@Override
	public WpPosts getArticleSUrl(PageData page) {
		
		return parseArticleSUrl(page,null);
	}
	
	public static boolean debug = true;
	
	@Override
	public WpPosts parseArticleSUrl(PageData page, String[] searchKeys) {
		 WpPosts post = new WpPosts();
		 post.host = page.host;
		try {
			System.out.println("Spier4Shaidaima 开始解析:" + page.url);
			Map<WpTermTaxonomy, Integer> keyCnt = new HashMap();
	
			post.power -= 100;
			//if(allString.contains("问题描述")) return null;
			//int power = checkTitle(title, searchKeys);
			int power = 0;
			//如果需要自动添加标签
			//匹配的 目录和标签集合
				//if( keyCnt.size() == 0 ) return null;
				List<Map.Entry<WpTermTaxonomy,Integer>> sort=new ArrayList();
				for(Iterator<Map.Entry<WpTermTaxonomy,Integer>>  it=sort.iterator(); it.hasNext(); ){
					power += it.next().getValue();
				}
				post.listkeyCnt = sort;
			
			
			StringBuffer sb = new StringBuffer();
			List<Content> listCon = new ArrayList<Content>();
			String code = getCode(page.url);
			if(code == null) return null;
			listCon.add(new Content( code ,true,"cpp" ));
			if(searchKeys != null && searchKeys.length == 2){
				String fcode = StringEscapeUtils.unescapeHtml4(code);
				if(HduSubmit.GetInstance().doSubmit(searchKeys[1], fcode)){
					power += 50;
				}
			}
			
			
			post.listContent = listCon;
			//继续找评论   过段时间再找
			//post.setWpCommentses(getCommets(post, page));
			power += checkCodePower(post);
			post.power += power;
			post.hasCode = true;
			post.url = page.url;
			System.out.println("解析成功！！！！");
			return post;
		} catch (Exception e) {
		
			e.printStackTrace();
			//System.out.println("parse Html:-----------------\n" + page.html);
			return null;
		}
	}
	
}
