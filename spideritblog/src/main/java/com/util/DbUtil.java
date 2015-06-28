package com.util;


import java.util.List;

import com.model.Log;
import org.apache.log4j.spi.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.model.LogDAO;
import org.slf4j.Logger;


public class DbUtil {
	static Logger logger = org.slf4j.LoggerFactory.getLogger(DbUtil.class);
	/**
	 * 
	 * @param link
	 * @return 有该link返回true,没有的话返回false
	 */
	public static boolean checkUrl(String link) {
		LogDAO logDAO = new LogDAO();
		Session session = HibernateSessionFactory.openCurrentSession();
		Transaction tran = session.beginTransaction();
		List list = logDAO.findByProperty("url", link.trim());
		tran.commit();
		logger.info("checkUrl  : " + link + " , result : " + list);

		if(null == list || list.size() == 0) return false;
		return true;
	}

	public static boolean checkTitle(String title,String host) {
		logger.info("checkTitle  : " + title + " , " + host);
		LogDAO logDAO = new LogDAO();
		Session session = HibernateSessionFactory.openCurrentSession();
		Transaction tran = session.beginTransaction();
		List list = logDAO.findByProperty("name", title + ";" + host);
		tran.commit();
		if(null == list || list.size() == 0) return false;
		return true;
	}

	public static String getParsedUrl(String url) {
		LogDAO logDAO = new LogDAO();
		Session session = HibernateSessionFactory.openCurrentSession();
		Transaction tran = session.beginTransaction();
		List<Log> list = logDAO.findByProperty("url", url.trim());
		tran.commit();
		if(null == list || list.size() == 0) return null;
		return list.get(0).getOther();
	}
}
