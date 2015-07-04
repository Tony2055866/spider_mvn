package com.sqider;

import com.google.gson.Gson;
import com.hdusubmit.HduSubmit;
import com.main.Util;
import com.model.CsdnCommentList;
import com.model.WpComments;
import com.model.WpPosts;
import com.model.WpTermTaxonomy;
import com.util.CodeUtil;
import com.util.MyUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Spider4Csdn extends Spider{
	static Logger logger = LoggerFactory.getLogger(Spider4Csdn.class);
	public static boolean debug = false;
	
	public WpPosts parseArticleSUrl(PageData page,String[] searchKeys){
		return parseArticleSUrl(page,searchKeys, true);
	}
	
	@Override
	public WpPosts parseArticleSUrl(PageData page,String[] searchKeys, boolean isAddTag) {
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
			
			if(searchKeys != null)
				if( !rightTitle(title,searchKeys)) return null;
			post.setPostTitle(title);
			
			List<String> keys = getKeys(page);
//System.out.println("keys: " + keys.size());
			
			Div contentDiv = MyUtil.parseTags(page.html, Div.class, "class", "article_content").get(0);
//			contentDiv
			String allString = contentDiv.getStringText();
			if(debug) System.out.println(contentDiv.toHtml());

			//不爬 已经有HDU内容的
			if(searchKeys!=null &&   (allString.contains("Problem Description") || allString.contains("Sample Input")
					|| allString.contains("问题描述")) ){
				post.power -= 30;
				post.hasPro = true;
				//return null;
			}
			//if(allString.contains("问题描述")) return null;
			//int power = checkTitle(title, searchKeys);
			int power = 0;
			
			//如果需要自动添加标签
			//匹配的 目录和标签集合
			if(isAddTag){
				Set<WpTermTaxonomy> set = getMatchKeys(keys, title, allString, keyCnt);
				//power += set.size();
				if(searchKeys != null){ //对于oj题目
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
				}else{
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
				
			}
			

			
			int codeCnt= replaceCodeTag(contentDiv.getChildrenHTML(), post, searchKeys);
			boolean hasCode = codeCnt > 0;

			if(searchKeys !=null){
				if(codeCnt > 4) return null;
				if(codeCnt >= 2) post.power -= 200;
			}
			//如果没有代码则
			 if(searchKeys !=null && !hasCode){
				 power -= 100;
				 //return null;
			 }
			/*for(Content con:listCon){
				System.out.println(con.text);
			}*/
			//继续找评论   过段时间再找
			//post.setWpCommentses(getCommets(post, page));
			power += checkCodePower(post);
			post.power += power;
			post.hasCode = hasCode;
			
			post.url = page.url;
			System.out.println("解析成功！！！！");
			return post;
		} catch (Exception e) {
		
			e.printStackTrace();
			//System.out.println("parse Html:-----------------\n" + page.html);
			return null;
		}
		
	}

	private Content getCode(String str, boolean oj) {
		//System.out.println("getCode:" + str);
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
				
				if(debug) System.out.println("code lang:" + lang);
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
			List<Span> spans = MyUtil.parseTags(page.html, Span.class, "class", "link_title");
			LinkTag link = (LinkTag) spans.get(0).getChild(0);
			
			return link.getLinkText().trim();
		} catch (Exception e) {
			e.printStackTrace();
//			throw e;
			return null;
		}
	}
	
	List<String> getKeys(PageData page){
		List<String> keys = new ArrayList<String>();
		List<Span> spans = MyUtil.parseTags(page.html, Span.class, "class", "link_categories");
		if(spans.size() > 0) {
			String str = spans.get(0).getStringText();
			
			List<LinkTag> links = MyUtil.parseTags(str, LinkTag.class, "href", null);
			//int len = spans.get(0).getChildCount();
			//System.out.println("关键词个数： " + len);
			for(int i=0; i<links.size(); i++){
				//System.out.println(spans.get(0).getChild(i));
				LinkTag link =links.get(i);
				keys.add(link.getLinkText());
			}
		}
		return keys;
	}
	
	private Set<WpComments> getCommets(WpPosts post,PageData pg) {
		System.out.println(pg.url);
		String commentsUrl = pg.url.replace("article/details", "comment/list");
		PageData pdata = MyUtil.getPage(commentsUrl, false);
		Gson gson = new Gson();
		CsdnCommentList list = gson.fromJson(pdata.html, CsdnCommentList.class);
//		System.out.println(list.getList().length);
		Set<WpComments> coms = new HashSet<WpComments>();
		return coms;
	}
	
	public static void main(String[] args) {
		//Init.init();
		//blog.csdn.net/hqu_fritz/article/details/39474493
		String url = "http://blog.csdn.net/hqu_fritz/article/details/39474493";
		String searchKeys[] = new String[]{"hdu", "3768"};
		PageData pg = MyUtil.getPage(url, false);
		WpPosts post = new Spider4Csdn().parseArticleSUrl(pg, null, false);
		logger.info("start print article content!! ");
		if(post != null){
			for(Content con:post.listContent){
				//System.out.println(con.text + "\n --------------------");
				String text = con.text.replaceAll("class=\"brush", "xxxxxbrush");
				text = text.replaceAll("class=\".+?\"", "");
				text = text.replaceAll("xxxxxbrush", "class=\"brush");
				System.out.println(text);
			}
		}
//		List<Map.Entry<WpTermTaxonomy,Integer>> keys = post.listkeyCnt;
//		for(Map.Entry<WpTermTaxonomy,Integer> t:keys){
//			System.out.println(t.getKey().getDescription() + "  => " + t.getValue());
//		}
	}
	
	//返回代码的 数量
	public int replaceCodeTag(String html, WpPosts post,String[] searchKeys) throws Exception{
		//List<PreTag> codes = MyUtil.parseTags(str, PreTag.class, "name", "code");
		//List<TextareaTag> codes2 = MyUtil.parseTags(str, TextareaTag.class, "name", "code");
		if(post.listContent == null){
			List<Content> listCon = new ArrayList<Content>();
			post.listContent = listCon;
		}
		int cnt = 0;
		StringBuffer sb = new StringBuffer();
		NodeList nodelist = MyUtil.parseAllTags(html);
		for(int i=0; i<nodelist.size(); i++){
			Node node = nodelist.elementAt(i);
			if( (node instanceof PreTag || node instanceof TextareaTag)
					&& ((TagNode)node).getAttribute("name")!=null &&
					( (TagNode)node).getAttribute("name").equals("code")  ){
				TagNode pre = (TagNode)node;
				cnt ++;
				String lang = "cpp";
				
				//如果是oj只有 java 和 cpp
				if(searchKeys != null && searchKeys.length == 2 && Util.isNumber(searchKeys[1])){
					String codetext = pre.toPlainTextString();
					String code = StringEscapeUtils.unescapeHtml4(codetext);
					if(HduSubmit.GetInstance().doSubmit(searchKeys[1], code)){
						post.power += 50;
					}
					lang = CodeUtil.getLang(code).toLowerCase();
				}else{
					lang = pre.getAttribute("class");
					if(lang.trim().length() < 1) lang = "cpp";
				}
				pre.removeAttribute("class");
				pre.setAttribute("class", "brush:" + lang);
				if(node instanceof PreTag)
					sb.append(pre.toTagHtml());
				else{
					pre.setTagName("pre");
					pre.getEndTag().setTagName("pre");
					pre.removeAttribute("class");
					pre.setAttribute("class", "brush:" + lang);
					//pre.setEndTag(new PreTag());
					pre.removeAttribute("rows");
					pre.removeAttribute("cols");
					sb.append(pre.toTagHtml());
				}
			}else if(node instanceof Div && (node.toHtml().contains("code_snippet_id"))){
				//logger.info("code_snippet_id div's html:" + node.toHtml());
				List<PreTag> codes =MyUtil.parseTags(node.toHtml(), PreTag.class, "name", "code");
				logger.info("codes.size : " + codes.size());
				for(PreTag preCodeTag:codes){

					if(StringUtils.isNotEmpty(preCodeTag.getAttribute("class")) && !preCodeTag.getAttribute("class").equals("null") ){
						preCodeTag.removeAttribute("class");
						preCodeTag.setAttribute("class", "'brush:" + preCodeTag.getAttribute("class")+"'");
					}else{
						preCodeTag.removeAttribute("class");
						preCodeTag.setAttribute("class", "'brush:cpp'");
					}
					//sb.append(preCodeTag.toTagHtml());
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
