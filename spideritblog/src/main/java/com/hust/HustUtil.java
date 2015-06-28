package com.hust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Node;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.itblog.sqider.PageData;
import com.itblog.sqider.PreTag;
import com.itblog.sqider.ProblemData;
import com.util.MyUtil;

public class HustUtil {

	
	public static void main(String[] args) {
		// getProblemData (returnHustId("ZOJ", "1123"));
//		getProblemData("19438");
//		String test = "hello \\\" \\r\\n world";
//		System.out.println(test);
		
		//System.out.println(getAcCode("HDU", "2777"));
		List<String[]> allKeys = getAllProblems("Jack of All Trades",new String[]{"HDU","2777"});
//		
		if(allKeys != null){
			for(String[] keys:allKeys){
				System.out.println(keys[0] + " " + keys[1]);
			}
		}
		
	}
	
	public static List<String[]> getAllProblems(String title,String[] keys){
		if (title == null)
			return null;
		title=title.trim();
		// cient参数，可选
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);

		HttpClient client = new DefaultHttpClient(httpParams);

		// (可选)上下文信息，如果用到session信息的用。
		HttpContext context = new BasicHttpContext();
		
		HttpPost post = new HttpPost("http://acm.hust.edu.cn/vjudge/problem/listProblem.action?"+
				"sEcho=3&iColumns=8&sColumns=&iDisplayStart=0&iDisplayLength=25&mDataProp_0=0&mDataProp_1=1&mDataProp_2=2&mDataProp_3=3&mDataProp_4=4&mDataProp_5=5&mDataProp_6=6&mDataProp_7=7&sSearch=" +
				URLEncoder.encode(title) +
				"&bRegex=false&sSearch_0=&bRegex_0=false&bSearchable_0=true&sSearch_1=&bRegex_1=false&bSearchable_1=true&sSearch_2=&bRegex_2=false&bSearchable_2=true&sSearch_3=&bRegex_3=false&bSearchable_3=true&sSearch_4=&bRegex_4=false&bSearchable_4=true&sSearch_5=&bRegex_5=false&bSearchable_5=true&sSearch_6=&bRegex_6=false&bSearchable_6=true&sSearch_7=&bRegex_7=false&bSearchable_7=true&iSortCol_0=1&sSortDir_0=asc&iSortingCols=1&bSortable_0=false&bSortable_1=true&bSortable_2=true&bSortable_3=false&bSortable_4=true&bSortable_5=true&bSortable_6=true&bSortable_7=true&OJId=All");
		initPost(post, "application/json, text/javascript, */*; q=0.01", "application/x-www-form-urlencoded");
		try {

			HttpResponse response = client.execute(post, context);
			int stat = response.getStatusLine().getStatusCode();
			String json = "", line = null;
			System.out.println(stat);
			if (stat == 200) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			while ((line = reader.readLine()) != null)
				json += line + "\n";
			 }else{
				 return null;
			 }
			List<String[]> ansList = new ArrayList<String[]>();
			// System.out.println(json);
			Gson gson = new Gson();
			Hust hustData = gson.fromJson(json, Hust.class);

			String realId = returnHustId(keys[0], keys[1]);
			String desOrigin = getDes5(realId);
			// System.out.println(hustData.getAaData().size());
			for (Object obj : hustData.getAaData()) {
				List list = (List) obj;
				if (list.get(2).toString().trim().equals(title)){
					//System.out.println("list.get(2).toString():" + list.get(2).toString());
					
					//realId = ((Double) list.get(5)).intValue() + "";
					String ojId = list.get(1).toString();
					String oj = list.get(0).toString();
					String curDes = getDes5(realId);
					System.out.println(curDes);
					if(title.split(" ").length == 1){
						if(desOrigin.equals(curDes)){
							String[] ttkeys= new String[]{oj,ojId};
						     ansList.add(ttkeys);
						}
					}else{
						String[] ttkeys= new String[]{oj,ojId};
						ansList.add(ttkeys);
					}
				
				}
			}
			//System.out.println("Hust的Id:" + realId);
			EntityUtils.consume(response.getEntity());
			return ansList;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static String getDes5(String realId){
		ProblemData pm = getProblemData(realId);
		//System.out.println(pm.text);
		String parseHtml = "<div class=mydivParse>" + pm.text + "</div>";
		Div div = MyUtil.parseTags(parseHtml, Div.class, "class", "mydivParse").get(0);
		String ans = div.toPlainTextString().trim().substring(0,20);
		if(ans.startsWith("h1,h2,h3,h4")){
			List<ParagraphTag> ps =  MyUtil.parseTags(parseHtml, ParagraphTag.class, null, null);
			if(ps != null && ps.size() > 0){
				for(ParagraphTag pp:ps){
					String des = pp.toPlainTextString().trim();
					if(des.length() > 30)
						return des.substring(0,20);
				}
			}
		}
		return div.toPlainTextString().trim().substring(0,20);
	}
	
	public static String getOjType(String oj){
		oj = oj.toUpperCase().trim();
		if(oj.equals("HDU") || oj.equals("HOJ")) return "HDU";
		else if(oj.equals("POJ") || oj.equals("PKU")) return "POJ";
		else if(oj.equals("UVALIVE")) return "UVALive";
		else if(oj.equals("UVALIVE")) return "UVALive";
		else if(oj.equals("CODEFORCES")) return "CodeForces";
		return oj;
	}

	
	public static ProblemData getProblemData(String id) {
		if (id == null)
			return null;
		PageData pd = MyUtil
				.getPage("http://acm.hust.edu.cn/vjudge/problem/viewProblem.action?id="
						+ id);
		if (pd != null) {
			Div titleDiv = MyUtil.parseTags(pd.html, Div.class, "class", "ptt")
					.get(0);
			String title = null;
			for (int i = 0; i < titleDiv.getChildren().size(); i++) {
				Node node = titleDiv.getChild(i);
				if (node instanceof LinkTag)
					title = ((LinkTag) node).getLinkText();
			}
			if (title == null)
				return null;
			//System.out.println(title);
			String resJson = getProDesJson(id);
			String proText = StringUtils.substringBetween(resJson, "s0.description=\"", "\";s0.hint");

			if(proText.length() > 10){
				ProblemData problem = new ProblemData();
				problem.text = StringEscapeUtils.unescapeJava(proText);
				problem.title = title;
				return problem;
			}
		}
		return null;
	}
	
	public static String getAcCode(String oj,String problem){
System.out.println("get HUST getAcCode: " + oj + " " + problem);
		oj = oj.toUpperCase();
		HttpClient client = new DefaultHttpClient();
		String secho = getSEcho(oj);
		if(secho == null) return null;
		HttpPost post = new HttpPost(
				"http://acm.hust.edu.cn/vjudge/problem/fetchStatus.action?sEcho=" +
				secho +
				"&iColumns=15&sColumns=&iDisplayStart=0&iDisplayLength=20&mDataProp_0=0&mDataProp_1=1&mDataProp_2=2&mDataProp_3=3&mDataProp_4=4&mDataProp_5=5&mDataProp_6=6&mDataProp_7=7&mDataProp_8=8&mDataProp_9=9&mDataProp_10=10&mDataProp_11=11&mDataProp_12=12&mDataProp_13=13&mDataProp_14=14&un=&OJId=" +
				getOjType(oj)+ "&probNum=" +
				 problem +"&res=1");
		initPost(post, "application/json, text/javascript, */*; q=0.01", "application/x-www-form-urlencoded");
		try {
			HttpResponse response = client.execute(post);
			int stat = response.getStatusLine().getStatusCode();
			String json = "", line = null;
			//System.out.println(stat);
			if (stat == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				while ((line = reader.readLine()) != null)
					json += line + "\n";
			 }else{
				 return null;
			 }
System.out.println(json);
			Gson gson = new Gson();
			Hust hustData = gson.fromJson(json, Hust.class);
			String realId = null;
			// System.out.println(hustData.getAaData().size());
			if(hustData == null || hustData.getAaData() == null || hustData.getAaData().size() == 0) return null;
			for (Object obj : hustData.getAaData()) {
				List list = (List) obj;
//				for(Object o:list) System.out.println(o);
//				System.out.println(list.size());
				if (list.get(10).toString().equals("2.0"))
				{
					return getCodebyRunId( ((Double)list.get(0)).intValue() + "");
				}
			}
			
			EntityUtils.consume(response.getEntity());
		
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		return null;
	}

	public static String returnHustId(String oj, String problem) {

		// cient参数，可选
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);

		HttpClient client = new DefaultHttpClient(httpParams);

		// (可选)上下文信息，如果用到session信息的用。
		HttpContext context = new BasicHttpContext();

		HttpPost post = new HttpPost(
				"http://acm.hust.edu.cn/vjudge/problem/listProblem.action?sEcho=16&iColumns=8&sColumns=&iDisplayStart=0&iDisplayLength=25&mDataProp_0=0&mDataProp_1=1&mDataProp_2=2&mDataProp_3=3&mDataProp_4=4&mDataProp_5=5&mDataProp_6=6&mDataProp_7=7"
						+ "&sSearch="
						+ problem
						+ "&bRegex=false&sSearch_0=&bRegex_0=false&bSearchable_0=true&sSearch_1=&bRegex_1=false&bSearchable_1=true&sSearch_2=&bRegex_2=false&bSearchable_2=true&sSearch_3=&bRegex_3=false&bSearchable_3=true&sSearch_4=&bRegex_4=false&bSearchable_4=true&sSearch_5=&bRegex_5=false&bSearchable_5=true&sSearch_6=&bRegex_6=false&bSearchable_6=true&sSearch_7=&bRegex_7=false&bSearchable_7=true&iSortCol_0=1&sSortDir_0=asc&iSortingCols=1&bSortable_0=false&bSortable_1=true&bSortable_2=true&bSortable_3=false"
						+ "&bSortable_4=true&bSortable_5=true&bSortable_6=true&bSortable_7=true&OJId="
						+ oj);
		initPost(post, "application/json, text/javascript, */*; q=0.01", "application/x-www-form-urlencoded");

		try {

			HttpResponse response = client.execute(post, context);

			int stat = response.getStatusLine().getStatusCode();

			String json = "", line = null;
			System.out.println(stat);
			if (stat == 200) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			while ((line = reader.readLine()) != null)
				json += line + "\n";
			 }else{
				 return null;
			 }
			// System.out.println(json);
			Gson gson = new Gson();
			Hust hustData = gson.fromJson(json, Hust.class);

			String realId = null;
			// System.out.println(hustData.getAaData().size());
			for (Object obj : hustData.getAaData()) {
				List list = (List) obj;
				if (list.get(1).toString().equals(problem))
					realId = ((Double) list.get(5)).intValue() + "";
			}

			System.out.println("Hust的Id:" + realId);
			EntityUtils.consume(response.getEntity());
			return realId;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static void initPost(HttpPost post,String accepet, String type) {
		post
				.setHeader("Accept-Language",
						"zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		post.setHeader("accept",
				accepet);
		post
				.setHeader(
						"user-agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		post.setHeader("Content-Type", type);
		;
	}

	static String getProDesJson(String id) {
		HttpClient client = new DefaultHttpClient();
		// (可选)上下文信息，如果用到session信息的用。
		HttpContext context = new BasicHttpContext();
		String data = "callCount=1&page=/vjudge/problem/viewProblem.action?id=19417A&"
				+ "httpSessionId=&scriptSessionId=BFADD655A107EB49B4228F916E0C4AF315&"
				+ "c0-scriptName=judgeService&c0-methodName=fetchDescriptions"
				+ "&c0-id=0&c0-param0=string:" + id + "&batchId=0";
		HttpPost post = new HttpPost(
				"http://acm.hust.edu.cn/vjudge/dwr/call/plaincall/judgeService.fetchDescriptions.dwr");
		initPost(post, "*/*", "text/plain");
		data = data.replaceAll("\\&", "\n");
		//System.out.println(data);
		try {
			post.setEntity(new StringEntity(data));

			HttpResponse response = client.execute(post);
			System.out.println(response.getStatusLine().getStatusCode());
			String json = "", line = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			while ((line = reader.readLine()) != null)
				json += line + "\n";
			
			
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String getCodebyRunId(String id) {
		PageData pd = MyUtil.getPage("http://acm.hust.edu.cn/vjudge/problem/viewSource.action?id=" + id);
		if(pd != null){
			List<PreTag> pres = MyUtil.parseTags(pd.html, PreTag.class, "class", null);
			if(pres != null && pres.size() > 0){
				PreTag pre = pres.get(0);
				return pre.getStringText();
			}
		}
		               
		return null;
	}
	public static String getSEcho(String oj){
		oj = oj.toUpperCase().trim();
		if(oj.equals("POJ")) return "6";
		if(oj.equals("HDU")) return "7";
		if(oj.equals("UVALIVE")) return "5";
		if(oj.equals("ZOJ")) return "8";
		if(oj.equals("SGU")) return "10";
		if(oj.equals("UVA")) return "11";
		if(oj.equals("CODEFORCES")) return "12";
		if(oj.equals("URAL")) return "14";
		if(oj.equals("HUST")) return "15";
		if(oj.equals("NBUT")) return "16";
		if(oj.equals("FZU")) return "17";
		if(oj.equals("SPOJ")) return "18";
		if(oj.equals("CSU")) return "19";
		return null;
	}

	class Hust {
		private List aaData;
		private Number iTotalDisplayRecords;
		private Number iTotalRecords;
		private String sColumns;

		public List getAaData() {
			return this.aaData;
		}

		public void setAaData(List aaData) {
			this.aaData = aaData;
		}

		public Number getITotalDisplayRecords() {
			return this.iTotalDisplayRecords;
		}

		public void setITotalDisplayRecords(Number iTotalDisplayRecords) {
			this.iTotalDisplayRecords = iTotalDisplayRecords;
		}

		public Number getITotalRecords() {
			return this.iTotalRecords;
		}

		public void setITotalRecords(Number iTotalRecords) {
			this.iTotalRecords = iTotalRecords;
		}

		public String getSColumns() {
			return this.sColumns;
		}

		public void setSColumns(String sColumns) {
			this.sColumns = sColumns;
		}
	}

}
