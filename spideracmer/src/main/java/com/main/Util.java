package com.main;


import com.model.LogDAO;
import com.model.WpPosts;
import com.sqider.Content;
import com.sqider.PageData;
import com.util.ImageUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Util {

	private static Logger logger = LoggerFactory.getLogger(Util.class);


	static int dayTime = 24 *60  * 60 * 1000;
	static long hourTime = 60  * 60 * 1000;
	public static String adStrings[] = {
			"联系电话","业务QQ","信誉第一"
	};
	public static void setCommonPost(WpPosts post, int hours){
		setCommonPost(post, hours, 1L);
	}
	
	public static long lastTime = 0;
	public static void setCommonPost(WpPosts post, int hours, long author){
		Random random = new Random();
		long postTime = new Date().getTime() - hours * hourTime ;
		
		if(lastTime != 0){
			lastTime += (1000 * 60) * (random.nextInt(8) + 3) + random.nextInt(60000); //必须和上一篇相差 3分钟以上
			postTime = lastTime;
		}
		
		Timestamp tm = new Timestamp(postTime);
		Timestamp tm2 = new Timestamp(postTime -  60 * 8 * 60 * 1000);
		
		post.setPostAuthor(author);
		post.setPostDate(tm);
		post.setPostDateGmt(tm2);
		
		post.setPostStatus("publish");
		post.setCommentStatus("open");
		post.setPingStatus("open");
		post.setToPing("");
		post.setPinged("");
		post.setPostModified(tm);
		post.setPostModifiedGmt(tm2);
		post.setPostContentFiltered("");
		post.setPostParent(0L);
		post.setPostPassword("");
		post.setGuid("");
		post.setPostMimeType("");
		
		post.setMenuOrder(0);
		post.setPostType("post");
		post.setCommentCount(0L);
		
		lastTime = postTime;
	}
	
	
	public static String getText(WpPosts post) throws Exception{
		return getText(post, true);
	}
	/**
	 * 对于一般的文章，不需要判断 OJ等等。   一般性方法
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public static String getText(WpPosts post, boolean isZhuan) throws Exception {
	//	String text = proData.text + "\n";
		String text = "";
		for(int i=0; i<post.listContent.size(); i++){
			Content content = post.listContent.get(i);
			//logger.info(content.text);
			if(content == null) continue;
			if(!content.isCode ){
				content.text = content.text.replaceAll("href=\"http://.+?\"", "");
				content.text = content.text.replaceAll("class=\".+?\"", "");
				 //text += ImageUtil.modeyUrlOnly(content.text, post.url, post.host);
				 //ImageUtil.modifyImgHtml(content.text, post.url, Init.host, post.url, Init.baseDownLoad, post.host);
				 text += ImageUtil.modifyImgHtml(content.text, new PageData(post.host,post.url));
			}
			else{
				text += "<pre class=\"brush:" + content.lang + " \">";
				text += content.text.trim();
				text += "</pre>";
			}
		}
		if(isZhuan)
			text += "原文：" + post.url;
		return text;
	}
	
	
	
	public static String  getPlainHtml(String text){
		if(text == null) return "";
		String res = "", tmp;
		Scanner scanner = new Scanner(text);
		String last = "test";
		while(scanner.hasNextLine()){
			tmp = scanner.nextLine();
			String t = StringEscapeUtils.unescapeHtml4(tmp).replaceAll("[\\s\\u00A0]+$", "");
			if(t.trim().equals(""))continue;
			else{
				res += tmp +"<br>\n";
			}
				
		}
		return res;
	}
	
	
	
	public static void main(String[] args) {
//		logger.info(StringEscapeUtils.unescapeHtml4("&nbsp; &nbsp; &nbsp;").trim().replaceAll("[\\s\\u00A0]+$", ""));
//		logger.info(StringEscapeUtils.unescapeHtml4("&nbsp;").equals(" "));
//		logger.info((int)StringEscapeUtils.unescapeHtml4("&nbsp;").charAt(0));
//		logger.info((int)' ');
//		logger.info("&nbsp; &nbsp;".replaceAll("&nbsp;", "dd"));
		long postTime = System.currentTimeMillis() - (long)(1000 * hourTime) ;
	
	}

	
	
	/**
	 * inputstream to string utf-8编码
	 * 
	 * @param is
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String convertStreamToString(InputStream is)
			throws UnsupportedEncodingException {
		BufferedInputStream bis = new BufferedInputStream(is);
		InputStreamReader inputStreamReader = new InputStreamReader(bis,
				"utf-8");
		BufferedReader br = new BufferedReader(inputStreamReader);
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static boolean isNumber(String str){
		for(int i=0; i<str.length(); i++){
			if(str.charAt(i) < '0' || str.charAt(i) > '9') return false;
		}
		return true;
	}
	
	public static boolean checkLogurl(String url){
		List list = new LogDAO().findByUrl(url);
		if(list.size() > 0) return true;
		return false;
	}
}
