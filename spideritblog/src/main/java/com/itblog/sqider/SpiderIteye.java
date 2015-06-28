package com.itblog.sqider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.mapping.Array;
import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;

import com.itong.main.ItblogUtil;
import com.model.WpPosts;
import com.model.WpTermTaxonomy;
import com.util.CodeUtil;
import com.util.MyUtil;

public class SpiderIteye {
	public static boolean debug = false;
	
	public WpPosts parseArticleSUrl(PageData page, boolean isAddTag) {
		 String baseUrl ="http://blog.csdn.net";
		 
		 WpPosts post = new WpPosts();
		 post.host = page.host;
		try {
			System.out.println("Spider4Csdn 开始解析:" + page.url);
			Map<WpTermTaxonomy, Integer> keyCnt = new HashMap();
			String title = getTitle(page);
if(debug)
	System.out.println("文章标题:" + title);
			if(title == null) return null;
			
			post.setPostTitle(title);

//System.out.println("keys: " + keys.size());
			
			Div contentDiv = MyUtil.parseTags(page.html, Div.class, "class", "blog_content").get(0);
//			contentDiv
			String allString = contentDiv.getStringText();
if(debug) System.out.println(contentDiv.toHtml());

			//if(allString.contains("问题描述")) return null;
			//int power = checkTitle(title, searchKeys);
			int power = 0;
			//如果需要自动添加标签
			//匹配的 目录和标签集合
			if(isAddTag){
				Set<WpTermTaxonomy> set = SpiderUtil.getMatchKeys(null, title, allString, keyCnt);
					boolean cat = false;
					//普通文章
					for(WpTermTaxonomy term:set){
						if(term.getTaxonomy().contains("cate")){
							if(!cat) post.getTerms().add(term);
							cat = true;
						}else{
							post.getTerms().add(term);
						}
					}
					post.listkeyCnt = null;
				}
			
			StringBuffer sb = new StringBuffer();
			List<Content> listCon = new ArrayList<Content>();
			post.listContent = listCon;
			
			int codeCnt= replaceCodeTag(contentDiv.getChildrenHTML(), post);
			
			boolean hasCode = codeCnt > 0;

			post.power += power;
			post.hasCode = hasCode;
			
			post.url = page.url;
			System.out.println("解析成功！！！！");
			return post;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	private int replaceCodeTag(String childrenHTML, WpPosts post) {
		List<Content> listContent = new ArrayList<Content>();
		 post.listContent = listContent;
		 int codecnt = 0;
		 StringBuffer sb = new StringBuffer();
			NodeList nodelist = MyUtil.parseAllTags(childrenHTML);
			for(int i=0; i<nodelist.size(); i++){
				Node node = nodelist.elementAt(i);
				if( (node instanceof PreTag || node instanceof TextareaTag)
						&& ((TagNode)node).getAttribute("name")!=null &&
						( (TagNode)node).getAttribute("name").equals("code")  ){
					TagNode pre = (TagNode)node;
					codecnt ++;
					String lang = "cpp";
					lang = pre.getAttribute("class");
					if(lang.trim().length() < 1) lang = "cpp";
					pre.setAttribute("class", "brush:" + lang);
					sb.append(pre.toTagHtml());
				}
				else if(node instanceof TextNode){
					sb.append(node.getText());
				}else{
					sb.append("<" + node.getText() + ">");
				}
			}
			post.listContent.add( new Content(sb.toString(), false,null) );
		return 0;
	}

	private Set<WpTermTaxonomy> getMatchKeys(Object object, String title,
			String allString, Map<WpTermTaxonomy, Integer> keyCnt) {
		
		return null;
	}
	
	public WpPosts parseUrl(String url,boolean addTag){
		PageData pd = MyUtil.getPage(url);
		
		return parseArticleSUrl(pd, addTag);
		
	}
	
	public static List<String> getUrls(){
		List<String> urls = new ArrayList<String>();
		PageData pd = MyUtil.getPage("http://www.iteye.com/blogs");
		List<Div> listbox = MyUtil.parseTags(pd.html, Div.class, "class", "box");
		if(listbox.size() > 7){
			Div listDiv = listbox.get(7);
			List<LinkTag> listUrls = MyUtil.parseTags(listDiv.toHtml(), LinkTag.class, null, null);
			for(LinkTag link:listUrls){
				if(link.getLink().contains("iteye.com/blog"))
					urls.add(link.getLink());
			}
		}
		return urls;
	}

	private String getTitle(PageData page) {
		List<Div> list = MyUtil.parseTags(page.html, Div.class, "class", "blog_title");
		if(list.size() > 0) return list.get(0).toPlainTextString();
		return null;
	}
	
	public static void main(String[] args) {
//		WpPosts post = new SpiderIteye().parseUrl("http://deepinmind.iteye.com/blog/2047172", true);
//		
//		if(post != null){
//			for(Content con:post.listContent){
//			//	System.out.println(con.text + "\n --------------------");
//				String text = con.text.replaceAll("class=\"brush", "xxxxxbrush");
//				text = text.replaceAll("class=\".+?\"", "");
//				text = text.replaceAll("xxxxxbrush", "class=\"brush");
//				System.out.println(text);
//			}
//		}
		SpiderIteye spider = new SpiderIteye();
		List<String> urls = new SpiderIteye().getUrls();
		for(String str:urls){
		                System.out.println(str);
		                WpPosts post  = spider.parseUrl(str, true);
		                try {
							ItblogUtil.saveCommPost(post, false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                
		                break;
		}
	}
}
