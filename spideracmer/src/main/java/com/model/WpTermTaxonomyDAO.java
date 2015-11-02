package com.model;

import com.util.HibernateSessionFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for
 * WpTermTaxonomy entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.model.WpTermTaxonomy
 * @author MyEclipse Persistence Tools
 */

public class WpTermTaxonomyDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(WpTermTaxonomyDAO.class);
	// property constants
	public static final String TERM_ID = "termId";
	public static final String TAXONOMY = "taxonomy";
	public static final String DESCRIPTION = "description";
	public static final String PARENT = "parent";
	public static final String COUNT = "count";

	public void save(WpTermTaxonomy transientInstance) {
		//logger.info("saving WpTermTaxonomy instance:" + openCurrentSession().hashCode());
		try {
//			Session session = openSession();
//			session.saveOrUpdate( transientInstance );
//			session.flush();
//			session.clear();
//			session.close();
			//getSession().saveOrUpdate( transientInstance );
		//	HibernateSessionFactory.closeSession();
			
			openCurrentSession().saveOrUpdate(transientInstance);
			
			//openCurrentSession().close();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WpTermTaxonomy persistentInstance) {
		log.debug("deleting WpTermTaxonomy instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WpTermTaxonomy findById(Long id) {
		log.debug("getting WpTermTaxonomy instance with id: " + id);
		try {
			
			WpTermTaxonomy instance = (WpTermTaxonomy) openCurrentSession().get(
					"com.model.WpTermTaxonomy", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WpTermTaxonomy instance) {
		log.debug("finding WpTermTaxonomy instance by example");
		try {
			List results = getSession().createCriteria(
					"com.model.WpTermTaxonomy").add(Example.create(instance))
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
		log.debug("finding WpTermTaxonomy instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from WpTermTaxonomy as model where model."
					+ propertyName + "= ?";
			boolean test = false;
			Session session = null;
			if(test){
				 session = HibernateSessionFactory.openSession();
			}else
				session = openCurrentSession();
			//logger.info(session.hashCode());
			Query queryObject = session.createQuery(queryString);
			
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTermId(Object termId) {
		return findByProperty(TERM_ID, termId);
	}

	public List findByTaxonomy(Object taxonomy) {
		return findByProperty(TAXONOMY, taxonomy);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByParent(Object parent) {
		return findByProperty(PARENT, parent);
	}

	public List findByCount(Object count) {
		return findByProperty(COUNT, count);
	}

	public List findAll() {
		log.debug("finding all WpTermTaxonomy instances");
		try {
			String queryString = "from WpTermTaxonomy";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WpTermTaxonomy merge(WpTermTaxonomy detachedInstance) {
		log.debug("merging WpTermTaxonomy instance");
		try {
			WpTermTaxonomy result = (WpTermTaxonomy) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WpTermTaxonomy instance) {
		log.debug("attaching dirty WpTermTaxonomy instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WpTermTaxonomy instance) {
		log.debug("attaching clean WpTermTaxonomy instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List findByTaxonomy(String string, boolean b) {
		return findByProperty(TAXONOMY, string, b);
	}

	public List findByProperty(String propertyName, Object value, boolean b) {
		log.debug("finding WpTermTaxonomy instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from WpTermTaxonomy as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			
			getSession().clear();
			getSession().close();
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}