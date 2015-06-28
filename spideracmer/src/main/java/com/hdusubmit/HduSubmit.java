package com.hdusubmit;

import com.main.Main;
import com.main.Util;
import com.model.WpPosts;
import com.sqider.Content;
import com.util.Init;
import com.util.MyUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HduSubmit {
	static Logger logger = LoggerFactory.getLogger(HduSubmit.class);


	static HttpClient client;
	static HttpContext context;
	public static int LOGINT = 1;
	private static HduSubmit hub = new HduSubmit();
	
	public String username;
	
	private HduSubmit(){
		username = "gaotong2055";
		try {
			if(Init.ojTestUser == 1)
				username = "gaotong2055";
			else
				username = "acmerblog";
			this.dologin(username, "19902055");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HduSubmit GetInstance(){
		return hub;
	}
	
	static boolean isLogin = false;
	public static void main(String[] args) throws Exception {
		//test();
		//if(true) return;
		Init.init();
		Main.ojtypebak = "hoj";
		HduSubmit hduSubmit = new HduSubmit();
		try {
			// 下登录
			
			// String code = MyUtil.getCodeFromLocal("hdu", "1004");
			for (int i = 1100; i <= 4822; i++) {
				String code = MyUtil.getCodeFromLocal("hdu", i + "");
				boolean res = false;
				if (code != null) {
					res = hduSubmit.doSubmit(i + "", code);
				}
				if(res) continue;
				// 没有本地代码，去网络搜索
				// WpPosts post = Main.getFinalPost(new String[]{"hdu",
				// "1004"});
				List<WpPosts> posts = Main.getFinalPosts(new String[] { "hdu",
						i + "" });
				for (WpPosts post : posts) {
					for (Content con : post.listContent) {
						if(con == null) continue;
						if (con.isCode) {
							String c = StringEscapeUtils
									.unescapeHtml4(con.text);
							res = hduSubmit.doSubmit(i + "", c, 2);
							if(res) break;
							Thread.sleep(2000);
						}
					}
					if(res) break;
				}
				logger.info(i + " 提交成功");
				//System.out.println(i + " 提交成功");
				Thread.sleep(2000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

	private static void test() throws Exception {
		//new HduSubmit().doSubmit("1101", "12fdsfsdfsdfsdf3fdsssssss12fdsfsdfsdfsdf3fdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
		
	}

	public void dologin(String name, String passwd) throws Exception {
		// TODO Auto-generated method stub
		client = new DefaultHttpClient();
		context = new BasicHttpContext();

		HttpPost post = new HttpPost(
				"http://acm.hdu.edu.cn/userloginex.php?action=login");
		//HttpParams httpparam = client.getParams();
		// param.setParameter("username", "gaotong2055");
		// param.setParameter("userpass", "19902055");
		// param.setParameter("login", "Sign In");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", name));
		params.add(new BasicNameValuePair("userpass", passwd));
		params.add(new BasicNameValuePair("login", "Sign In"));

		post
				.setHeader("Accept-Language",
						"zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		post
				.setHeader("accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post
				.setHeader(
						"user-agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		HttpResponse response = client.execute(post, context);
		int stat = response.getStatusLine().getStatusCode();
		//System.out.println(stat);
		EntityUtils.consume(response.getEntity());
		if (stat == 302) {
			logger.info("login success! ");
			//System.out.println("login success! ");
			isLogin = true;
			return;
		}
	}

	public boolean doSubmit(String problem, String code) throws Exception {
		try {
			Integer.parseInt(problem.trim());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		boolean res = false;
		if (code != null) {
			res = this.doSubmit(problem , code, 2);
		}
		if(!res){
			res = this.doSubmit(problem, code, 0);
		}
		if(!res)
			res = this.doSubmit(problem, code, 5);
		return res;
	}
	public boolean doSubmit(String problem, String code,int language) throws Exception {
		if(!isLogin){
			dologin(username, "19902055");
		}
		HttpPost post = new HttpPost(
				"http://acm.hdu.edu.cn/submit.php?action=submit");
		HttpParams httpparam = client.getParams();


		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("check", "0"));
		params.add(new BasicNameValuePair("problemid", problem));

		params.add(new BasicNameValuePair("language", "" + language));
		params.add(new BasicNameValuePair("usercode", code));
		post
				.setHeader("Accept-Language",
						"zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		post
				.setHeader("accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post
				.setHeader(
						"user-agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		post.setEntity(new UrlEncodedFormEntity(params, "gb2312"));

		HttpResponse response = client.execute(post, context);
		int stat = response.getStatusLine().getStatusCode();
		//System.out.println("after submit:" + Util.convertStreamToString(response.getEntity().getContent()));

		
		EntityUtils.consume(response.getEntity());
		
		
		if (stat == 302) {
			//System.out.println("submit success! ");
			logger.info("submit success!");
			Thread.sleep(2000);
			return getRes(problem);
		}
		return false;
	}

	public boolean getRes(String problem) throws Exception {
		if(!isLogin) dologin(username, "19902055");
		String total = "";
		while ((total == "") || total.contains("queuing")
				|| total.contains("compiling") || total.contains("running")) {
			total = "";
			Thread.sleep(1000);
			HttpGet get = new HttpGet(
					"http://acm.hdu.edu.cn/status.php?user="+username+"&pid="
							+ problem);
			HttpResponse subResponse = client.execute(get, context);
			HttpEntity entity = subResponse.getEntity();
			if (entity != null) {
				BufferedReader br = null;
				br = new BufferedReader(new InputStreamReader(entity
						.getContent()));
				String line = null;
				boolean flag = false;
				while ((line = br.readLine()) != null) {
					if (line.contains("fixed_table")) {
						total = line.toLowerCase();
						break;
					}
				}
			}
			EntityUtils.consume(subResponse.getEntity());
		}
		//System.out.println(total);
		if (total.contains("<font color=red>accepted</font>"))
			return true;

		return false;
	}

}
