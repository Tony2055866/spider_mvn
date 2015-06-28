package com.main;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.util.DbUtil;
import org.htmlparser.tags.LinkTag;

import com.itblog.sqider.PageData;
import com.itblog.sqider.Spider;
import com.itblog.sqider.Spider4Cnblog;
import com.itblog.sqider.Spider4Csdn;
import com.itong.main.ItblogUtil;
import com.model.Log;
import com.model.LogDAO;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.model.WpTermTaxonomy;
import com.util.ItblogInit;
import com.util.MyUtil;

public class AddByArticleUrl {
	public static boolean isZhuan;
	static{
		if(ItblogInit.defautTerm == null){
			ItblogInit.init();
		}
	}
	
	
	
	public static void addbyUrl(String artileUrl, Set<WpTermTaxonomy> set, int hours, String wpurl,
			boolean addTag){
		if(DbUtil.checkUrl(artileUrl)){
			System.out.println("已存在");
			return;
		}
	//	Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();
		
		ItblogInit.init(!addTag);
System.out.println("延迟的小时数：" + hours);
System.out.println("选择的标签个数:" + set.size());
		WpPostsDAO pdao = new WpPostsDAO();
		WpPosts post = null;
		
		boolean tag = false;
		if(set == null || set.size() == 0) tag = true;
		post = parseUrl(artileUrl, tag);
		
		if(post == null){
			System.out.println("获取的文章的null");
			return;
		}
		
		ItblogUtil.setCommonPost(post, hours);
		post.setPostExcerpt(post.getPostTitle());
		if(wpurl == null || wpurl.trim().equals("")){
			//设置固态链接
			post.setPostName( "article-" + System.currentTimeMillis());
		}else if(wpurl.trim().equals("0")){
			post.setPostName(MyUtil.clearTitle(post.getPostTitle()));
		}
		else{
			post.setPostName(MyUtil.clearTitle(wpurl));
		}
		//post.setPostTitle(post.getPostTitle());
		
		try {
			String contText = ItblogUtil.getText(post, isZhuan);
			post.setPostContent(contText);
			pdao.save(post);
			
			//第一步存储结束！
			post.setGuid(ItblogInit.host + "/?p=" + post.getId());
			
			//如果搜集标签，则自动搜集标签
			if(post.getTerms().size() == 0){
				post.getTerms().add(ItblogInit.defautTerm);
			}
			pdao.save(post);
			
			
			Log log = new Log();
			log.setUrl(post.url);
			log.setOther(post.getGuid());
			new LogDAO().save(log);
			
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
			System.out.println(pd.host);
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
