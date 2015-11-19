package com.main;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.model.Log;
import com.model.LogDAO;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.util.HibernateSessionFactory;
import com.util.HojUtil;
import com.util.ItblogInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnfJob {
	private static Logger logger = LoggerFactory.getLogger(UnfJob.class);

	public static String from;
	public static boolean fast = false;
	public static void main(String[] args) {
		ItblogInit.init();
		LogDAO lDao = new LogDAO();
		List<Log> list = lDao.findByUrl("");
		//Log log = lDao.findById(185);
		for(Log log:list){
			if(log.getName().startsWith("hoj") || log.getName().startsWith("hdu")){
				//if(log.getOther().contains("not found")) continue;
				String problem = log.getName().split(" ")[1];
				try {
					String[] keys = new String[]{"hdu",problem};
					
					Main.savePostByKeys(keys, log);
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			logger.info(log.getName());
		}
	}
	
	public static boolean unf = true;
	public static void getUnfinished() throws Exception {
		
		fast = true;
		Main.ojtypebak = "hoj";
		Main.ojtype = "hdu";
		WpPostsDAO wpdao = new WpPostsDAO();
		Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();
		Session session= HibernateSessionFactory.openCurrentSession();
		List<WpPosts> postsList = session.createQuery("from WpPosts as p where p.postTitle like '%待解决%' and post_status='publish' order by p.id desc").list();
		//logger.info(postsList.size());
		tran.commit();
		unf = false;
		//依次查找每一篇未完成的文章。重新搜索
		for(WpPosts unfpost:postsList){
			if(unf) break;
			tran = HibernateSessionFactory.openCurrentSession().beginTransaction();
			String[] titles = unfpost.getPostTitle().split(" ");
			String key2 = titles[1];
		
			if(titles[1].length() >= 4)
				key2 = titles[1].substring(0,4);
			Main.proData = HojUtil.getPorblemStr(key2
					, false);
			try{
				int proNum = Integer.parseInt(key2);
				int start = Integer.parseInt(from);
				if(proNum > start) continue;
			}catch (Exception e) {
				logger.info("题目出错：" + unfpost.getPostTitle());
				
				continue;
			}
			logger.info("开始getUnfinished: "+ unfpost.getId() + "  :" + unfpost.getPostTitle());
			String[] keys = new String[]{titles[0].toLowerCase(),key2};
			WpPosts finalPost = Main.getFinalPost(keys);
			
		/*	//找到关键词后开始搜索查找
			List<WpPosts> posts = new ArrayList<WpPosts>();
			
				Main.find(keys, posts);
			if(posts.size()  < 2){
				keys[0] = Main.ojtypebak;
				Main.find(keys,posts);
			}
			WpPosts finalPost = null;
			try {
				//没有找到则 搜索题目名称开始查找
				if(posts.size() == 0){
					Main.find(new String[]{ Main.proData.title.toLowerCase()}, posts);
					for(int i=0; i< Main.ojs.length && posts.size() < 1; i++){
						Main.find(new String[]{ Main.proData.title.toLowerCase() , Main.ojs[i] }, posts);
					}
				}
				finalPost = null;
				int maxPower = -1000;
				for(WpPosts post:posts){
					if(post.power > maxPower){
						maxPower = post.power;
						finalPost = post;
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info("查找失败，返回！");
				e.printStackTrace();
				return;
			}*/
			//logger.info("final post info:");
			if(finalPost != null){
				String mainKeyWord = Main.setMainTerms(finalPost);
				String keyWordInTitle = mainKeyWord == null ? "": "-"+mainKeyWord+"-";
				unfpost.setPostTitle(Main.ojtype +" " + keys[1] + " " + Main.proData.title + keyWordInTitle + "[解题报告]" + Main.ojtypebak);
				unfpost.setPostContent(unfpost.getPostContent()+ "<br>\n" + Main.getText(finalPost,false));
				//unfpost.setTerms(finalPost.getTerms());
//				if(finalPost.getTerms() != null && finalPost.getTerms().size() > 0){
//					for(WpTermTaxonomy term:finalPost.getTerms()){
//						unfpost.getTerms().add(term);
//					}
//				}
logger.info("查找待解决题目成功:" + unfpost.getPostTitle());
				
				wpdao.save(unfpost);
			}
			tran.commit();
		}
		unf = true;
		//tran.commit();
	}
}
