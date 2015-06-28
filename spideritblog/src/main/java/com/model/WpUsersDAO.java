package com.model;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * WpUsers entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.model.WpUsers
 * @author MyEclipse Persistence Tools
 */

public class WpUsersDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(WpUsersDAO.class);
	// property constants
	public static final String USER_LOGIN = "userLogin";
	public static final String USER_PASS = "userPass";
	public static final String USER_NICENAME = "userNicename";
	public static final String USER_EMAIL = "userEmail";
	public static final String USER_URL = "userUrl";
	public static final String USER_ACTIVATION_KEY = "userActivationKey";
	public static final String USER_STATUS = "userStatus";
	public static final String DISPLAY_NAME = "displayName";

	public void save(WpUsers transientInstance) {
		log.debug("saving WpUsers instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WpUsers persistentInstance) {
		log.debug("deleting WpUsers instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WpUsers findById(Long id) {
		log.debug("getting WpUsers instance with id: " + id);
		try {
			WpUsers instance = (WpUsers) getSession().get("com.model.WpUsers",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WpUsers instance) {
		log.debug("finding WpUsers instance by example");
		try {
			List results = getSession().createCriteria("com.model.WpUsers")
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
		log.debug("finding WpUsers instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from WpUsers as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUserLogin(Object userLogin) {
		return findByProperty(USER_LOGIN, userLogin);
	}

	public List findByUserPass(Object userPass) {
		return findByProperty(USER_PASS, userPass);
	}

	public List findByUserNicename(Object userNicename) {
		return findByProperty(USER_NICENAME, userNicename);
	}

	public List findByUserEmail(Object userEmail) {
		return findByProperty(USER_EMAIL, userEmail);
	}

	public List findByUserUrl(Object userUrl) {
		return findByProperty(USER_URL, userUrl);
	}

	public List findByUserActivationKey(Object userActivationKey) {
		return findByProperty(USER_ACTIVATION_KEY, userActivationKey);
	}

	public List findByUserStatus(Object userStatus) {
		return findByProperty(USER_STATUS, userStatus);
	}

	public List findByDisplayName(Object displayName) {
		return findByProperty(DISPLAY_NAME, displayName);
	}

	public List findAll() {
		log.debug("finding all WpUsers instances");
		try {
			String queryString = "from WpUsers";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WpUsers merge(WpUsers detachedInstance) {
		log.debug("merging WpUsers instance");
		try {
			WpUsers result = (WpUsers) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WpUsers instance) {
		log.debug("attaching dirty WpUsers instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WpUsers instance) {
		log.debug("attaching clean WpUsers instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}