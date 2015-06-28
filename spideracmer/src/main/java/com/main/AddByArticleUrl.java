package com.main;


import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.model.WpTermTaxonomy;
import com.sqider.PageData;
import com.sqider.Spider;
import com.sqider.Spider4Cnblog;
import com.sqider.Spider4Csdn;
import com.util.Init;
import com.util.MyUtil;
import org.htmlparser.tags.LinkTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddByArticleUrl {
	public static boolean isZhuan;

	static Logger logger = LoggerFactory.getLogger(AddByArticleUrl.class);

	public static void addbyUrl(String artileUrl, Set<WpTermTaxonomy> set, int hours, String wpurl,
			boolean addTag){
		
	//	Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();
		
		Init.init(!addTag);
System.out.println("延迟的小时数：" + hours);
System.out.println("选择的标签个数:" + set.size());
		WpPostsDAO pdao = new WpPostsDAO();
		WpPosts post = null;
		
		post = parseUrl(artileUrl, addTag);
		
		if(post == null){
			System.out.println("获取的文章的null");
			return;
		}
		
		Util.setCommonPost(post, hours);
		post.setPostExcerpt(post.getPostTitle());
		if(wpurl == null || wpurl.trim().equals("")){
			//设置固态链接
			post.setPostName( "article-" + System.currentTimeMillis());
		}else if(wpurl.trim().equals("0")){
			post.setPostName(MyUtil.clearTitleToUrl(post.getPostTitle()));
		}
		else{
			post.setPostName(MyUtil.clearTitleToUrl(wpurl));
		}
		//post.setPostTitle(post.getPostTitle());
		
		try {
			String contText = Util.getText(post, isZhuan);
			post.setPostContent(contText);
			pdao.save(post);
			
			//第一步存储结束！
			post.setGuid(Init.host + "/?p=" + post.getId());
			
			//如果搜集标签，则自动搜集标签
			if(addTag){
				if(set == null && set.size() > 0){
					for(WpTermTaxonomy tax:set) post.getTerms().add(tax);
				}
			}else
				post.setTerms(set);
			
			pdao.save(post);
			
			System.out.println("结束---------------------------");
		//	tran.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	
	public static void addByAuthor_url(String author_Url, Set<WpTermTaxonomy> set, int hours, String wpurl,
			boolean addTag){
		
	}
	
	public static List<String> getArticleUrls(String author_Url){
//		?viewmode=contents
		List<String> list = new ArrayList<String>();
		if(author_Url.contains("blog.csdn.net")){
			PageData pd = MyUtil.getPage(author_Url + "?viewmode=contents");
			List<LinkTag> listLinks = MyUtil.parseTags(pd.html, LinkTag.class, "title", "阅读次数");
			for(LinkTag link:listLinks){
				System.out.println(link.getLink());
			}
			
		}
		
		
		return null;
	}
	
	public static WpPosts parseUrl(String artileUrl, boolean addTag){
		//System.out.println(trueUrl + ":" + artileUrl);
		if(!isRightUrl(artileUrl)){
			return null;
		}
System.out.println("parseUrl artileUrl: " + artileUrl);
		PageData pd = MyUtil.getPage(artileUrl, true);
		if(pd == null){
			//System.out.println("获取页面数据失败！！  :" + artileUrl);
			return null;
		}
		Spider spider = null;
		
		if(pd.host.endsWith("blog.csdn.net") && !pd.host.contains("m.blog.csdn")){
			spider = new Spider4Csdn();
			return spider.parseArticleSUrl(pd,  null, addTag);
		}else if(pd.host.endsWith("cnblogs.com")){
			spider = new Spider4Cnblog();
			return spider.parseArticleSUrl(pd , null, addTag);
		}else{
			logger.info("no spider for :" + pd.host);
			//System.out.println(pd.host);
		}
		
		return null;
	}
	
	private static boolean isRightUrl(String artileUrl) {
		if(artileUrl.contains("blog.csdn") || artileUrl.contains("cnblogs.com")
		) return true;
		return false;
	}
	
	public static void main(String[] args) {
		//String articleStr = "http://blog.csdn.net/gladyoucame/article/details/11896455";
		//addbyUrl(articleStr, null, 0, "",false);
		
		String author_url = "http://blog.csdn.net/zdp072/article/category/1606133";
		getArticleUrls(author_url);
		
		
		//WpPosts post = parseUrl("http://blog.csdn.net/gladyoucame/article/details/14139459", false);
		//System.out.println(post);
	}
	

}
