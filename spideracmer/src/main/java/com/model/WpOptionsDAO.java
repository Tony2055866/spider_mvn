package com.model;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for
 * WpOptions entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.model.WpOptions
 * @author MyEclipse Persistence Tools
 */

public class WpOptionsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(WpOptionsDAO.class);
	// property constants
	public static final String OPTION_NAME = "optionName";
	public static final String OPTION_VALUE = "optionValue";
	public static final String AUTOLOAD = "autoload";

	public void save(WpOptions transientInstance) {
		log.debug("saving WpOptions instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WpOptions persistentInstance) {
		log.debug("deleting WpOptions instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WpOptions findById(Long id) {
		log.debug("getting WpOptions instance with id: " + id);
		try {
			WpOptions instance = (WpOptions) getSession().get(
					"com.model.WpOptions", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WpOptions instance) {
		log.debug("finding WpOptions instance by example");
		try {
			List results = getSession().createCriteria("com.model.WpOptions")
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
		log.debug("finding WpOptions instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from WpOptions as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByOptionName(Object optionName) {
		return findByProperty(OPTION_NAME, optionName);
	}

	public List findByOptionValue(Object optionValue) {
		return findByProperty(OPTION_VALUE, optionValue);
	}

	public List findByAutoload(Object autoload) {
		return findByProperty(AUTOLOAD, autoload);
	}

	public List findAll() {
		log.debug("finding all WpOptions instances");
		try {
			String queryString = "from WpOptions";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WpOptions merge(WpOptions detachedInstance) {
		log.debug("merging WpOptions instance");
		try {
			WpOptions result = (WpOptions) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WpOptions instance) {
		log.debug("attaching dirty WpOptions instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WpOptions instance) {
		log.debug("attaching clean WpOptions instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}