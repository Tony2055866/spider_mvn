package com.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Transaction;

import com.model.WpTermTaxonomy;
import com.model.WpTermTaxonomyDAO;
import com.model.WpTerms;
import com.model.WpTermsDAO;
import com.util.HibernateSessionFactory;


public class TermDao {

	public static List<WpTermTaxonomy> list1,list2;

	public static List<WpTermTaxonomy> getCateTerms(){
		
		if(list1 != null) return list1;
		Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();

		WpTermTaxonomyDAO wtdao = new WpTermTaxonomyDAO();
		WpTermsDAO termDao = new WpTermsDAO();
		List<WpTermTaxonomy> list1 = wtdao.findByTaxonomy("category");
		//System.out.println(list.size());
		//List<WpTerms> cateTerms = new ArrayList<WpTerms>(list.size());
		//for(int i=0; i<list.size(); i++){
//			WpTermTaxonomy taxonomy = list.get(i);
//			WpTerms term = termDao.findById(taxonomy.getTermId());
//			
//			try {
//				String flug = URLDecoder.decode(term.getSlug(), "utf-8");
//				term.setSlug(flug);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			cateTerms.add(term);
			
			//System.out.println(term.getName());
		//}
		
		tran.commit();
		return list1;
	}
	
	public static List<WpTermTaxonomy> getTagTerms(){
		if(list2 != null) return list2;
		Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();

		WpTermTaxonomyDAO wtdao = new WpTermTaxonomyDAO();
		WpTermsDAO termDao = new WpTermsDAO();
		List<WpTermTaxonomy> list2 = wtdao.findByTaxonomy("post_tag");
		//List<WpTerms> cateTerms = new ArrayList<WpTerms>(list.size());
		tran.commit();
		return list2;
	}
	
	
	
//	public
	public static void main(String[] args) {
		
	}
	
	
}
