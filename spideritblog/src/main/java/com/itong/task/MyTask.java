package com.itong.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.util.ItblogInit;
import org.apache.log4j.Logger;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;

import com.itblog.sqider.PageData;
import com.itong.main.ItblogUtil;
import com.model.WpPosts;
import com.util.MyUtil;

public abstract class MyTask extends Thread {
	public int sleepTimeHours = 24; //hours
//	public int articlesPerUnit = 20;
	public int pages = 5;
	public int sleeptimePerUrl = 300; //seconds
	public int count;
	public boolean running = false;
	public int remain;
	public String startTime;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	public static void main(String[] args) {
//		logger.info(new Date().toLocaleString());
	}
	
	public synchronized void restart(){
		startTime = new Date().toLocaleString();
		running = true;
		notifyAll();
	}
	
	public void stopTask(){
		startTime = "";
		running = false;
	}
	
	@Override
	public void run() {
		work();
	}
	public synchronized void work() {
		try {
			while(true){
				while(!running) wait();
				List<String> urls = null;

				try{
					urls = findArticleInPage(pages);
					remain = urls.size();
				}catch (Exception e){
					e.printStackTrace();
				}

				if(urls != null){
					for(String url:urls){
						try {
						while(!running) wait();
						WpPosts post = null;

							logger.info("start parse article : " + url);
							post = parseArticle(url);
							if(post == null){
								remain--;
								continue;
							}
							post.visitedUrlMap = new HashMap<String, String>();
							post.visitedUrls = new HashSet<String>();
							post.visitedUrls.add(post.url);
							ItblogUtil.deepCrawl(post);

							ItblogUtil.saveCommPost(post,false); //投标记原文链接

							deleteIndex();
							
							logger.info("deep crawl get : " + post.visitedUrlMap.size() +   " ; " + post.visitedUrlMap);

							count ++;
							remain --;
							logger.info("sleep time :" + sleeptimePerUrl * 1000 + ";   after, save: "+ post.url);
							Thread.sleep(sleeptimePerUrl * 1000);
						} catch (Exception e) {
							logger.error("sparse article error !!! , url: " + url);
							e.printStackTrace();
						}

					}
				}


				
				Thread.sleep(sleepTimeHours * 3600 * 1000); //按小时的休息
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void deleteIndex() {
		String path = ItblogInit.hostPath + "wp-content/cache/supercache/www.51itong.net/index.html";
		File file = new File(path);
		file.delete();
		
		MyUtil.getPage(ItblogInit.host);
	}


	public abstract WpPosts parseArticle(String url);
	
	public abstract List<String> findArticleInPage(int page);
}
