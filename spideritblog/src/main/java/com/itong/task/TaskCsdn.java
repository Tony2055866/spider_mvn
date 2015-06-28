package com.itong.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;

import com.itblog.sqider.PageData;
import com.itblog.sqider.Spider4Csdn;
import com.itong.main.ItblogUtil;
import com.model.WpPosts;
import com.util.DbUtil;
import com.util.ItblogInit;
import com.util.MyUtil;

public class TaskCsdn extends MyTask{
	private static TaskCsdn instance = new TaskCsdn();
	public static TaskCsdn getTask(){
		return instance;
	}
	
	private TaskCsdn(){
		sleepTimeHours = 5;
		sleeptimePerUrl = 300;
		pages = 3;
	}
	private static Spider4Csdn spider = new Spider4Csdn();
	
	public static void main(String[] args) {
//		instance.start();
		ItblogInit.init();
		MyTask task = TaskCsdn.getTask();
		task.start();
		task.restart();
	}

	static boolean test = true;
	
	public synchronized void work() {
		if(!ItblogInit.local || !test){
			super.work();
			return;
		}
		try {
			while(true){
				while(!running) wait();
				List<String> urls = new ArrayList<String>();
				urls.add("http://blog.csdn.net/singwhatiwanna/article/details/39937639");
				remain = urls.size();
				for(String url:urls){
					while(!running) wait();
					WpPosts post = parseArticle(url);
					if(post == null){
						remain--;
						 continue;
					}
					try {
						post.visitedUrlMap = new HashMap<String, String>();
						post.visitedUrls = new HashSet<String>();
						post.visitedUrls.add(post.url);
						ItblogUtil.deepCrawl(post);
						
						ItblogUtil.saveCommPost(post,false); //标记原文链接
					} catch (Exception e) {
						e.printStackTrace();
					}
					logger.info("deep crawl get : " + post.visitedUrlMap.size() +   " ; " + post.visitedUrlMap);
					count ++;
					remain --;
					System.out.println("sleep time :" + sleeptimePerUrl * 1000 + ";   after, save: "+ post.url);
					Thread.sleep(sleeptimePerUrl * 1000);
				}
				
				Thread.sleep(sleepTimeHours * 3600 * 1000); //按小时的休息
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public WpPosts parseArticle(String url) {
		PageData pd = MyUtil.getPage(url);
		WpPosts post = spider.parseArticleSUrl(pd, null, true);
		return post;
	}
	
	public List<String> findArticleInPage(int page){
		List<String> urls = new ArrayList<String>();
		for(int p=0; p<page; p++){
			String urlstr = "http://blog.csdn.net/";
			if(p > 0){
				urlstr += "?&page=" + (p+1);
			}
			PageData pd = MyUtil.getPage(urlstr);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			List<Div> divs = MyUtil.parseTags(pd.html, Div.class, "class", "blog_list");
			
			for(Div div:divs){
				LinkTag link = MyUtil.parseTags(div.toHtml(), LinkTag.class, "target", "_blank").get(0);
				String text = link.getLinkText();
				String url = link.getLink();

				if(!url.startsWith("http"))
					url = "http://" + url;
				if(ItblogUtil.getKeysByText(text).size() > 0 && !DbUtil.checkUrl(url) && !urls.contains(url)){
					urls.add(url);
				}
			}
			
		}
		return urls;
	}

	/**
	 * 不再使用
	 * 获取被顶比较多的前5个文章的url
	 * @return
	 */
	@Deprecated
	public static List<String> getUrlsByComment(int top){
		List<String> urls = new ArrayList<String>();
		
		List<MyUrl> myurls = new ArrayList<MyUrl>();
		for(int p=0; p<5; p++){
			String urlstr = "http://blog.csdn.net/";
			if(p > 0){
				urlstr += "?&page=" + (p+1);
			}
			
			PageData pd = MyUtil.getPage(urlstr);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			List<Div> divs = MyUtil.parseTags(pd.html, Div.class, "class", "blog_list");
			
			for(Div div:divs){
				LinkTag link = MyUtil.parseTags(div.toHtml(), LinkTag.class, "target", "_blank").get(0);
				LinkTag commentlink = MyUtil.parseTags(div.toHtml(), LinkTag.class, "class", "comment").get(0);
				String upcntstr = StringUtils.substringBetween(div.toHtml(), "<span>(", ")</span>");
				int cpcnt = 0;
				int commentCnt = 0;
				try {
					cpcnt = Integer.parseInt(upcntstr);
					commentCnt = Integer.parseInt(StringUtils.substringBetween(commentlink.toHtml(), "(", ")"));
				} catch (NumberFormatException e) {
				}
				
//				if(DbUtil.checkUrl().checkLogurl(link.getLink()) && ItblogUtil.getKeysByText(link.getLinkText()).size() > 0){
//					continue;
//				}
				MyUrl url = new MyUrl(link.getLink(), cpcnt, commentCnt);
				myurls.add(url);
			}
			
		}
		
		Collections.sort(myurls);
		
		for(int i=0; i<myurls.size() && i<top; i++)
			urls.add(myurls.get(i).url);
		return urls;
		
	}
	
	static class MyUrl implements Comparable<MyUrl>{
		String url;
		int upCnt;
		int commentCnt;
		public MyUrl(String link, int cpcnt, int commentCnt2) {
			if(link.startsWith("http"))
				url = link;
			else url = "http://" + link;
			upCnt = cpcnt;
			commentCnt = commentCnt2;
		}
		@Override
		public int compareTo(MyUrl o) {
			if(commentCnt == o.commentCnt){
				return o.upCnt - upCnt;
			}
			return o.commentCnt - commentCnt;
		}
		
		
	}
	
	
}
