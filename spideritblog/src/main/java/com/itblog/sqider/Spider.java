package com.itblog.sqider;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.main.Main;
import com.model.WpPosts;
import com.model.WpTermTaxonomy;
import com.util.ItblogInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Spider {

	private static Logger logger = LoggerFactory.getLogger(Spider.class);

	public abstract WpPosts parseArticleSUrl(PageData page, String[] searchKeys);
	
	public abstract WpPosts getArticleSUrl(PageData page);
	
	public static void main(String[] args) {
//		logger.info("HDU .1310".matches("HDU" + "[\\s-,.]*" + "1310") );
	}
	public WpPosts parseArticleSUrl(PageData page, String[] searchKeys,
			boolean isAddTag) {
		return parseArticleSUrl(page, searchKeys, true);
	}
	/**
	 * 
	 * @param title 要检测的标题
	 * @param searchKeys 根据关键词 检测 该标题是否符合要求
	 * @return
	 */
	boolean rightTitle(String title,String[] searchKeys){
		title = title.toLowerCase();
		if(searchKeys == null) return true;
		if(searchKeys.length == 2){
			boolean test = title.matches(searchKeys[0] + "[\\s-,.]*" + searchKeys[1]);
			if(test) return test;
			else
			return ( title.contains(searchKeys[0] + searchKeys[1]) ||  title.contains(searchKeys[0] + " " + searchKeys[1]) 
					||title.contains(searchKeys[0] + "-" + searchKeys[1]) ||title.contains(searchKeys[0] + "," + searchKeys[1]) 
					|| title.contains(searchKeys[0] + " - " + searchKeys[1])
					|| title.contains(searchKeys[0] + "- " + searchKeys[1]) 
					|| title.contains(searchKeys[0] + " -" + searchKeys[1])
					|| title.contains(searchKeys[0] + "  " + searchKeys[1]));
		}else{
			if( title.contains(searchKeys[0].toLowerCase()) ){
				for(int i=0; i<Main.ojs.length; i++){
					if(title.contains(Main.ojs[i])) return true;
				}
			}
			return false;
//			boolean f = (title.contains(searchKeys[0].toLowerCase()) && ( 
//					title.contains("poj")  ||
//			  title.contains("uva") ||
//			  title.contains("zoj") ) ||
//			  title.contains("pku") );
//			//logger.info("test rightTitle:" + title + "  <>  " + searchKeys[0]);
//			//logger.info(f);
//			return f;
		}
		
	}
	
	/**
	 * 
	 * @param keys 在文章中找到的一下关键词列表
	 * @param title 文章的标题
	 * @param content 文章的内容
	 * @param keyCnt 记录关键词的 权值大小
	 * @return 返回文章最终的分类 列表
	 */
	public Set<WpTermTaxonomy> getMatchKeys(List<String> keys,String title,String content,Map<WpTermTaxonomy, Integer> keyCnt ){
		
		Set<WpTermTaxonomy> set = new HashSet<WpTermTaxonomy>();
		
		for(String key: ItblogInit.catKeySet){
			//logger.info("match key:" + key);
			for(String contentKey:keys){
				if(contentKey.toLowerCase().contains(key)){
					addKey(keyCnt,  key,2);
				}
			}
			if(title.toLowerCase().contains(key)){
				addKey(keyCnt, key, 5);
			}
			
			content = content.toLowerCase();
			
			if(!key.equals("sort")){
				if(key.equals("dp")){
					if(content.contains("dp")){
						addKey(keyCnt,  key, 1);
					}
					if(content.contains("dp[")){
							addKey(keyCnt,  key, 3);
					}
				}else
					if(content.contains(key))
					addKey(keyCnt,  key, 1);
			}
		}
		
		for(String key: ItblogInit.tagKeySet){
			
			for(String contentKey:keys){
				if(contentKey.toLowerCase().contains(key)){
					addKey2(keyCnt,  key, 8);
				}
			}
			if(title.toLowerCase().contains(key)){
				addKey2(keyCnt,  key, 12);
			}
			content = content.toLowerCase();
			
			if(!key.equals("sort")){
				if(key.equals("dp")){
					if(content.contains("dp")){
						addKey2(keyCnt,  key, 4);
					}
					if(content.contains("dp[")){
							addKey2(keyCnt,  key, 8);
					}
				}else
					if(content.contains(key))
					addKey2(keyCnt,  key, 8);
			}
		}
	
		return set;
	}
	public void addKey2(Map<WpTermTaxonomy, Integer> keyCnt,
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

	public void addKey(Map<WpTermTaxonomy, Integer> keyCnt,
		 String key, int v) {
		
		WpTermTaxonomy tax = ItblogInit.catTermTaxMap.get(key);
		//if(tax == null) tax = Init.tagTermTaxMap.get(key);
		if(tax == null ) return;
		//set.add(tax);
		if(keyCnt.containsKey(tax)){
			keyCnt.put(tax, keyCnt.get(tax)+v);
		}else{
			keyCnt.put(tax, v);
		}
	}
	public int checkCodePower(WpPosts post){
		int cnt = 0;
		int codecnt = 0;
		if(post == null) return 0;
		for(Content con:post.listContent){
			if(con == null ) continue;
			if(con.text == null) continue;
			if(con.isCode){
				if( con.lang.endsWith("cpp") ){
					if(!con.text.contains("scanf") && !con.text.contains("cin"))
						cnt -= 10; //如果没有输入输出，说明不是正确的代码，减分！！
				}
			}else {
				if(con.text.contains("题目大意")) cnt +=20; //有此内容加分！！
				if(con.text.contains("题目分析")) cnt +=20;
				if(con.text.contains("解题思路")) cnt +=20;
				if(con.text.contains("题意") || con.text.contains("分析")) cnt +=20;
				codecnt++;
			}
		}
		if(codecnt > 2) cnt -= 30;
		if(post.getPostTitle() != null)
		cnt -= post.getPostTitle().length(); //题目长，减分！
		return cnt;
	}

	
	
}
