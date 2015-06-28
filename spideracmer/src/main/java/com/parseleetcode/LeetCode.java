package com.parseleetcode;

import com.util.MyUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
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
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableRow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LeetCode {
	static HttpContext context;
	static DefaultHttpClient client;
	private String title = "";
	static HashMap<String, String> map = new HashMap<String, String>();
	
	Logger logger = Logger.getLogger(LeetCode.class);
	static{
		 BasicHttpParams httpParams = new BasicHttpParams();
		 HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		 HttpConnectionParams.setSoTimeout(httpParams, 10000);
		 client = new DefaultHttpClient(httpParams);
		 context = new BasicHttpContext();
		String html = MyUtil.getPage("https://oj.leetcode.com/accounts/login/", false, client, context).html;
		String csrfmiddlewaretoken = StringUtils.substringBetween(html, "csrfmiddlewaretoken' value='", "'");
		System.out.println(csrfmiddlewaretoken);
		try {
//			HttpResponse responseTest = client.execute(get, context);
//			EntityUtils.consume(responseTest.getEntity());
			HttpPost post = new HttpPost("https://oj.leetcode.com/accounts/login/");
			post.setHeader("Referer", "https://oj.leetcode.com/accounts/login/");
			
		    CookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie cookie = new BasicClientCookie("csrftoken", csrfmiddlewaretoken);
            cookie.setPath("/"); 
            cookie.setDomain("oj.leetcode.com"); 
            cookieStore.addCookie(cookie); 
            client.setCookieStore(cookieStore);

            
			 //构造最简单的字符串数据   
		     StringEntity reqEntity = new StringEntity("csrfmiddlewaretoken="+csrfmiddlewaretoken+"&login=gaotong2055%40163.com&password=19902055");
		     reqEntity.setContentType("application/x-www-form-urlencoded");   
		  // 设置请求的数据   
		     post.setEntity(reqEntity);   
		     // 执行   
		     HttpResponse response = client.execute(post, context);
		   
				 EntityUtils.consume(response.getEntity());
				 
				 String problems = MyUtil.getPage("https://oj.leetcode.com/problems/", false, client, context).html;
				 String table = StringUtils.substringBetween(problems, "<table id=\"problemList\"", "</table>");
				 List<LinkTag> links = MyUtil.parseTags(table, LinkTag.class, null, null);
				 for(LinkTag link:links){
					 
					 if(link.getLink().startsWith("/problems")){
						// System.out.println(link.getStringText().toString());
						 map.put(StringEscapeUtils.unescapeHtml4(link.getStringText().toString()), "https://oj.leetcode.com" + link.getLink().toString());
					 }
				 }
				 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String javaCode = null;
	public String pythonCode = null;
	private String cppCode = null;
	
	public void initCode(){
		if(javaCode == null && pythonCode == null){
			if(map.get(title) == null) return;
			String url = map.get(title) + "submissions/";
			logger.info("initCode : url = " + url );
			String text = MyUtil.getPage(url, false, client, context).html;
			//logger.info("initCode : text = " + text );
			String table = StringUtils.substringBetween(text, "<tbody>", "</tbody>");
			if(table == null || table.trim() == "") return; 
			List<TableRow> rows = MyUtil.parseTags(table, TableRow.class, null, null);
			for(TableRow row:rows){
				if(javaCode != null && pythonCode != null) break;
				if(javaCode == null &&  row.toPlainTextString().contains("java") && row.toHtml().contains("Accepted")){
					LinkTag link = MyUtil.parseTags(row.toHtml(), LinkTag.class, null, null).get(1);
					javaCode = getCode(link,"java");
				}else if(pythonCode == null &&  row.toPlainTextString().toLowerCase().contains("python") && row.toHtml().contains("Accepted")){
					LinkTag link = MyUtil.parseTags(row.toHtml(), LinkTag.class, null, null).get(1);
					javaCode = getCode(link,"python");
				}
			}
		}
	}
	
	private String getCode(LinkTag link,String lang) {
		String url = "https://oj.leetcode.com" + link.getLink();
		String text = MyUtil.getPage(url, false, client, context).html;
		String code = StringUtils.substringBetween(text, "'" + lang + "', '", "');");
		return StringEscapeUtils.escapeHtml4(MyUtil.unescape(code));
	}

	public LeetCode(String title) {
		// TODO Auto-generated constructor stub
		this.title = title;
	}
	
	public  String getDes(){
		if(map.get(title) == null) return "";
		String url = map.get(title);
		String text = MyUtil.getPage(url, false, client, context).html;
		List<Div> list = MyUtil.parseTags(text, Div.class, "class", "question-content");
		if(list.size() == 0) return null;
		Div div = list.get(0);
//		List<Span> spans = MyUtil.parseTags(text, Span.class, null, null);
//		Span span = spans.get(spans.size()-1);
//		List
		
		String tags = "";
		for(int i =0 ;i < div.getChildren().size(); i++){
			Node node = div.getChildren().elementAt(i);
			if(node instanceof Div && ((Div)node).getAttribute("class").startsWith("btn btn-xs btn-warning")){
				div.removeChild(i);
				//System.out.println(node.toHtml());
			}
			if(node instanceof Span && ((Span)node).getAttribute("class").equals("hide")){
				List<LinkTag> links = MyUtil.parseTags(node.toHtml(), LinkTag.class, null, null);
				for(LinkTag link:links){
					tags += "&nbsp;" + link.getStringText();
				}

				div.removeChild(i);
			}
		}
		return (div.toHtml() + "<br> 标签:" + tags);
		//all = StringUtils.substringBetween(all, "btn-warning\">", "</dib>")
		//return div.toHtml().replace("Show Tags", "标签：") + " \n "  + "题目链接：" + url + "<br>";
	}
	
	public static void main(String[] args) {
		/*LeetCode l = new LeetCode("Min Stack");
		String text = MyUtil.getPage("https://oj.leetcode.com/profile/", false, client, context).html;
		System.out.println(text);*/
		//System.out.println(map.get("Valid Palindrome"));
		
//		System.out.println(map.size());
		System.out.println(map.keySet());
		for(String key:map.keySet()){
			
		}
		/*LeetCode l = new LeetCode("Min Stack");
		l.initCode();
		
		System.out.println(l.javaCode);*/
	}
	
	
	
}
