package com.test;

import com.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChangeDateTime {

	static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ChangeDateTime.class);


	public static void main(String[] args) {
		
//		testupdateTime();
//		if(true) return;
		Session session = HibernateSessionFactory.sessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		List<Object[]> posts = session.createQuery("select postDate,postDateGmt,postModified,postModifiedGmt,id from WpPosts post where post.postStatus='publish' and " +
		"post.postType='post' order by post.postDate").list();
		tran.commit();
		
		Random random = new Random();
		int cnt = 0;
		
		long lastTime = 0;
		
		for(int i=0; i<posts.size(); i++){
		//	if(cnt++ < 50) continue;
		//	if(cnt ++ > 80) break;
			
			Object[] userData = posts.get(i);
			Timestamp tmp = (Timestamp) (userData[0]);
			logger.info(userData[0]);
			//Timestamp tmp2 = (Timestamp) (userData[2]);
			long postid = (Long)userData[4];
			
			long cur = tmp.getTime();
			if(lastTime == 0){
				lastTime = cur;
				continue;
			}else{
				if( cur - lastTime < (1000 * 60 * 3) ){
					cur = lastTime + (1000 * 60) * (random.nextInt(8) + 3) + random.nextInt(60000);
					lastTime = cur;
				}else {
					lastTime = cur;
					continue;
				}
				//if( cur - lastTime >= (1000 * 60 * 10) ){ //超过十分钟
			}
			
			
			//Timestamp newstamp = new Timestamp(cur);
			//Timestamp newstamp2 = new Timestamp(cur + 8*3600*1000);
			long cur2 = cur +8*3600*1000;
//			
			session = HibernateSessionFactory.sessionFactory.getCurrentSession();
			 tran = session.beginTransaction();
			org.hibernate.Query q = session.createQuery("update WpPosts post set " +
					"post.postDate=?,post.postDateGmt=?,post.postModified=?,post.postModifiedGmt=? " +
					"where post.id=?");
			logger.info(new Date(cur));
			q.setTimestamp(0, new Date(cur));
			q.setTimestamp(1,  new Date(cur2));
			q.setTimestamp(2, new Date(cur));
			q.setTimestamp(3,  new Date(cur2));
			q.setLong(4, postid);
			q.executeUpdate();
			tran.commit();
			logger.info(cur);
			logger.info(cnt  + "  UPDATE: " +  new Timestamp(cur) );
			
		}
		
		
		logger.info(posts.size());
	}

	private static void testupdateTime() {
		
		Session session = HibernateSessionFactory.sessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		org.hibernate.Query q = session.createQuery("update WpPosts post set " +
				"post.postDate=?,post.postDateGmt=?,post.postModified=?,post.postModifiedGmt=? " +
				"where post.id=?");
		long cur = 1383822922830L;
		long cur2 = 1383822922830L;
		q.setTimestamp(0, new Date(cur));
		q.setTimestamp(1,  new Date(cur2));
		q.setTimestamp(2, new Date(cur));
		q.setTimestamp(3,  new Date(cur2));
		q.setLong(4, 731L);
		q.executeUpdate();
		tran.commit();
	}
	
}
