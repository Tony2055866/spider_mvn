package com.model;

import com.util.HibernateSessionFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for
 * WpPosts entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.model.WpPosts
 * @author MyEclipse Persistence Tools
 */

public class WpPostsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(WpPostsDAO.class);
	// property constants
	public static final String POST_AUTHOR = "postAuthor";
	public static final String POST_CONTENT = "postContent";
	public static final String POST_TITLE = "postTitle";
	public static final String POST_EXCERPT = "postExcerpt";
	public static final String POST_STATUS = "postStatus";
	public static final String COMMENT_STATUS = "commentStatus";
	public static final String PING_STATUS = "pingStatus";
	public static final String POST_PASSWORD = "postPassword";
	public static final String POST_NAME = "postName";
	public static final String TO_PING = "toPing";
	public static final String PINGED = "pinged";
	public static final String POST_CONTENT_FILTERED = "postContentFiltered";
	public static final String POST_PARENT = "postParent";
	public static final String GUID = "guid";
	public static final String MENU_ORDER = "menuOrder";
	public static final String POST_TYPE = "postType";
	public static final String POST_MIME_TYPE = "postMimeType";
	public static final String COMMENT_COUNT = "commentCount";

	public void save(WpPosts transientInstance) {
		log.debug("saving WpPosts instance:" + openCurrentSession().hashCode());
		//System.out.println("saving WpPosts instance:" + openCurrentSession().hashCode());
		try {
//			Session session = openSession();
//			//getSession().saveOrUpdate(getSession().merge( transientInstance) );
//			session.saveOrUpdate(transientInstance );
//			session.flush();
//			session.clear();
//			session.close();
			Transaction tran = HibernateSessionFactory.getSessionFactory().getCurrentSession().beginTransaction();
			HibernateSessionFactory.getSessionFactory().getCurrentSession().saveOrUpdate( transientInstance );
				//tran.commit();
				//openCurrentSession().close();
			
			log.debug("save successful");
			
			WpTermTaxonomyDAO ttDao = new WpTermTaxonomyDAO();
			if(transientInstance.getTerms() == null) return;
			for(WpTermTaxonomy tt:transientInstance.getTerms()){
				tt.setCount(tt.getCount()+1L);
				ttDao.save(tt);
			}
			
			tran.commit();
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}


	public void save2(WpPosts transientInstance) {
		log.debug("saving WpPosts instance:" + openCurrentSession().hashCode());
		try {

			Transaction tran = HibernateSessionFactory.getSessionFactory().getCurrentSession().beginTransaction();
			HibernateSessionFactory.getSessionFactory().getCurrentSession().saveOrUpdate( transientInstance );
			//tran.commit();
			//openCurrentSession().close();
			log.debug("save successful");
			tran.commit();
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WpPosts persistentInstance) {
		log.debug("deleting WpPosts instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
			
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WpPosts findById(Long id) {
		log.debug("getting WpPosts instance with id: " + id);
		try {
			WpPosts instance = (WpPosts) getSession().get("com.model.WpPosts",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WpPosts instance) {
		log.debug("finding WpPosts instance by example");
		try {
			List results = getSession().createCriteria("com.model.WpPosts")
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
		log.debug("finding WpPosts instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from WpPosts as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPostAuthor(Object postAuthor) {
		return findByProperty(POST_AUTHOR, postAuthor);
	}

	public List findByPostContent(Object postContent) {
		return findByProperty(POST_CONTENT, postContent);
	}

	public List findByPostTitle(Object postTitle) {
		return findByProperty(POST_TITLE, postTitle);
	}

	public List findByPostExcerpt(Object postExcerpt) {
		return findByProperty(POST_EXCERPT, postExcerpt);
	}

	public List findByPostStatus(Object postStatus) {
		return findByProperty(POST_STATUS, postStatus);
	}

	public List findByCommentStatus(Object commentStatus) {
		return findByProperty(COMMENT_STATUS, commentStatus);
	}

	public List findByPingStatus(Object pingStatus) {
		return findByProperty(PING_STATUS, pingStatus);
	}

	public List findByPostPassword(Object postPassword) {
		return findByProperty(POST_PASSWORD, postPassword);
	}

	public List findByPostName(Object postName) {
		return findByProperty(POST_NAME, postName);
	}

	public List findByToPing(Object toPing) {
		return findByProperty(TO_PING, toPing);
	}

	public List findByPinged(Object pinged) {
		return findByProperty(PINGED, pinged);
	}

	public List findByPostContentFiltered(Object postContentFiltered) {
		return findByProperty(POST_CONTENT_FILTERED, postContentFiltered);
	}

	public List findByPostParent(Object postParent) {
		return findByProperty(POST_PARENT, postParent);
	}

	public List findByGuid(Object guid) {
		return findByProperty(GUID, guid);
	}

	public List findByMenuOrder(Object menuOrder) {
		return findByProperty(MENU_ORDER, menuOrder);
	}

	public List findByPostType(Object postType) {
		return findByProperty(POST_TYPE, postType);
	}

	public List findByPostMimeType(Object postMimeType) {
		return findByProperty(POST_MIME_TYPE, postMimeType);
	}

	public List findByCommentCount(Object commentCount) {
		return findByProperty(COMMENT_COUNT, commentCount);
	}

	public List findAll() {
		log.debug("finding all WpPosts instances");
		try {
			String queryString = "from WpPosts";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WpPosts merge(WpPosts detachedInstance) {
		log.debug("merging WpPosts instance");
		try {
			WpPosts result = (WpPosts) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WpPosts instance) {
		log.debug("attaching dirty WpPosts instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WpPosts instance) {
		log.debug("attaching clean WpPosts instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}