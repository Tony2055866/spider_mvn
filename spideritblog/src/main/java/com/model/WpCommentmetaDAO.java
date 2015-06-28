package com.model;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * WpCommentmeta entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.model.WpCommentmeta
 * @author MyEclipse Persistence Tools
 */

public class WpCommentmetaDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(WpCommentmetaDAO.class);
	// property constants
	public static final String COMMENT_ID = "commentId";
	public static final String META_KEY = "metaKey";
	public static final String META_VALUE = "metaValue";

	public void save(WpCommentmeta transientInstance) {
		log.debug("saving WpCommentmeta instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WpCommentmeta persistentInstance) {
		log.debug("deleting WpCommentmeta instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WpCommentmeta findById(Long id) {
		log.debug("getting WpCommentmeta instance with id: " + id);
		try {
			WpCommentmeta instance = (WpCommentmeta) getSession().get(
					"com.model.WpCommentmeta", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WpCommentmeta instance) {
		log.debug("finding WpCommentmeta instance by example");
		try {
			List results = getSession().createCriteria(
					"com.model.WpCommentmeta").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding WpCommentmeta instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from WpCommentmeta as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCommentId(Object commentId) {
		return findByProperty(COMMENT_ID, commentId);
	}

	public List findByMetaKey(Object metaKey) {
		return findByProperty(META_KEY, metaKey);
	}

	public List findByMetaValue(Object metaValue) {
		return findByProperty(META_VALUE, metaValue);
	}

	public List findAll() {
		log.debug("finding all WpCommentmeta instances");
		try {
			String queryString = "from WpCommentmeta";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WpCommentmeta merge(WpCommentmeta detachedInstance) {
		log.debug("merging WpCommentmeta instance");
		try {
			WpCommentmeta result = (WpCommentmeta) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WpCommentmeta instance) {
		log.debug("attaching dirty WpCommentmeta instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WpCommentmeta instance) {
		log.debug("attaching clean WpCommentmeta instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}