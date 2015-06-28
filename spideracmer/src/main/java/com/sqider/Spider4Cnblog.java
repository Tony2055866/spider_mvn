package com.sqider;

import com.hdusubmit.HduSubmit;
import com.main.Util;
import com.model.WpPosts;
import com.model.WpTermTaxonomy;
import com.util.CodeUtil;
import com.util.MyUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.util.*;

public class Spider4Cnblog extends Spider{

	static boolean test = false;
	
	public WpPosts parseArticleSUrl(PageData page,String[] searchKeys){
		return parseArticleSUrl(page,searchKeys, true);
	}
	
	@Override
	public WpPosts parseArticleSUrl(PageData page, String[] searchKeys, boolean isAddTag) {
		WpPosts post = new WpPosts();
		System.out.println("Spider4Cnblog 开始解析:" + page.url);
		Map<WpTermTaxonomy, Integer> keyCnt = new HashMap();
		post.host = page.host;
		try {
			String title = getTitle(page);
if(test)
	System.out.println("文章标题:" + title);
			if(title == null) return null;
			
			if(searchKeys != null)
				if( !rightTitle(title,searchKeys)) return null;
			post.setPostTitle(title);
			
			List<String> keys = getKeys(page);
if(test)
	System.out.println("keys: " + keys.size());
			
			Div contentDiv = MyUtil.parseTags(page.html, Div.class, "id", "cnblogs_post_body").get(0);
//			contentDiv
			String allString = contentDiv.getStringText();
			
			//System.out.println("allString: " + allString);
			//不爬 已经有HDU内容的
			if(allString.contains("Problem Description") || allString.contains("Sample Input")
					|| allString.contains("问题描述")){
				post.power -= 30;
				post.hasPro = true;
				//return null;
			}
			//if(allString.contains("问题描述")) return null;
			//keyCnt 记录每个关键词出现的次数
			int power = 0;
			List<Map.Entry<WpTermTaxonomy,Integer>> sort=new ArrayList();  //存储所有的key 出现的次数
			if(isAddTag){
				Set<WpTermTaxonomy> set = getMatchKeys(keys, title, allString, keyCnt);
				//System.out.println("keyCnt.size():" + keyCnt.size());
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
			StringBuffer sb = new StringBuffer();

			String content = "";
			boolean hasCode = false;
			int codeCnt =0;
			 codeCnt= replaceCodeTag(contentDiv.getChildrenHTML(), post, searchKeys);
			//searchKeys != null 表示是搜索的OJ题目，否则为普通文章
			if(searchKeys != null && codeCnt > 3) return null;
			
			//System.out.println(power + " " + post.power);
			//如果没有代码则返回！！
			if(searchKeys != null && !hasCode){
				
				 power -= 100;
			}
			    //                               return null;
			
			//继续找评论   过段时间再找
			//post.setWpCommentses(getCommets(post, page));
			
			power += checkCodePower(post);
			//System.out.println(power + " " + post.power);
			post.power += power;
			post.listkeyCnt = sort;
			post.hasCode = hasCode;
			
			post.url = page.url;
			return post;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return post;
	}
	
	private int replaceCodeTag(String childrenHTML, WpPosts post,
			String[] searchKeys) throws Exception {
		int cnt = 0;
		StringBuffer sb = new StringBuffer();
		NodeList nodelist = MyUtil.parseAllTags(childrenHTML);
		for(int i=0; i<nodelist.size(); i++){
			Node node = nodelist.elementAt(i);
			TagNode tag = null;
			if(node instanceof TagNode)
				 tag = (TagNode)node;
			if( (node instanceof Div)
					&& tag.getAttribute("class")!=null &&
					(   tag.getAttribute("class").trim().equalsIgnoreCase("cnblogs_code")
					|| tag.getAttribute("class").trim().equalsIgnoreCase("cnblogs_Highlighter") 
					||tag.getAttribute("class").trim().equalsIgnoreCase("cnblogs_code_hide")  
					)
					&& !tag.toHtml().contains("class=\"brush:") //有bursh则不用更改了
					) {
			//	System.out.println(" ---------------------------");
				cnt ++;
				String lang = "cpp";
				Div codeDiv = (Div)tag;
				Content codeContent = getCode(tag.toHtml(), (Div)tag);
				//如果是oj只有 java 和 cpp
				if(searchKeys != null && searchKeys.length == 2 && Util.isNumber(searchKeys[1])){
					String code = StringEscapeUtils.unescapeHtml4(codeContent.text);
					if(HduSubmit.GetInstance().doSubmit(searchKeys[1], code)){
						post.power += 50;
					}
					lang = codeContent.lang;
				}
				sb.append("\n<pre class='brush:" + lang + "'>" + codeContent.text + "</pre>\n");
				//直接跳到末尾的标签。 中间是代码
				Tag endTag = tag.getEndTag();
				while(nodelist.elementAt(i) != endTag){
					i++;
				}
			}
			else if(node instanceof TextNode){
				sb.append(node.getText());
			}else{
				sb.append("<" + node.getText() + ">");
			}
		}
		post.listContent.add( new Content(sb.toString(), false,null) );
		return cnt;
	}

	private Content getCode(String str, Div codeDiv) {
		//System.out.println("getCode:\n" + str);
		try {
			List<PreTag> codes = MyUtil.parseTags(str, PreTag.class, null, null);
			String orCodestr = "";
			PreTag codePre = null;
			if(codes.size() > 0){
				 codePre = codes.get(0);
				 orCodestr = codePre.getStringText();
			}else{
				 orCodestr = codeDiv.getStringText();
			}
			String code =  CodeUtil.getCode(orCodestr);
			String lang = CodeUtil.getLang(code);
			if(codes.size() > 0)
				codePre.setAttribute("class", "brush:" + lang);
			Content content = new Content(code, true, lang);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("博客园获取代码错误");
			return null;
		}
	}
	
	

	private String getTitle(PageData page) throws Exception {
		try {
			List<LinkTag> spans = MyUtil.parseTags(page.html, LinkTag.class, "id", "cb_post_title_url");
			if(spans == null || spans.size() == 0){
				spans = MyUtil.parseTags(page.html, LinkTag.class, "id", "ctl01_lnkTitle");
			}
			if(spans == null || spans.size() == 0) return null;
			LinkTag link = (LinkTag) spans.get(0);
			return link.getLinkText().trim();
		} catch (Exception e) {
			e.printStackTrace();
//			throw e;
			return null;
		}
	}
	
	private String getComments(){
	
		return null;
	}
	
	List<String> getKeys(PageData page){
		List<String> keys = new ArrayList<String>();
		// 原来的不管用，删掉了
		return keys;
	}

	public static void main(String[] args) {
		//Init.init();
		String url = "http://www.cnblogs.com/jbelial/archive/2011/07/25/2116518.html";
		String searchKeys[] = new String[]{"hdu", "3460"};
		PageData pg = MyUtil.getPage(url, false);
		WpPosts post = new Spider4Cnblog().parseArticleSUrl(pg, null,false);
		for(Content con:post.listContent){
			//if( con!=null && con.isCode)
			System.out.println(con.text);
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
		
		return null;
	}

}
