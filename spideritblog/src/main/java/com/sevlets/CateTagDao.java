package com.sevlets;

import java.util.List;

import org.hibernate.Query;

import com.model.WpTermTaxonomy;
import com.model.WpTermTaxonomyDAO;
import com.model.WpTerms;
import com.model.WpTermsDAO;

/**
 * 关闭session 的获取！
 * @author Administrator
 *
 */
public class CateTagDao {
	public static List<WpTermTaxonomy> listCat;
	public static List<WpTermTaxonomy> listTag;
	
	public static  List<WpTermTaxonomy> getListCat(){
		if(listCat != null) return listCat;
		WpTermTaxonomyDAO wptaxDao = new WpTermTaxonomyDAO();
		listCat = wptaxDao.findByTaxonomy("category",true);
		return listCat;
	}
	
	public static  List<WpTermTaxonomy> getListTag(){
		if(listTag != null) return listTag;
		WpTermTaxonomyDAO wptaxDao = new WpTermTaxonomyDAO();
		listTag = wptaxDao.findByTaxonomy("post_tag",true);
		return listTag;
	}

}
