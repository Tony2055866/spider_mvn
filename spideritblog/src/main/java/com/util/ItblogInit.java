package com.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.dao.TermDao;
import com.model.WpTermTaxonomy;

public class ItblogInit {
	public static Map<String,WpTermTaxonomy> catTermTaxMap = new HashMap<String,WpTermTaxonomy>();
	public static Map<String,WpTermTaxonomy> tagTermTaxMap = new HashMap<String,WpTermTaxonomy>();
	
	public static WpTermTaxonomy defautTerm;
	public static WpTermTaxonomy Term_other;
	//public static WpTermTaxonomy Term_Android,Term_Other,Term_Java;
	public static long userId = 1;
	public static Set<String> catKeySet;
	public static Set<String> tagKeySet;
	public static boolean isWindows;
	public static String host = "http://localhost/itong/";
//	public static String host = "http://www.acmerblog.com";
	
	public static String  imgbaseDownLoad = "D:\\PHP\\itblog\\img";
	public static String logDir="D:\\PHP\\wordpress-3.7-zh_CN\\wordpress\\spiderlog";
	public static String hostPath = "D:\\PHP\\itblog\\";
	public static boolean inited = false;
	
	public static int ojTestUser = 3;
	public static int ojSpiderUserId = 3;
	public static boolean local = true;
	public static void init(){
		init(false);
	}
	/**
	 * 
	 * @param noload true 则不加载数据库信息
	 */
	static Logger logger = Logger.getLogger(ItblogInit.class);
	public static void init(boolean noload){
		if(inited) return;
			if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
				isWindows = true;
			} else {
				isWindows = false;
				imgbaseDownLoad = "/server/itblog/img/";
				host = "http://www.51itong.net/";
				logDir = "/server/logs/itblog";
				hostPath = "/server/itblog/";
				local = false;
			}
		if(noload) return;
		inited = true;
		 TermDao cateDao = new TermDao();
		 List<WpTermTaxonomy> catTerms = cateDao.getCateTerms();
		 for(WpTermTaxonomy term:catTerms){
			String des = term.getDescription();
			des = des.trim().toLowerCase();
			catTermTaxMap.put(term.getTerm().getName(), term);
			if(des != null && des != ""){
				String keyArr[] = des.split(";");
				for(int i=0; i<keyArr.length; i++){
					catTermTaxMap.put(keyArr[i], term);
					if(keyArr[i].equals("其它")){
						defautTerm = term;
						Term_other = term;
					}
					//System.out.println(keyArr[i] + " -> " + term.getDescription());
				}
			}
		 }
		 System.out.println("---------");
		 List<WpTermTaxonomy> tagTerms = cateDao.getTagTerms();
		 System.out.println(tagTerms.size());

		 for(WpTermTaxonomy term:tagTerms){
			 tagTermTaxMap.put(term.getTerm().getName(), term);
				String des = term.getDescription();
//				System.out.println(term.getDescription());
				des = des.trim().toLowerCase();
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
		logger.info("inited ! catKeySet:" + catKeySet);

	}
	
	public static void main(String[] args) {
		init();
		System.out.println(Term_other);
	}

}
