package com.main;

import java.io.File;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Transaction;

import com.itblog.sqider.Baidu;
import com.itblog.sqider.Content;
import com.itblog.sqider.Google;
import com.itblog.sqider.PageData;
import com.itblog.sqider.ProblemData;
import com.itblog.sqider.SearchEngine;
import com.itblog.sqider.So360;
import com.itblog.sqider.Sougou;
import com.itblog.sqider.Spider;
import com.itblog.sqider.Spider4163;
import com.itblog.sqider.Spider4Baidu;
import com.itblog.sqider.Spider4Cnblog;
import com.itblog.sqider.Spider4Cppblog;
import com.itblog.sqider.Spider4Csdn;
import com.itblog.sqider.Spider4Sina;
import com.itong.main.ItblogUtil;
import com.model.Log;
import com.model.LogDAO;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.model.WpTermTaxonomy;
import com.model.WpTermTaxonomyDAO;
import com.model.WpTerms;
import com.sevlets.OJTask;
import com.util.HibernateSessionFactory;
import com.util.HojUtil;
import com.util.ImageUtil;
import com.util.ItblogInit;
import com.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static WpTermTaxonomyDAO wtdao = new WpTermTaxonomyDAO();
	public static WpTermTaxonomy termtaxHojCat ;
	public static WpTermTaxonomy termtaxHojTag ;
	
	static Baidu baidu = new Baidu();
	static Google google = new Google();
	static Sougou sogou = new Sougou();
	static So360 so = new So360();
	static SearchEngine ses[]= new SearchEngine[]{google,sogou,baidu,so};
	
	public static String ojs[] = new String[]{"uva","poj","hoj","hdu","hdoj","pku","zoj","zju"};
	
	static{
		Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();
		termtaxHojCat =wtdao.findById(30L);
		termtaxHojTag =wtdao.findById(11L);
		tran.commit();
		
		
	}
	public static int findN = 2; //找到优秀博客的篇数2，就结束
	public static int normalN = 3;//一般的博客，找到4篇
	
	
	//最终标题排除在外的ID(数据库中的ID)，主要是acm HOJ, 公司名称 等词
	public static long[] exIds = {3,10,11,13,29,30,31,32,33,34,65,66,86,102,101,108,109,111,113};
	
	public static int problem;
	static int searchOrder = 1;
	public static ProblemData proData;
	static int start=0,end=0;
	public static String ojtype,ojtypebak;
	public static int delayHours;
	public static long author = 14L;
	public static  PrintStream out;
	public static void startOJTask(String oj, String ojbak, int s, int e, int delayHours) throws Exception{
		ItblogInit.init();
		ojtype = oj;
		ojtypebak = ojbak;
		start= s;
		end = e;
		searchOrder = 1;
		OJTask.stop = false;
		try{
		OJTask.ojlogFile = new File(ItblogInit.logDir + File.separator +"ojlog_" + System.currentTimeMillis() +".txt");
		if(!OJTask.ojlogFile.getParentFile().exists())
			OJTask.ojlogFile.getParentFile().mkdirs();
		if(!OJTask.ojlogFile.exists()) OJTask.ojlogFile.createNewFile();
logger.info("写入log：" +OJTask.ojlogFile.getAbsolutePath() );
out = new PrintStream(OJTask.ojlogFile);
out.println("写入log：" +OJTask.ojlogFile.getAbsolutePath() );
		getAnswer(); //开始调用，运行
		//System.setOut(out);
		}catch(Exception ee){
			OJTask.stop = true;
			ee.printStackTrace();
out.println("出现错误，任务停止！！"+ ee.getMessage());
			return;
		}
	}
	
	public static void main(String[] args) throws Exception {
		ItblogInit.init();
		ojtype = "hdu";
		ojtypebak ="hoj";
		if(args.length >= 2){
			start = Integer.parseInt(args[0]);
			end = Integer.parseInt(args[1]);
		}else{
			start = 3511;
			end = 3700;
		}
		if(args.length >= 3){
			searchOrder = Integer.parseInt(args[2]);
		}
		if(args.length >=4)
			delayHours = Integer.parseInt(args[3]);
//		if(args.length >= 5){
//			getUnfinished();
//		}else
		OJTask.stop =false;
			getAnswer();
		//OJTask.stop = false;
		//testGetPost();
	}
	
	

	private static void getAnswer()  {
		//统一用小写
		String[] keys = new String[2];
		logger.info("start" + start + "  end " + end);
		for( problem=start; problem< end && !OJTask.stop; problem++){
			urlSet = new HashSet<String>();
			keys[0] = ojtype;
			keys[1] = problem+"";
			
logger.info("start problem: " + problem  + "  "  + ojtype);
if(out!=null)
	out.println("start problem: " + problem  + "  "  + ojtype);

			//Log log = checkIfFinished(keys);
//			if(log != null  ){
//				if(log.getOther().contains("found")){
//					updatePostByKeys(keys);
//				}
//			}else{
				try {
					int flag = savePostByKeys(keys);
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("出现错误，停止！！");
					if(out!=null) out.println("出现错误，停止！！");
					return;
				}
		}
		OJTask.stop = true;
	}
	private static void updatePostByKeys(String[] keys) {
		
	}
	
	

	private static Log checkIfFinished(String[] keys) {
		LogDAO ldao = new LogDAO();
		String vlaue =  keys[0] + " " + keys[1];
		List list = ldao.findByProperty("name",vlaue);
		if(list.size() > 0) return (Log) list.get(0);
		return null;
	}

	public static int savePostByKeys(String[] keys) throws Exception {
		return savePostByKeys(keys, null);
	}
	
	public static int savePostByKeys(String[] keys, Log log) throws Exception {
		try {
			proData = HojUtil.getPorblemStr(keys[1], true);
		} catch (Exception e) {
			e.printStackTrace();
			if(log == null)
			  log = new Log();
			log.setFlag(1); //未完成
			log.setName(ojtype + " " + problem);
			log.setTime(new Timestamp(new Date().getTime()));
			log.setOther("problem error!");
			log.setUrl("");
			LogDAO lDao = new LogDAO();
			lDao.save(log);
			return 0;
			//e.printStackTrace();
		}
		
		WpPosts finalPost = getFinalPost(keys);
		logger.info("final post info:");
		
		if(finalPost != null){
			logger.info(finalPost.url);
			logger.info(finalPost.power + "");
			
			savePost(finalPost, keys, log);
		}else{
logger.info("查找失败！！");
if(out!=null)
out.println("查找失败！！");
			if(log!=null && log.getFlag() != -1)
				savePostNotFinished(keys);
			if(log == null){
				savePostNotFinished(keys); 
				log = new Log();
			}
			log = new Log();
			log.setFlag(-1); //未完成, 存下文章
			if(keys.length > 1)
				log.setName(ojtype +" " + keys[1]);
			else
				log.setName(ojtype +" "  + keys[0]);
			log.setTime(new Timestamp(new Date().getTime()));
			log.setOther("not found");
			LogDAO lDao = new LogDAO();
			lDao.save(log);
		}
		return 1;
	}
	
	/**
	 * 返回 最终存储的文章
	 */
	public static WpPosts getFinalPost(String[] keys) throws Exception {
		// TODO Auto-generated method stub
		List<WpPosts> posts = getFinalPosts(keys);
		//logger.info("找到结果返回, 结果个数：" + posts.size());
		WpPosts finalPost = null;
		int maxPower = -1000;
		for(WpPosts post:posts){
			if(post.power > maxPower){
				maxPower = post.power;
				finalPost = post;
			}
			logger.info("all post info:");
			logger.info(post.url);
			logger.info("post.power:" + post.power);
		}
		return finalPost;
	}
	/**
	 * 返回的是 所以可能的文章列表. 并在同时 查看并提交本地的代码（如果有的话）
	 */
	public static List<WpPosts> getFinalPosts(String[] keys) throws Exception {
		List<WpPosts> posts = new ArrayList<WpPosts>();
		//logger.info("");
		  find(keys, posts);
		if(posts.size()  < normalN ){
			keys[0] = ojtypebak;
			find(keys,posts);
		}
		//普通搜索没找到
		if(posts.size() == 0 && proData != null ){
			find(new String[]{proData.title.toLowerCase()}, posts);
			for(int i=0; i<ojs.length && posts.size() < 1; i++){
				find(new String[]{proData.title.toLowerCase() , ojs[i] }, posts);
			}
		}
		return posts;
	}
	
	
	public static void find(String[] keys, List<WpPosts> posts) throws Exception{
		find(keys, posts, 0);
	}

	static Set<String> urlSet = new HashSet<String>();
	public static boolean debug = true;
	/**
	 * 
	 * @param keys 搜索的关键词( hdu 1100 )
	 * @param posts 搜索到的posts列表,结果保存在此列表
	 * @param method  google优先还是百度优先。默认google优先. 
	 * @throws Exception 
	 */
	public static void find(String[] keys, List<WpPosts> posts, int method) throws Exception{
		int pNum = 20;
		WpPosts post = null;
		Set<String> uniqueUrls = new HashSet<String>();
		
		int goodCnt = 0, ncnt = 0;//统计博客园和CSDN的文章数量
		
		int baiduIndex = 1;
		//urlSet = new HashSet<String>();
		for(int i=0;i<ses.length; i++){
			List<String> urls = ses[i].search(keys, pNum, method);
			int cnt = 0;
			if(urls == null  && !UnfJob.fast){
				urls = ses[i].search(keys, pNum, method);
				cnt++;
				if(out!=null)
		out.println("搜索失败，等待中！！！" );
				if(cnt >= 4) break;
				try {
					Thread.sleep( 20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			if(urls == null && !UnfJob.fast){
//				throw new Exception("search failed！");
//			}

			if(urls == null)
				urls = new ArrayList<String>();
logger.info("搜索引擎找到的果个数：" + urls.size());
if(out!=null)
out.println("搜索引擎找到的果个数：" + urls.size());
			for(String url:urls){
logger.info("搜索" + i + " " + url);
				if(goodCnt >= findN || posts.size() > 5){
					logger.info("找到"+  findN +"篇文章，返回");
					if(out!=null)
					out.println("查找失败！！");
					return ;
				}
				
				if(uniqueUrls.contains(url)) continue;
				uniqueUrls.add(url);
				if( (post=parseSearchUrl(url, keys, baiduIndex!=i)) != null){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					posts.add(post);
					if(post.power > 0 && post.url.contains("blog.csdn") || post.url.contains("cnblog.com")){
						goodCnt++;
					}
					
				}
			}
		}
	
	}
	public static void savePost(WpPosts post, String[] keys, Log log) throws Exception {
		String mainKeyWord = setMainTerms(post);
		
		if(mainKeyWord != null &&!post.getPostTitle().contains(mainKeyWord)){
			
		}
//		if( !post.getPostTitle().contains("解题") && !post.getPostTitle().contains("报告")){
//			post.setPostTitle(post.getPostTitle() + "[解题报告]");
//		}
		String postText = getText(post);
		//logger.info(postText);
		savePost(post, mainKeyWord, postText, keys, log);
	}

	public static String setMainTerms(WpPosts post) {
		List<Map.Entry<WpTermTaxonomy,Integer>> terms = post.listkeyCnt;
		
		//finalPost.setWpCommentses(terms);
		int cnt = 0;
		String mainKeyWord = null;
		
		if(terms!=null){
			if(debug)
			logger.info("add Term Num: " + terms.size());
			int cnt1=0,cnt2=0;
			for(Map.Entry<WpTermTaxonomy,Integer> entry:terms){
				WpTerms t = entry.getKey().getTerm();
				if(t!=null && !checkKey(t.getTermId())) continue;
				
				if(mainKeyWord == null){
					mainKeyWord = t.getName();
logger.info("main keyword: " + mainKeyWord);
if(out!=null)
out.println("main keyword: " + mainKeyWord);
				}else{
if(debug)
logger.info("keyword: " + t.getName());
if(out!=null)
out.println("keyword: " + t.getName());
				//if(cnt >= 2) break;
				}
				if(entry.getKey().getTaxonomy().equals("category")){
					if(cnt1 <= 1) post.getTerms().add(entry.getKey());
					cnt1++;
				}else if(entry.getKey().getTaxonomy().equals("post_tag")){
					if(cnt2 <=1)post.getTerms().add(entry.getKey());
					cnt2++;
				}
			}
		}
		return mainKeyWord;
	}
	
	//存储没有 解决的题目
	public static void savePostNotFinished(String[] keys) {
logger.info("存储为解决的题目！！！");
if(out!=null)
out.println("存储为解决的题目！！！");
	//	Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();
		WpPosts post = new WpPosts();
		WpPostsDAO pdao = new WpPostsDAO();
		ItblogUtil.setCommonPost(post, delayHours, 14L);
		
		post.setPostContent(proData.text);
		post.setPostTitle(ojtype +" " + keys[1] + " " + proData.title + "待解决" + "[解题报告]" + "C++");
		
		post.setPostExcerpt(post.getPostTitle());
		
		String ctitle = MyUtil.clearTitle(proData.title);
		ctitle = ctitle.trim();
		if(ctitle.endsWith("-")) ctitle = ctitle.substring(1, ctitle.length()-1);
		if(ctitle.equals(""))
			post.setPostName(ojtype +"-" + keys[1]);
		else
			post.setPostName(ojtype +"-" + keys[1] + "-" +ctitle);
		
		pdao.save(post);
		//logger.info(wpPosts.getId());
		
		post.setGuid(ItblogInit.host + "/?p=" + post.getId());
		post.getTerms().add(termtaxHojCat);
		post.getTerms().add(termtaxHojTag);
		
		pdao.save(post);
		
		//tran.commit();
	}

	private static void savePost(WpPosts post, String mainKeyWord,
			String postText,String keys[], Log log) {
		//Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();

		
		WpPostsDAO pdao = new WpPostsDAO();
		ItblogUtil.setCommonPost(post, delayHours, 14L);
		post.setPostContent(postText);
		
		String lang = getMainLang(post);
		if(lang.toLowerCase().equals("cpp")) lang = "C++";
		//post.setPostTitle("HD " + num + " " + pdata.title + " [解题报告] Java");
		String keyWordInTitle = mainKeyWord == null ? "": "-"+mainKeyWord+"-";
		if(ojtypebak != null && ojtypebak != "")
			post.setPostTitle(ojtype.toUpperCase() +" " + keys[1] + "-"  +  proData.title + keyWordInTitle + "[解题报告]" + ojtypebak.toUpperCase() );
		else
			post.setPostTitle(ojtype.toUpperCase() +" " + keys[1] + "-"  + proData.title + keyWordInTitle + "[解题报告]" + lang);
		post.setPostExcerpt(post.getPostTitle());
		String ctitle = MyUtil.clearTitle(proData.title);
		ctitle = ctitle.trim();
		if(ctitle.endsWith("-")) ctitle = ctitle.substring(1, ctitle.length()-1);
		if(ctitle.equals(""))
			post.setPostName(ojtype +"-" + keys[1]);
		else
			post.setPostName(ojtype +"-" + keys[1] + "-" +ctitle);
		//post.setPostName(ojtype +"-" + keys[1] +"-" + MyUtil.clearTitle(proData.title));
	
		pdao.save(post);
		//logger.info(wpPosts.getId());
		post.setGuid(ItblogInit.host + "/?p=" + post.getId());
		post.getTerms().add(termtaxHojCat);
		post.getTerms().add(termtaxHojTag);
		
		pdao.save(post);
		
		//tran.commit();
		
logger.info("---------------------------");
if(out!=null)
out.println("-----------------------------------------------");
Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();

		if(log == null)
		  log = new Log();
		log.setFlag(1); //未完成
		log.setUrl(post.url);
		log.setName(ojtype +" "+ keys[1]);
		log.setTime(new Timestamp(new Date().getTime()));
		LogDAO lDao = new LogDAO();
		log.setOther(post.getId() + "");
		lDao.save(log);
		tran.commit();

	}
	
	private static String getMainLang(WpPosts post) {
		for(int i=0; i<post.listContent.size(); i++){
			
			Content content = post.listContent.get(i);
			if(content == null) continue;
			if(content.isCode) return content.lang;
		}
		return "cpp";
	}

	private static String getText(WpPosts post) throws Exception {
		return getText(post,true);
	}
	public static String getText(WpPosts post, boolean pro) throws Exception {
		String text = "";
		
		//文章有题目时，直接整篇文章
		if(post.listContent.size() == 1 && post.hasPro){
			Content content = post.listContent.get(0);
			if(post.pageData != null)
				text += ImageUtil.modifyImgHtml(content.text, post.pageData);
			else
				text += ImageUtil.modifyImgHtml(content.text, new PageData(post.host,post.url));
		}else {

			if(pro && proData!=null)
				text = proData.text + "\n";
			//logger.info(proData.text);
			for(int i=0; i<post.listContent.size(); i++){
				Content content = post.listContent.get(i);
				if(content == null || content.text == null || content.text == "") continue;
				if(!content.isCode ){

					content.text = content.text.replaceAll("href=\"http://.+?\"", "");
					content.text = content.text.replaceAll("class=\"brush", "xxxxxbrush");
					content.text = content.text.replaceAll("class=\".+?\"", "");
					content.text = content.text.replaceAll("xxxxxbrush", "class=\"brush");
					//	logger.info(content.text);
					//post.hasPro 说明文章部分含有问题，则不插入该博客的文字部分 (163 新浪等除外)
					if( !post.hasPro || !post.hasCode ){
						if(post.pageData != null)
							text += ImageUtil.modifyImgHtml(content.text, post.pageData);
						else
							text += ImageUtil.modifyImgHtml(content.text, new PageData(post.host,post.url));
					}
				}
				else{
					text += "<pre class=\"brush:" + content.lang + " \">";
					text += content.text.trim();
					text += "</pre>";
				}
			}
			
		}
		
		
		if(post.url != null)
			text += "参考：<a href='#respond'>" + post.url + "</a>";
		//logger.info("----------------");
		//logger.info(text);
		return text;
	}

	
	//解析搜索到的链接. 并将连接传给相应网站的spider (百度)
	public static WpPosts parseSearchUrl(String artileUrl, String[] searchKeys, boolean trueUrl){
//logger.info(trueUrl + ":" + artileUrl);
		if(trueUrl && !isRightUrl(artileUrl)){
			return null;
		}
if(debug )
logger.info("artileUrl: " + artileUrl);
		PageData pd = MyUtil.getPage(artileUrl, false);
		if(pd == null){
if(debug)
logger.info("获取页面数据失败！！  :" + artileUrl);
			return null;
		}
		if(urlSet.contains(pd.url)) return null;
		urlSet.add(pd.url);
		Spider spider = null;
		if(pd.host.endsWith("blog.csdn.net") && !pd.host.contains("m.blog.csdn")){
			spider = new Spider4Csdn();
			return spider.parseArticleSUrl(pd,  searchKeys);
		}else if(pd.host.endsWith("cnblogs.com")){
			spider = new Spider4Cnblog();
			return spider.parseArticleSUrl(pd , searchKeys);
		}else if(pd.host.contains("blog.163.com")){
			spider = new Spider4163();
			return spider.parseArticleSUrl(pd , searchKeys);
		}else if(pd.host.contains("blog.sina.com")){
			spider = new Spider4Sina();
			return spider.parseArticleSUrl(pd , searchKeys);
		}else if(pd.host.contains("hi.baidu.com")){
			spider = new Spider4Baidu();
			return spider.parseArticleSUrl(pd , searchKeys);
		}else if(pd.host.contains("shaidaima.com")){
			
			return spider.parseArticleSUrl(pd , searchKeys);
		}else if(pd.host.contains("cppblog")){
			return new Spider4Cppblog().parseArticleSUrl(pd , searchKeys);
		}
		return null;
	}

	public static boolean isRightUrl(String artileUrl) {
		//logger.info("isRightUrl:" + artileUrl);
		if(artileUrl.contains("blog.csdn") || artileUrl.contains("cnblogs.com") 
				|| artileUrl.contains("blog.163.com") || artileUrl.contains("blog.sina.com") 
				|| artileUrl.contains("hi.baidu.com") || artileUrl.contains("shaidaima") 
				|| artileUrl.contains("cppblog.com") 
		) return true;
		return false;
	}
	
	private static boolean checkKey(long termid){
		for(int i=0; i<exIds.length; i++)
			if(termid == exIds[i]) return false; 
		return true;
	}
	
	
	
}
