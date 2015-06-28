package com.test;

import com.main.Main;
import com.main.Util;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.util.Init;

public class TestSavePost {
	public static void main(String[] args) {
		
		 WpPosts post= new WpPosts();
			Util.setCommonPost(post, 0);
			post.setPostExcerpt("tttt");
			WpPostsDAO pdao = new WpPostsDAO();
			
			post.setPostName( "article-" + System.currentTimeMillis());
			post.setPostTitle("tttt");
				post.setPostContent("context");
				
			
				pdao.save(post);
				System.out.println("id:" + post.getId());
				//第一步存储结束！
				post.setGuid(Init.host + "/?p=" + post.getId());
				
				//如果搜集标签，则自动搜集标签
				post.getTerms().add(Main.termtaxHojCat);
				post.getTerms().add(Main.termtaxHojTag);
				
				pdao.save(post);
				
				//HibernateSessionFactory.closeSession();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new Thread(){
					public void run(){
						 WpPosts post1= new WpPosts();
							Util.setCommonPost(post1, 0);
							post1.setPostExcerpt("tttt");
							WpPostsDAO pdao = new WpPostsDAO();
							
							post1.setPostName( "article-" + System.currentTimeMillis());
							post1.setPostTitle("tttt");
								post1.setPostContent("context");
								pdao.save(post1);
								//第一步存储结束！
								post1.setGuid(Init.host + "/?p=" + post1.getId());
								
								//如果搜集标签，则自动搜集标签
								post1.getTerms().add(Main.termtaxHojCat);
								post1.getTerms().add(Main.termtaxHojTag);
								
								pdao.save(post1);
								
								WpPosts post2= new WpPosts();
								Util.setCommonPost(post2, 0);
								post2.setPostExcerpt("tttt");
								
								post2.setPostName( "article-" + System.currentTimeMillis());
								post2.setPostTitle("tttt");
									post2.setPostContent("context");
									pdao.save(post2);
									//第一步存储结束！
									post2.setGuid(Init.host + "/?p=" + post2.getId());
									
									//如果搜集标签，则自动搜集标签
									post2.getTerms().add(Main.termtaxHojCat);
									post2.getTerms().add(Main.termtaxHojTag);
									
									pdao.save(post2);
					}
				}.start();
	}
	
	
	
}
