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
 * WpLinks entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.model.WpLinks
 * @author MyEclipse Persistence Tools
 */

public class WpLinksDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(WpLinksDAO.class);
	// property constants
	public static final String LINK_URL = "linkUrl";
	public static final String LINK_NAME = "linkName";
	public static final String LINK_IMAGE = "linkImage";
	public static final String LINK_TARGET = "linkTarget";
	public static final String LINK_DESCRIPTION = "linkDescription";
	public static final String LINK_VISIBLE = "linkVisible";
	public static final String LINK_OWNER = "linkOwner";
	public static final String LINK_RATING = "linkRating";
	public static final String LINK_REL = "linkRel";
	public static final String LINK_NOTES = "linkNotes";
	public static final String LINK_RSS = "linkRss";

	public void save(WpLinks transientInstance) {
		log.debug("saving WpLinks instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WpLinks persistentInstance) {
		log.debug("deleting WpLinks instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WpLinks findById(Long id) {
		log.debug("getting WpLinks instance with id: " + id);
		try {
			WpLinks instance = (WpLinks) getSession().get("com.model.WpLinks",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WpLinks instance) {
		log.debug("finding WpLinks instance by example");
		try {
			List results = getSession().createCriteria("com.model.WpLinks")
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
		log.debug("finding WpLinks instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from WpLinks as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByLinkUrl(Object linkUrl) {
		return findByProperty(LINK_URL, linkUrl);
	}

	public List findByLinkName(Object linkName) {
		return findByProperty(LINK_NAME, linkName);
	}

	public List findByLinkImage(Object linkImage) {
		return findByProperty(LINK_IMAGE, linkImage);
	}

	public List findByLinkTarget(Object linkTarget) {
		return findByProperty(LINK_TARGET, linkTarget);
	}

	public List findByLinkDescription(Object linkDescription) {
		return findByProperty(LINK_DESCRIPTION, linkDescription);
	}

	public List findByLinkVisible(Object linkVisible) {
		return findByProperty(LINK_VISIBLE, linkVisible);
	}

	public List findByLinkOwner(Object linkOwner) {
		return findByProperty(LINK_OWNER, linkOwner);
	}

	public List findByLinkRating(Object linkRating) {
		return findByProperty(LINK_RATING, linkRating);
	}

	public List findByLinkRel(Object linkRel) {
		return findByProperty(LINK_REL, linkRel);
	}

	public List findByLinkNotes(Object linkNotes) {
		return findByProperty(LINK_NOTES, linkNotes);
	}

	public List findByLinkRss(Object linkRss) {
		return findByProperty(LINK_RSS, linkRss);
	}

	public List findAll() {
		log.debug("finding all WpLinks instances");
		try {
			String queryString = "from WpLinks";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WpLinks merge(WpLinks detachedInstance) {
		log.debug("merging WpLinks instance");
		try {
			WpLinks result = (WpLinks) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WpLinks instance) {
		log.debug("attaching dirty WpLinks instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WpLinks instance) {
		log.debug("attaching clean WpLinks instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}