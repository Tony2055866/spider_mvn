package com.sqider;

import com.google.gson.Gson;
import com.model.CsdnCommentList;
import com.model.WpComments;
import com.model.WpPosts;
import com.model.WpTermTaxonomy;
import com.util.Init;
import com.util.MyUtil;
import org.apache.log4j.Logger;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TextareaTag;

import java.util.*;

public class Spider4Cppblog extends Spider{

	static Logger logger = Logger.getLogger(Spider4Cppblog.class);


	public static boolean debug = true;
	
	public WpPosts parseArticleSUrl(PageData page,String[] searchKeys){
		return parseArticleSUrl(page,searchKeys, true);
	}
	
	@Override
	public WpPosts parseArticleSUrl(PageData page,String[] searchKeys, boolean isAddTag) {
		 WpPosts post = new WpPosts();
		 post.host = page.host;
		try {
			logger.info("Spider4Cppblog 开始解析:" + page.url);
			Map<WpTermTaxonomy, Integer> keyCnt = new HashMap();
			String title = getTitle(page);
if(debug)
logger.info("文章标题:" + title);
			if(title == null) return null;
			if(searchKeys != null)
				if( !rightTitle(title,searchKeys)) return null;
			post.setPostTitle(title);
			
			List<String> keys = getKeys(page);
//logger.info("keys: " + keys.size());
			List<Div> contents = MyUtil.parseTags(page.html, Div.class, "class", "postText");
			if(contents.size() < 1){
				contents = MyUtil.parseTags(page.html, Div.class, "class", "post");
			}
			Div contentDiv = contents.get(0);
//			contentDiv
			String allString = contentDiv.getStringText();
			if(debug) logger.info(contentDiv.toHtml());
			post.power -= 50;
			//不爬 已经有HDU内容的
			if(allString.contains("Problem Description") || allString.contains("Sample Input")
					|| allString.contains("问题描述")){
				post.power -= 30;
				post.hasPro = true;
				//return null;
			}

			int power = 0;
			//如果需要自动添加标签
			//匹配的 目录和标签集合
			if(isAddTag){
				Set<WpTermTaxonomy> set = getMatchKeys(keys, title, allString, keyCnt);
				//power += set.size();
				
				//if( keyCnt.size() == 0 ) return null;
				
				List<Map.Entry<WpTermTaxonomy,Integer>> sort=new ArrayList();  
	
				if(keyCnt.size() > 1){
					ValueComparator vc = new ValueComparator();
					sort.addAll(keyCnt.entrySet());
					Collections.sort(sort, vc);
				}
				
			
				for(Iterator<Map.Entry<WpTermTaxonomy,Integer>>  it=sort.iterator(); it.hasNext(); ){
					power += it.next().getValue();
				}
				post.listkeyCnt = sort;
			}
			
			StringBuffer sb = new StringBuffer();
			List<Content> listCon = new ArrayList<Content>();
			String content = "";

			int codeCnt=0;
			boolean hasCode = false;
			listCon.add( new Content(contentDiv.toHtml(), false, null) );
			
			if(searchKeys !=null)
				if(codeCnt > 4) return null;
			
			if(content.trim().length() > 0)
				listCon.add(new Content(content, false, null));
			
			//如果没有代码则返回！！
			 if(searchKeys !=null && !hasCode){
				 power -= 100;
				 //return null;
			 }
			/*for(Content con:listCon){
				logger.info(con.text);
			}*/
			post.listContent = listCon;
			//继续找评论   过段时间再找
			//post.setWpCommentses(getCommets(post, page));
			power += checkCodePower(post);
			
			post.power += power;
			post.hasCode = hasCode;
			
			post.url = page.url;
			logger.info("解析成功！！！！");
			return post;
		} catch (Exception e) {
		
			e.printStackTrace();
			//logger.info("parse Html:-----------------\n" + page.html);
			return null;
		}
		
	}

	private Content getCode(String str, boolean oj) {
		logger.info("getCode:" + str);
		try {
			List<PreTag> codes = MyUtil.parseTags(str, PreTag.class, "name", "code");
			//LinkTag link = (LinkTag) spans.get(0).getChild(0);
			if(codes.size() > 0){
				PreTag node1 = codes.get(0);
				String lang = node1.getAttribute("class");
				
				//oj的话只用 Java和cpp
				if(oj)
					if(! lang.toLowerCase().equals("java")) lang = "cpp";
				
				return new Content(node1.getStringText(),true,lang);
			}
			List<TextareaTag> codes2 = MyUtil.parseTags(str, TextareaTag.class, "name", "code");
			if(codes2.size() > 0){
				TextareaTag node1 = codes2.get(0);
				String lang = node1.getAttribute("class");
				if(oj)
					if(! lang.toLowerCase().equals("java")) lang = "cpp";
				
				if(debug) logger.info("code lang:" + lang);
				return new Content(node1.getStringText(),true,lang);
			}
			return null;
			//return link.getLinkText().trim();
		} catch (Exception e) {
			e.printStackTrace();
//			throw e;
			return null;
		}
	}

	private String getTitle(PageData page) throws Exception {
		try {
			List<Div> spans = MyUtil.parseTags(page.html, Div.class, "class", "postTitle");
			if(spans.size() > 0){
				Div link = (Div) spans.get(0);
			
			return link.toPlainTextString().trim();
			
			}
		} catch (Exception e) {
			e.printStackTrace();
//			throw e;
			return null;
		}
		return null;
	}
	
	List<String> getKeys(PageData page){
		List<String> keys = new ArrayList<String>();
		List<Span> spans = MyUtil.parseTags(page.html, Span.class, "class", "link_categories");
		if(spans.size() > 0) {
			String str = spans.get(0).getStringText();
			
			List<LinkTag> links = MyUtil.parseTags(str, LinkTag.class, "href", null);
			//int len = spans.get(0).getChildCount();
			//logger.info("关键词个数： " + len);
			for(int i=0; i<links.size(); i++){
				//logger.info(spans.get(0).getChild(i));
				LinkTag link =links.get(i);
				keys.add(link.getLinkText());
			}
		}
		return keys;
	}
	
	private Set<WpComments> getCommets(WpPosts post,PageData pg) {
		
		logger.info(pg.url);
		String commentsUrl = pg.url.replace("article/details", "comment/list");
		PageData pdata = MyUtil.getPage(commentsUrl, false);
		Gson gson = new Gson();
		CsdnCommentList list = gson.fromJson(pdata.html, CsdnCommentList.class);
//		logger.info(list.getList().length);
		Set<WpComments> coms = new HashSet<WpComments>();
		return coms;
	}
	
	public static void main(String[] args) {
		Init.init();
		
		String url = "http://www.cppblog.com/superlong/archive/2010/07/19/120756.html";
		String searchKeys[] = new String[]{"hdu", "3455"};
		PageData pg = MyUtil.getPage(url, false);
		WpPosts post = new Spider4Cppblog().parseArticleSUrl(pg, searchKeys);
		if(post == null) return;
		List<Map.Entry<WpTermTaxonomy,Integer>> keys = post.listkeyCnt;
		for(Map.Entry<WpTermTaxonomy,Integer> t:keys){
			logger.info(t.getKey().getDescription() + "  => " + t.getValue());
		}
	}
	
	private class ValueComparator implements Comparator<Map.Entry<WpTermTaxonomy, Integer>>  
    {  
        public int compare(Map.Entry<WpTermTaxonomy, Integer> mp1, Map.Entry<WpTermTaxonomy, Integer> mp2)   
        {  
            return mp2.getValue() - mp1.getValue();  
        }  
    }

	@Override
	public WpPosts getArticleSUrl(PageData page) {
		// TODO Auto-generated method stub
		return null;
	}


}
