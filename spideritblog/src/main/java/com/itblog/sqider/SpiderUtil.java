package com.itblog.sqider;



import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.model.WpTermTaxonomy;
import com.util.ItblogInit;

public class SpiderUtil {

	/**
	 * 
	 * @param keys 在文章中找到的一下关键词列表
	 * @param title 文章的标题
	 * @param content 文章的内容
	 * @param keyCnt 记录关键词的 权值大小
	 * @return 返回文章最终的分类 列表
	 */
	public static Set<WpTermTaxonomy> getMatchKeys(List<String> keys,String title,String content,Map<WpTermTaxonomy, Integer> keyCnt ){
		
		Set<WpTermTaxonomy> set = new HashSet<WpTermTaxonomy>();
		if(ItblogInit.catKeySet == null) ItblogInit.init();
		for(String key: ItblogInit.catKeySet){
			//logger.info("match key:" + key);
			if(keys != null){
				for(String contentKey:keys){
					if(contentKey.toLowerCase().contains(key)){
						addKey(keyCnt,  key,2);
					}
				}
			}
			
			//分别检测 标题和内容的关键词
			if(title.toLowerCase().contains(key)){
				addKey(keyCnt, key, 5);
			}
			content = content.toLowerCase();
			if(content.contains(key))
			addKey(keyCnt,  key, 1);
		}
		
		for(String key: ItblogInit.tagKeySet){
			if(keys != null){
				for(String contentKey:keys){
					if(contentKey.toLowerCase().contains(key)){
						addKey2(keyCnt,  key, 8);
					}
				}
			}
			//分别检测 标题和内容的关键词
			if(title.toLowerCase().contains(key)){
				addKey2(keyCnt,  key, 12);
			}
			content = content.toLowerCase();
			
			if(content.contains(key))
			addKey2(keyCnt,  key, 8);
		}


		return set;
	}
	public static  void addKey2(Map<WpTermTaxonomy, Integer> keyCnt,
			 String key, int v) {
			WpTermTaxonomy tax = ItblogInit.tagTermTaxMap.get(key);
			if(tax == null ) return;
			//set.add(tax);
			if(keyCnt.containsKey(tax)){
				keyCnt.put(tax, keyCnt.get(tax)+v);
			}else{
				keyCnt.put(tax, v);
			}
		}

	public static void addKey(Map<WpTermTaxonomy, Integer> keyCnt,
		 String key, int v) {
		WpTermTaxonomy tax = ItblogInit.catTermTaxMap.get(key);
		if(tax == null ) return;
		if(keyCnt.containsKey(tax)){
			keyCnt.put(tax, keyCnt.get(tax)+v);
		}else{
			keyCnt.put(tax, v);
		}
	}
}
