package com.itblog.sqider;

import java.util.*;

import org.htmlparser.Node;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;

import com.itong.main.ItblogUtil;
import com.main.Main;
import com.model.WpPosts;
import com.model.WpTermTaxonomy;
import com.util.DbUtil;
import com.util.ItblogInit;
import com.util.MyUtil;
import com.util.ValueComparator;



public class CtoSpider extends Spider{
	public static void main(String[] args)  {
		ItblogInit.init();
		Main.debug = true;
		WpPosts post = parseUrl("http://zengzhaozheng.blog.51cto.com/8219051/1370125",true);
		try {
			ItblogUtil.saveCommPost(post,false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(getBlogUrls(2).size());
		//System.out.println(MyUtil.getPage("http://gaotong.blog.51cto.com/2385162/1369526").html);
		//System.out.println(MyUtil.clearTitle("动态规划-最小编辑距离(Edit Distance)"));
		
	}
	
	public static void startTask(){
		
	}
	
	public static void startParse(int page) {
		for(int i=1; i<=page; i++){
			List<String> urls = getBlogUrls(i);
			for(String url:urls){
				WpPosts post = parseUrl(url, true);
				if(post == null) continue;
				try {
					ItblogUtil.saveCommPost(post,false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}
	
	public static  WpPosts parseUrl(String url,boolean isAddTag){
		return parseUrl(url, isAddTag, null);
	}
	
	public static  WpPosts parseUrl(String url,boolean isAddTag, PageData pd){
		WpPosts post = new WpPosts();
		post.url = url;
		if(pd == null)
		  pd = MyUtil.getPage(url);
		if(pd != null){
			
			//解析标题
			List<Div> titleDiv = MyUtil.parseTags(pd.html, Div.class, "class", "showTitle");
			String title;
			if(titleDiv.size() > 0){
				 title = titleDiv.get(0).toPlainTextString().trim();
				post.setPostTitle(title);
			}else
				return null;
			//解析分类，标签 等关键词
			List<String> keys = getKeysWords(pd);
			//获得内容
			List<Div> contentDivs = MyUtil.parseTags(pd.html, Div.class, "class", "showContent");
			Div contentDiv = null;
			if(contentDivs.size() > 0){
				contentDiv = contentDivs.get(0);
			}else
				return null;
			
			String allString = contentDiv.getStringText();
			if(ItblogUtil.checkAd(allString)){
				return null;
			}
			List<Map.Entry<WpTermTaxonomy,Integer>> sort=new ArrayList();  //存储所有的key 出现的次数
			Map<WpTermTaxonomy, Integer> keyCnt = new HashMap();
			
			//添加标签
			if(isAddTag){
				Set<WpTermTaxonomy> set = SpiderUtil.getMatchKeys(keys, title, allString, keyCnt);
				//System.out.println("keyCnt.size():" + keyCnt.size());
				//if( keyCnt.size() == 0 ) return null;
				if(keyCnt.size() > 1){
					ValueComparator vc = new ValueComparator();
					sort.addAll(keyCnt.entrySet());
					Collections.sort(sort, vc);
				}
				post.listkeyCnt =  sort;
			}
			
			post.url = pd.url;
			post.host = pd.host;
			//解析代码和 内容
			List<Content> conList = getContentList(allString, contentDiv);
			if(conList.size() > 0)
				post.listContent = conList;
			else
				return null;
			return post;
		}
		return null;
	}
	
	public static List<Content> getContentList(String text,Div contentDiv){
		List<Content> listCon = new ArrayList<Content>();
		String content = "";
		boolean hasCode = false;
		int codeCnt =0;
		for(int i=0; i<contentDiv.getChildCount(); i++){
//			Node n = allNodes.elementAt(i);
			Node n = contentDiv.childAt(i);
			String str = n.toHtml();
			if(n instanceof ParagraphTag){
				String plainTxt = n.toPlainTextString();
				if( plainTxt.startsWith("本文出自") ){
					continue;
				}
			}
			//replaceAll("href=\"http://s3\\.51cto\\.com.+?\"", "")
			str = str.replaceAll("href=\"http://.+?\"", "");
			//<a href="http://s3.51cto.com/wyfs02/M02/1D/3D/wKioL1MYKSXhpQ2gAAEpRR0S2BI482.jpg" 
			if(n instanceof PreTag && ((PreTag) n).getAttribute("class") != null){
				//System.out.println("codestr: " + str);
				String code = n.toPlainTextString();
				String lang = ((PreTag) n).getAttribute("class");
				
				int last = lang.length();
				if(lang.contains(";")) last = lang.indexOf(";");
				else if(lang.contains("\"")) last = lang.indexOf("\"");
				
				lang = lang.substring( lang.indexOf(':')+1 , last);
				if(content != ""){
					Content content1 = new Content(content, false, null);
					listCon.add(content1);
				}
				Content content2 = new Content(code, true, lang);
				//System.out.println("有代码！！！" + lang + "\n" + code);
				listCon.add(content2);
				
				hasCode = true;
				content = "";
				codeCnt++;
				//System.out.println(content2.text);
			}else{
				str.replaceAll("toolbar:false;", "");
				content += str;
			}
		}
		if(content.trim().length() > 0)
			listCon.add(new Content(content, false, null));
		return listCon;
	}
	
	public static List<String> getBlogUrls(int page){
		List<String> urls = new ArrayList<String>();
		Set<String> titleAndHostSet = new HashSet<String>();
		PageData pd = null;
		if(page == 1)
			pd = MyUtil.getPage("http://blog.51cto.com/original");
		else
			pd = MyUtil.getPage("http://blog.51cto.com/original/0/" + page);
		//System.out.println(pd.html);
		if(pd != null){
			List<LinkTag> list = MyUtil.parseTags(pd.html, LinkTag.class, "china", "标题");
			for(LinkTag link:list){
				if (!DbUtil.checkUrl(link.getLink()) && ItblogUtil.getKeysByText(link.getLinkText()).size() > 0
						&& !DbUtil.checkTitle(link.getLinkText(),pd.host))
					if(!urls.contains(link.getLink().trim()) && !titleAndHostSet.contains(link.getLinkText()+";" + pd.host)) {
						urls.add(link.getLink().trim());
						titleAndHostSet.add(link.getLinkText()+";" + pd.host);
					}
			}
		}
		return urls;
	}
	
	public static List<String> getKeysWords(PageData pd){
		List<String> keys = new ArrayList<String>();
		List<Div> tagsdiv = MyUtil.parseTags(pd.html, Div.class, "class", "showTags");
		if(tagsdiv.size() > 0){
			//List<LinkTag> tagsLinks = MyUtil.parseTags(tagsdiv.get(0).toHtml(),LinkTag.class, null,null);
			Div d = tagsdiv.get(0);
			for(int i=0; i<d.getChildren().size(); i++){
				Node node = d.getChild(i);
				if(node instanceof LinkTag){
					keys.add(node.toPlainTextString().trim());
				}
			}
			keys.remove(keys.size()-1);
		}
		List<Div> catsdiv = MyUtil.parseTags(pd.html, Div.class, "class", "showType");
		if(catsdiv.size() > 0){
			//List<LinkTag> tagsLinks = MyUtil.parseTags(tagsdiv.get(0).toHtml(),LinkTag.class, null,null);
			Div d = catsdiv.get(0);
			for(int i=0; i<d.getChildren().size(); i++){
				Node node = d.getChild(i);
				if(node instanceof LinkTag){
					keys.add(node.toPlainTextString().trim());
					break;
				}
			}
		}
		//for(String key:keys) System.out.println(key);
		return keys;
	}

	@Override
	public WpPosts getArticleSUrl(PageData page) {
		return  parseUrl(page.url, true, page);
	}

	@Override
	public WpPosts parseArticleSUrl(PageData page, String[] searchKeys) {
		
		return null;
	}
}
