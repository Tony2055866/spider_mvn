package com.itblog.sqider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;

import com.model.WpPosts;
import com.model.WpTermTaxonomy;
import com.util.ItblogInit;
import com.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spider4Baidu extends Spider{

	private static Logger logger = LoggerFactory.getLogger(Spider4Baidu.class);

	static boolean test = true;
	@Override
	public WpPosts getArticleSUrl(PageData page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public WpPosts parseArticleSUrl(PageData page,String[] searchKeys){
		WpPosts post = new WpPosts();
logger.info("Spider4Cnblog 开始解析:" + page.url);
		Map<WpTermTaxonomy, Integer> keyCnt = new HashMap();
		post.host = page.host;
		try {
			String title = getTitle(page);
if(test)
	logger.info("文章标题:" + title);
			if(title == null) return null;
			
			//if( !rightTitle(title,searchKeys)) return null;
			post.setPostTitle(title);
			List<String> keys = getKeys(page);

			Div contentDiv = MyUtil.parseTags(page.html, Div.class, "id", "content").get(0);
//			contentDiv
			String allString = contentDiv.getStringText();
			//String allString = Util.getPlainHtml(contentDiv.toPlainTextString());
			post.pageData = page;
			//allString = ImageUtil.modifyImgHtml(allString, page);
			//logger.info("allString: " + allString);
			//不爬 已经有HDU内容的
			if(allString.contains("Problem Description") || allString.contains("Sample Input")
					|| allString.contains("问题描述")){
				post.power -= 50;
				//post.hasPro = true;
				//return null;
			}
			//if(allString.contains("问题描述")) return null;
			//keyCnt 记录每个关键词出现的次数
			int power = 0;
			List<Map.Entry<WpTermTaxonomy,Integer>> sort=new ArrayList();  //存储所有的key 出现的次数
			if(true){
				Set<WpTermTaxonomy> set = getMatchKeys(keys, title, allString, keyCnt);
				//logger.info("keyCnt.size():" + keyCnt.size());
				//if( keyCnt.size() == 0 ) return null;
				if(keyCnt.size() > 1){
					ValueComparator vc = new ValueComparator();
					sort.addAll(keyCnt.entrySet());
					Collections.sort(sort, vc);
				}
				for(Iterator<Map.Entry<WpTermTaxonomy,Integer>>  it=sort.iterator(); it.hasNext(); ){
					power += it.next().getValue();
				}
			}

			List<Content> listCon = new ArrayList<Content>();
			listCon.add(new Content(allString, false, null));
			boolean hasCode = false;
			int codeCnt =0;
			post.power -= 500;
			post.listContent = listCon;
			power += checkCodePower(post);
			post.power += power;
			post.listkeyCnt = sort;
			post.url = page.url;
			return post;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return post;
	}
	
	List<String> getKeys(PageData page){
		List<String> keys = new ArrayList<String>();
//		Div tagDiv = MyUtil.parseTags(page.html, Div.class, "class", "title content-title").get(0);
//		List<LinkTag> links = MyUtil.parseTags(tagDiv.toHtml(), LinkTag.class, null, null);
//		for(LinkTag link:links){
//			keys.add(link.getLinkText());
//			if(test) logger.info(link.getLinkText());
//		}
//		
		return keys;
	}
	
	private String getTitle(PageData page) throws Exception {
		try {
			List<HeadingTag> heads = MyUtil.parseTags(page.html, HeadingTag.class, "class", "title content-title");
			return heads.get(0).getStringText();
		} catch (Exception e) {
			e.printStackTrace();
//			throw e;
			return null;
		}
	}
	
	private class ValueComparator implements Comparator<Map.Entry<WpTermTaxonomy, Integer>>  
    {  
        public int compare(Map.Entry<WpTermTaxonomy, Integer> mp1, Map.Entry<WpTermTaxonomy, Integer> mp2)   
        {
            return mp2.getValue() - mp1.getValue();  
        }
    }
	
	public static void main(String[] args) {
		ItblogInit.init();
		String url = "http://hi.baidu.com/abcsf/item/74357c8e73e76eced1f8cdf3";
		String searchKeys[] = new String[]{"hdu", "3000"};
		PageData pg = MyUtil.getPage(url, false);
		WpPosts post = new Spider4Baidu().parseArticleSUrl(pg, searchKeys);
		logger.info(post.listContent.get(0).text);
		
	}

	
	
}




