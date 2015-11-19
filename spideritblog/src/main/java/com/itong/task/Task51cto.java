package com.itong.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itblog.sqider.CtoSpider;
import com.itong.main.ItblogUtil;
import com.model.Log;
import com.model.LogDAO;
import com.model.WpPosts;
import com.util.DbUtil;

public class Task51cto extends MyTask{
	
	public static void main(String[] args) {
		MyTask task = Task51cto.getTask();
		Task51cto.getTask().start();
		task.restart();
	}
	
	private Logger logger = Logger.getLogger(this.getClass());
	 
	private static Task51cto cto;
	
	public static Task51cto getTask(){
		if(cto == null){
			cto= new Task51cto();
		}
		return cto;
	}
	private Task51cto(){
		sleepTimeHours = 8;
		sleeptimePerUrl = 300;
		pages = 2;
	}
	
	/*@Override
	public void run() {
		isRunning = true;
		while(true){
			CtoSpider ctospider = new CtoSpider();
			List<String> urls= CtoSpider.getBlogUrls(0);
			for(String url:urls){
				if(DbUtil.checkUrl(url)){
					logger.info("重复 抓取文章");
					continue;
				}
				try {
					logger.info(url);
					WpPosts post = ctospider.parseUrl(url, true);
					//logger.info(Util.getText(post));
					ItblogUtil.saveCommPost(post,false);
					count ++;
				} catch (Exception e) {
					LogDAO ldao = new LogDAO();
					Log log = new Log();
					log.setTime(new Timestamp(System.currentTimeMillis()));
					log.setUrl(url);
					log.setOther("error!  " + e.getMessage());
					ldao.save(log);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//break;
			}
			
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}*/
	CtoSpider ctospider = new CtoSpider();
	
	@Override
	public List<String> findArticleInPage(int page) {
		
		List<String> allUrls = new ArrayList<String>();
		for(int i=0; i<page; i++){
			List<String> urls= CtoSpider.getBlogUrls(i);
			for(String url:urls)
				if(!allUrls.contains(url)) allUrls.add(url);
//			allUrls.addAll(urls);
		}
		return allUrls;
	}
	@Override
	public WpPosts parseArticle(String url) {
		return  ctospider.parseUrl(url, true);
	}
	
	
	
}
