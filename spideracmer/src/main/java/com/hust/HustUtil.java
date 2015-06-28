package com.hust;

import com.google.gson.Gson;
import com.sqider.PageData;
import com.sqider.PreTag;
import com.sqider.ProblemData;
import com.util.MyUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Node;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HustUtil {

	static Logger logger = LoggerFactory.getLogger(HustUtil.class);
	public static void main(String[] args) {
		// getProblemData (returnHustId("ZOJ", "1123"));
		//getProblemData("19438");
//		String test = "hello \\\" \\r\\n world";
//		System.out.println(test);
		
		//System.out.println(getAcCode("HDU", "2773"));
		/*List<String[]> allKeys = getAllProblems("Jack of All Trades",new String[]{"HDU","2777"});
//		
		if(allKeys != null){
			for(String[] keys:allKeys){
				System.out.println(keys[0] + " " + keys[1]);
			}
		}*/
		//returnHustIdNew("hdu", "Math Magic");
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
		
		HttpPost post = new HttpPost("http://acm.hust.edu.cn/vjudge/problem/toListProblem.action?"+
				"sEcho=3&iColumns=8&sColumns=&iDisplayStart=0&iDisplayLength=25&mDataProp_0=0&mDataProp_1=1&mDataProp_2=2&mDataProp_3=3&mDataProp_4=4&mDataProp_5=5&mDataProp_6=6&mDataProp_7=7&title=" +
				URLEncoder.encode(title) +
				"&bRegex=false&sSearch_0=&bRegex_0=false&bSearchable_0=true&sSearch_1=&bRegex_1=false&bSearchable_1=true&sSearch_2=&bRegex_2=false&bSearchable_2=true&sSearch_3=&bRegex_3=false&bSearchable_3=true&sSearch_4=&bRegex_4=false&bSearchable_4=true&sSearch_5=&bRegex_5=false&bSearchable_5=true&sSearch_6=&bRegex_6=false&bSearchable_6=true&sSearch_7=&bRegex_7=false&bSearchable_7=true&iSortCol_0=1&sSortDir_0=asc&iSortingCols=1&bSortable_0=false&bSortable_1=true&bSortable_2=true&bSortable_3=false&bSortable_4=true&bSortable_5=true&bSortable_6=true&bSortable_7=true&OJId=All");

		initPost(post, "application/json, text/javascript, */*; q=0.01", "application/x-www-form-urlencoded");
		try {

			HttpResponse response = client.execute(post, context);
			int stat = response.getStatusLine().getStatusCode();
			String json = "", line = null;
			logger.info("stat:" + stat);
//			System.out.println(stat);
			if (stat == 200) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			while ((line = reader.readLine()) != null)
				json += line + "\n";
			 }else{
				 return null;
			 }
			List<String[]> ansList = new ArrayList<String[]>();
			logger.info("json:" +json);
			///System.out.println("json:" + json);
			Gson gson = new Gson();
			Hust hustData = gson.fromJson(json, Hust.class);

			String realId = returnHustIdNew(keys[0], keys[1]);
			String desOrigin = getDes5(realId);
			// System.out.println(hustData.getAaData().size());
			for (String[] datas: hustData.data) {
				if (datas[2].trim().equals(title)){
					//System.out.println("list.get(2).toString():" + list.get(2).toString());
					
					//realId = ((Double) list.get(5)).intValue() + "";
					String ojId =datas[3];
					String oj = datas[0];
					String curDes = getDes5(realId);
					//System.out.println(curDes);
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
		logger.debug("get HUST getAcCode: " + oj + " " + problem);
//System.out.println("get HUST getAcCode: " + oj + " " + problem);
		oj = oj.toUpperCase();
		HttpClient client = new DefaultHttpClient();
		String secho = getSEcho(oj);
		if(secho == null) return null;
		HttpPost post = new HttpPost(
				"http://acm.hust.edu.cn/vjudge/problem/fetchStatus.action?" +
				"draw=5&columns%5B0%5D%5Bdata%5D=0&columns%5B0%5D%5Bname%5D=&columns%5B0%5D%5Bsearchable%5D=true&columns%5B0%5D%5Borderable%5D=false&columns%5B0%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B0%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B1%5D%5Bdata%5D=1&columns%5B1%5D%5Bname%5D=&columns%5B1%5D%5Bsearchable%5D=true&columns%5B1%5D%5Borderable%5D=false&columns%5B1%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B1%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B2%5D%5Bdata%5D=2&columns%5B2%5D%5Bname%5D=&columns%5B2%5D%5Bsearchable%5D=true&columns%5B2%5D%5Borderable%5D=false&columns%5B2%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B2%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B3%5D%5Bdata%5D=3&columns%5B3%5D%5Bname%5D=&columns%5B3%5D%5Bsearchable%5D=true&columns%5B3%5D%5Borderable%5D=false&columns%5B3%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B3%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B4%5D%5Bdata%5D=4&columns%5B4%5D%5Bname%5D=&columns%5B4%5D%5Bsearchable%5D=true&columns%5B4%5D%5Borderable%5D=false&columns%5B4%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B4%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B5%5D%5Bdata%5D=5&columns%5B5%5D%5Bname%5D=&columns%5B5%5D%5Bsearchable%5D=true&columns%5B5%5D%5Borderable%5D=false&columns%5B5%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B5%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B6%5D%5Bdata%5D=6&columns%5B6%5D%5Bname%5D=&columns%5B6%5D%5Bsearchable%5D=true&columns%5B6%5D%5Borderable%5D=false&columns%5B6%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B6%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B7%5D%5Bdata%5D=7&columns%5B7%5D%5Bname%5D=&columns%5B7%5D%5Bsearchable%5D=true&columns%5B7%5D%5Borderable%5D=false&columns%5B7%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B7%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B8%5D%5Bdata%5D=8&columns%5B8%5D%5Bname%5D=&columns%5B8%5D%5Bsearchable%5D=true&columns%5B8%5D%5Borderable%5D=false&columns%5B8%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B8%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B9%5D%5Bdata%5D=9&columns%5B9%5D%5Bname%5D=&columns%5B9%5D%5Bsearchable%5D=true&columns%5B9%5D%5Borderable%5D=false&columns%5B9%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B9%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B10%5D%5Bdata%5D=10&columns%5B10%5D%5Bname%5D=&columns%5B10%5D%5Bsearchable%5D=true&columns%5B10%5D%5Borderable%5D=false&columns%5B10%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B10%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B11%5D%5Bdata%5D=11&columns%5B11%5D%5Bname%5D=&columns%5B11%5D%5Bsearchable%5D=true&columns%5B11%5D%5Borderable%5D=false&columns%5B11%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B11%5D%5Bsearch%5D%5Bregex%5D=false&order%5B0%5D%5Bcolumn%5D=0&order%5B0%5D%5Bdir%5D=desc&start=0&length=20&search%5Bvalue%5D=&search%5Bregex%5D=false&un=&res=1&language=&orderBy=run_id"+
				"&probNum=" +
				problem+
				"&OJId=" + oj.toUpperCase());
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
			//[2197082,"ShineCheng",29813,"Accepted",244,0,"G++",3670,1398605868000,28510,2,"HDU","2773",null,0,0,0],
			//[1068344,"orzywmorz",29813,"Accepted",280,0,"C++",1958,1365513982000,22564,0,"HDU","2773",21853,0,0,0]
//System.out.println("getAcCode:" + json);
			EntityUtils.consume(response.getEntity());
			Gson gson = new Gson();
			FetchResult result = gson.fromJson(json, FetchResult.class);
			String realId = null;
			// System.out.println(hustData.getAaData().size());
			if(result == null || result.data== null || result.data.length == 0) return null;
			for (String[] data : result.data) {

				if (data[10].equals("2"))
				{
					return getCodebyRunId( data[0] );
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		return null;
	}
	
	public static String returnHustIdNew(String oj,String problem){
		// cient参数，可选
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);

		HttpClient client = new DefaultHttpClient(httpParams);

		// (可选)上下文信息，如果用到session信息的用。
		HttpContext context = new BasicHttpContext();
		HttpPost post = new HttpPost("http://acm.hust.edu.cn/vjudge/problem/listProblem.action?"+
				"draw=1&columns%5B0%5D%5Bdata%5D=0&columns%5B0%5D%5Bname%5D=&columns%5B0%5D%5Bsearchable%5D=false&columns%5B0%5D%5Borderable%5D=false&columns%5B0%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B0%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B1%5D%5Bdata%5D=1&columns%5B1%5D%5Bname%5D=&columns%5B1%5D%5Bsearchable%5D=true&columns%5B1%5D%5Borderable%5D=true&columns%5B1%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B1%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B2%5D%5Bdata%5D=2&columns%5B2%5D%5Bname%5D=&columns%5B2%5D%5Bsearchable%5D=true&columns%5B2%5D%5Borderable%5D=true&columns%5B2%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B2%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B3%5D%5Bdata%5D=3&columns%5B3%5D%5Bname%5D=&columns%5B3%5D%5Bsearchable%5D=true&columns%5B3%5D%5Borderable%5D=true&columns%5B3%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B3%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B4%5D%5Bdata%5D=4&columns%5B4%5D%5Bname%5D=&columns%5B4%5D%5Bsearchable%5D=true&columns%5B4%5D%5Borderable%5D=true&columns%5B4%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B4%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B5%5D%5Bdata%5D=5&columns%5B5%5D%5Bname%5D=&columns%5B5%5D%5Bsearchable%5D=true&columns%5B5%5D%5Borderable%5D=true&columns%5B5%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B5%5D%5Bsearch%5D%5Bregex%5D=false&order%5B0%5D%5Bcolumn%5D=3&order%5B0%5D%5Bdir%5D=desc&start=0&length=20&search%5Bvalue%5D=&search%5Bregex%5D=false&" +
				"OJId=" +
				oj.toUpperCase() +
				"&probNum=&title=" +
				URLEncoder.encode(problem) +
				"&source=");
		initPost(post, "application/json, text/javascript, */*; q=0.01", "application/x-www-form-urlencoded");

		try {

			HttpResponse response = client.execute(post, context);

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
			//System.out.println(json);
			Gson gson = new Gson();
			Hust hustData = gson.fromJson(json, Hust.class);

			String realId = null;
			// System.out.println(hustData.getAaData().size());
			for (String[] datas : hustData.data) {
				if (datas[2].equals(problem))
					realId = datas[datas.length-3];
			}

			//System.out.println("Hust的Id:" + realId);
			EntityUtils.consume(response.getEntity());
			return realId;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

//	public static String returnHustId(String oj, String problem) {
//
//		// cient参数，可选
//		BasicHttpParams httpParams = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
//		HttpConnectionParams.setSoTimeout(httpParams, 10000);
//
//		HttpClient client = new DefaultHttpClient(httpParams);
//
//		// (可选)上下文信息，如果用到session信息的用。
//		HttpContext context = new BasicHttpContext();
//
//		HttpPost post = new HttpPost(
//				"http://acm.hust.edu.cn/vjudge/problem/toListProblem.action?sEcho=16&iColumns=8&sColumns=&iDisplayStart=0&iDisplayLength=25&mDataProp_0=0&mDataProp_1=1&mDataProp_2=2&mDataProp_3=3&mDataProp_4=4&mDataProp_5=5&mDataProp_6=6&mDataProp_7=7"
//						+ "&sSearch="
//						+ problem
//						+ "&bRegex=false&sSearch_0=&bRegex_0=false&bSearchable_0=true&sSearch_1=&bRegex_1=false&bSearchable_1=true&sSearch_2=&bRegex_2=false&bSearchable_2=true&sSearch_3=&bRegex_3=false&bSearchable_3=true&sSearch_4=&bRegex_4=false&bSearchable_4=true&sSearch_5=&bRegex_5=false&bSearchable_5=true&sSearch_6=&bRegex_6=false&bSearchable_6=true&sSearch_7=&bRegex_7=false&bSearchable_7=true&iSortCol_0=1&sSortDir_0=asc&iSortingCols=1&bSortable_0=false&bSortable_1=true&bSortable_2=true&bSortable_3=false"
//						+ "&bSortable_4=true&bSortable_5=true&bSortable_6=true&bSortable_7=true&OJId="
//						+ oj);
//		initPost(post, "application/json, text/javascript, */*; q=0.01", "application/x-www-form-urlencoded");
//
//		try {
//
//			HttpResponse response = client.execute(post, context);
//
//			int stat = response.getStatusLine().getStatusCode();
//
//			String json = "", line = null;
//			System.out.println(stat);
//			if (stat == 200) {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					response.getEntity().getContent()));
//			while ((line = reader.readLine()) != null)
//				json += line + "\n";
//			 }else{
//				 return null;
//			 }
//			// System.out.println(json);
//			Gson gson = new Gson();
//			Hust hustData = gson.fromJson(json, Hust.class);
//
//			String realId = null;
//			// System.out.println(hustData.getAaData().size());
//			/*for (Object obj : hustData.getAaData()) {
//				List list = (List) obj;
//				if (list.get(1).toString().equals(problem))
//					realId = ((Double) list.get(5)).intValue() + "";
//			}
//*/
//			System.out.println("Hust的Id:" + realId);
//			EntityUtils.consume(response.getEntity());
//			return realId;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		return null;
//	}

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
			//System.out.println(response.getStatusLine().getStatusCode());
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

	static class Hust {
		//{"data":[["HDU","4427","Math Magic",1417148729000,"2012 Asia ChangChun Regional Contest",33419,33419,2000],["ZOJ","3662","Math Magic",1416567735000,"",32818,32818,3000],["UVALive","6073","Math Magic",1415886019000,"<a href=\"https:\/\/icpcarchive.ecs.baylor.edu:443\/index.php?option=com_onlinejudge&amp;Itemid=8&amp;category=548\">Regionals 2012<\/a> &gt;&gt; <a href=\"https:\/\/icpcarchive.ecs.baylor.edu:443\/index.php?option=com_onlinejudge&amp;Itemid=8&amp;category=552\">Asia - Changchun<\/a>",37874,37874,3000]],"draw":1,"recordsFiltered":3,"recordsTotal":30854}

		 private String draw;

		    private String recordsFiltered;

		    private String[][] data;

		    private String recordsTotal;

		    public String getDraw ()
		    {
		        return draw;
		    }

		    public void setDraw (String draw)
		    {
		        this.draw = draw;
		    }

		    public String getRecordsFiltered ()
		    {
		        return recordsFiltered;
		    }

		    public void setRecordsFiltered (String recordsFiltered)
		    {
		        this.recordsFiltered = recordsFiltered;
		    }

		    public String[][] getData ()
		    {
		        return data;
		    }

		    public void setData (String[][] data)
		    {
		        this.data = data;
		    }

		    public String getRecordsTotal ()
		    {
		        return recordsTotal;
		    }

		    public void setRecordsTotal (String recordsTotal)
		    {
		        this.recordsTotal = recordsTotal;
		    }
	}
	
    
	static class FetchResult
	{
	    private String draw;

	    private String recordsFiltered;

	    private String[][] data;

	    private String recordsTotal;

	    public String getDraw ()
	    {
	        return draw;
	    }

	    public void setDraw (String draw)
	    {
	        this.draw = draw;
	    }

	    public String getRecordsFiltered ()
	    {
	        return recordsFiltered;
	    }

	    public void setRecordsFiltered (String recordsFiltered)
	    {
	        this.recordsFiltered = recordsFiltered;
	    }

	    public String[][] getData ()
	    {
	        return data;
	    }

	    public void setData (String[][] data)
	    {
	        this.data = data;
	    }

	    public String getRecordsTotal ()
	    {
	        return recordsTotal;
	    }

	    public void setRecordsTotal (String recordsTotal)
	    {
	        this.recordsTotal = recordsTotal;
	    }
	}

}
