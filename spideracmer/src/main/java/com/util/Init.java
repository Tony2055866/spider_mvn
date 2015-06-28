package com.util;

import com.dao.TermDao;
import com.model.WpTermTaxonomy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Init {
	public static Map<String,WpTermTaxonomy> catTermTaxMap = new HashMap<String,WpTermTaxonomy>();
	public static Map<String,WpTermTaxonomy> tagTermTaxMap = new HashMap<String,WpTermTaxonomy>();
	public static Set<String> catKeySet;
	public static Set<String> tagKeySet;
	public static boolean isWindows;
	public static String host = "http://localhost/blog/";
//	public static String host = "http://www.acmerblog.com";
	
	public static String  baseDownLoad = "D:\\PHP\\wordpress-3.7-zh_CN\\wordpress\\img\\";
	public static String logDir="D:\\PHP\\wordpress-3.7-zh_CN\\wordpress\\spiderlog";
	private static boolean inited = false;
	public static String ojsourcesDir="E:\\算法\\HDU-ZOJ-POJ-4000AC源码";

	public static int ojTestUser = 1;
	public static void init(){
		init(false);
	}
	
	/**
	 * 
	 * @param noload true 则不加载数据库信息
	 */
	public static void init(boolean noload){
		if(inited) return;
		
			if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
				isWindows = true;
			} else {
				isWindows = false;
				baseDownLoad = "/usr/local/acmerblog/img/";
				host = "http://www.acmerblog.com/";
				logDir = "/usr/local/logs/spiderlog";
				ojsourcesDir = "/usr/local/source";
				ojTestUser = 2;
			}
		if(noload) return;
		inited = true;
		 TermDao cateDao = new TermDao();
		 List<WpTermTaxonomy> catTerms = cateDao.getCateTerms();
		 for(WpTermTaxonomy term:catTerms){
			String des = term.getDescription();
//			System.out.println(term.getDescription());
			des = des.trim().toLowerCase();
			catTermTaxMap.put(term.getTerm().getName(), term);
			if(des != null && des != ""){
				String keyArr[] = des.split(";");
				//System.out.println(term + " -> " + des);
				for(int i=0; i<keyArr.length; i++){
					catTermTaxMap.put(keyArr[i], term);
				}
			}
		 }
		 System.out.println("---------");
		 List<WpTermTaxonomy> tagTerms = cateDao.getTagTerms();
		 System.out.println(tagTerms.size());
		 for(WpTermTaxonomy term:tagTerms){
				String des = term.getDescription();
//				System.out.println(term.getDescription());
				des = des.trim().toLowerCase();
				tagTermTaxMap.put(term.getTerm().getName(), term);
				if(des != null && des != ""){
					String keyArr[] = des.split(";");
					for(int i=0; i<keyArr.length; i++){
						tagTermTaxMap.put(keyArr[i], term);
						//System.out.println(keyArr[i] + " -> " + term.getDescription());
					}
				}
		  }
		 catKeySet = catTermTaxMap.keySet();
		 tagKeySet = tagTermTaxMap.keySet();
	}
	
	public static void main(String[] args) {
		init();
		System.out.println(catTermTaxMap.get("leetcode"));
		System.out.println(tagTermTaxMap.get("leetcode"));
	}

}
