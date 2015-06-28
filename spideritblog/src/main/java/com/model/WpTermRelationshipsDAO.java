package com.model;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * WpTermRelationships entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.model.WpTermRelationships
 * @author MyEclipse Persistence Tools
 */

public class WpTermRelationshipsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(WpTermRelationshipsDAO.class);
	// property constants
	public static final String TERM_ORDER = "termOrder";

	public void save(WpTermRelationships transientInstance) {
		log.debug("saving WpTermRelationships instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WpTermRelationships persistentInstance) {
		log.debug("deleting WpTermRelationships instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WpTermRelationships findById(com.model.WpTermRelationshipsId id) {
		log.debug("getting WpTermRelationships instance with id: " + id);
		try {
			WpTermRelationships instance = (WpTermRelationships) getSession()
					.get("com.model.WpTermRelationships", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WpTermRelationships instance) {
		log.debug("finding WpTermRelationships instance by example");
		try {
			List results = getSession().createCriteria(
					"com.model.WpTermRelationships").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding WpTermRelationships instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from WpTermRelationships as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTermOrder(Object termOrder) {
		return findByProperty(TERM_ORDER, termOrder);
	}

	public List findAll() {
		log.debug("finding all WpTermRelationships instances");
		try {
			String queryString = "from WpTermRelationships";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WpTermRelationships merge(WpTermRelationships detachedInstance) {
		log.debug("merging WpTermRelationships instance");
		try {
			WpTermRelationships result = (WpTermRelationships) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WpTermRelationships instance) {
		log.debug("attaching dirty WpTermRelationships instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WpTermRelationships instance) {
		log.debug("attaching clean WpTermRelationships instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}