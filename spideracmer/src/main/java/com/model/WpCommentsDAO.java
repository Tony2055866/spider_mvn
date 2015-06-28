package com.model;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for
 * WpComments entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.model.WpComments
 * @author MyEclipse Persistence Tools
 */

public class WpCommentsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(WpCommentsDAO.class);
	// property constants
	public static final String COMMENT_POST_ID = "commentPostId";
	public static final String COMMENT_AUTHOR = "commentAuthor";
	public static final String COMMENT_AUTHOR_EMAIL = "commentAuthorEmail";
	public static final String COMMENT_AUTHOR_URL = "commentAuthorUrl";
	public static final String COMMENT_AUTHOR_IP = "commentAuthorIp";
	public static final String COMMENT_CONTENT = "commentContent";
	public static final String COMMENT_KARMA = "commentKarma";
	public static final String COMMENT_APPROVED = "commentApproved";
	public static final String COMMENT_AGENT = "commentAgent";
	public static final String COMMENT_TYPE = "commentType";
	public static final String COMMENT_PARENT = "commentParent";
	public static final String USER_ID = "userId";

	public void save(WpComments transientInstance) {
		log.debug("saving WpComments instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WpComments persistentInstance) {
		log.debug("deleting WpComments instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WpComments findById(Long id) {
		log.debug("getting WpComments instance with id: " + id);
		try {
			WpComments instance = (WpComments) getSession().get(
					"com.model.WpComments", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WpComments instance) {
		log.debug("finding WpComments instance by example");
		try {
			List results = getSession().createCriteria("com.model.WpComments")
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
		log.debug("finding WpComments instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from WpComments as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCommentPostId(Object commentPostId) {
		return findByProperty(COMMENT_POST_ID, commentPostId);
	}

	public List findByCommentAuthor(Object commentAuthor) {
		return findByProperty(COMMENT_AUTHOR, commentAuthor);
	}

	public List findByCommentAuthorEmail(Object commentAuthorEmail) {
		return findByProperty(COMMENT_AUTHOR_EMAIL, commentAuthorEmail);
	}

	public List findByCommentAuthorUrl(Object commentAuthorUrl) {
		return findByProperty(COMMENT_AUTHOR_URL, commentAuthorUrl);
	}

	public List findByCommentAuthorIp(Object commentAuthorIp) {
		return findByProperty(COMMENT_AUTHOR_IP, commentAuthorIp);
	}

	public List findByCommentContent(Object commentContent) {
		return findByProperty(COMMENT_CONTENT, commentContent);
	}

	public List findByCommentKarma(Object commentKarma) {
		return findByProperty(COMMENT_KARMA, commentKarma);
	}

	public List findByCommentApproved(Object commentApproved) {
		return findByProperty(COMMENT_APPROVED, commentApproved);
	}

	public List findByCommentAgent(Object commentAgent) {
		return findByProperty(COMMENT_AGENT, commentAgent);
	}

	public List findByCommentType(Object commentType) {
		return findByProperty(COMMENT_TYPE, commentType);
	}

	public List findByCommentParent(Object commentParent) {
		return findByProperty(COMMENT_PARENT, commentParent);
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findAll() {
		log.debug("finding all WpComments instances");
		try {
			String queryString = "from WpComments";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WpComments merge(WpComments detachedInstance) {
		log.debug("merging WpComments instance");
		try {
			WpComments result = (WpComments) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WpComments instance) {
		log.debug("attaching dirty WpComments instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WpComments instance) {
		log.debug("attaching clean WpComments instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}