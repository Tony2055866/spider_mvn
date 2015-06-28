package com.model;

import com.util.HibernateSessionFactory;
import org.hibernate.Session;


/**
 * Data access object (DAO) for domain model
 * @author MyEclipse Persistence Tools
 */
public class BaseHibernateDAO implements IBaseHibernateDAO {
	
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}
	public Session openSession() {
		return HibernateSessionFactory.openSession();
	}
	public Session openCurrentSession() {
		return HibernateSessionFactory.getSessionFactory().getCurrentSession();
	}
	
}