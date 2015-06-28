package com.model;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for
 * WpTerms entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.model.WpTerms
 * @author MyEclipse Persistence Tools
 */

public class WpTermsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(WpTermsDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String SLUG = "slug";
	public static final String TERM_GROUP = "termGroup";

	public void save(WpTerms transientInstance) {
		log.debug("saving WpTerms instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WpTerms persistentInstance) {
		log.debug("deleting WpTerms instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WpTerms findById(Long id) {
		log.debug("getting WpTerms instance with id: " + id);
		try {
			WpTerms instance = (WpTerms) getSession().get("com.model.WpTerms",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WpTerms instance) {
		log.debug("finding WpTerms instance by example");
		try {
			List results = getSession().createCriteria("com.model.WpTerms")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding WpTerms instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from WpTerms as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findBySlug(Object slug) {
		return findByProperty(SLUG, slug);
	}

	public List findByTermGroup(Object termGroup) {
		return findByProperty(TERM_GROUP, termGroup);
	}

	public List findAll() {
		log.debug("finding all WpTerms instances");
		try {
			String queryString = "from WpTerms";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WpTerms merge(WpTerms detachedInstance) {
		log.debug("merging WpTerms instance");
		try {
			WpTerms result = (WpTerms) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WpTerms instance) {
		log.debug("attaching dirty WpTerms instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WpTerms instance) {
		log.debug("attaching clean WpTerms instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}