package com.sqider;

import com.model.WpPosts;
import com.model.WpTermTaxonomy;
import com.util.Init;
import com.util.MyUtil;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Span;

import java.util.*;

public class Spider4163 extends Spider{
	static boolean test = true;
	@Override
	public WpPosts getArticleSUrl(PageData page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public WpPosts parseArticleSUrl(PageData page,String[] searchKeys){
		WpPosts post = new WpPosts();
		System.out.println("Spider4Cnblog 开始解析:" + page.url);
		Map<WpTermTaxonomy, Integer> keyCnt = new HashMap();
		post.host = page.host;
		try {
			String title = getTitle(page);
if(test)
	System.out.println("文章标题:" + title);
			if(title == null) return null;
			if(searchKeys != null){
				if( !rightTitle(title,searchKeys)){
					System.out.println("文章不符合");
					return null;
				}
			}
				post.setPostTitle(title);
			List<String> keys = getKeys(page);
//System.out.println(page.html);
			Div contentDiv = MyUtil.parseTags(page.html, Div.class, "class", "bct fc05 fc11 nbw-blog ztag").get(0);
//			contentDiv
			String allString = contentDiv.getStringText();
			//String allString = Util.getPlainHtml(contentDiv.toPlainTextString());
		//	allString = ImageUtil.modifyImgHtml(allString, page);
			//System.out.println("allString: " + allString);
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

			List<Content> listCon = new ArrayList<Content>();
			listCon.add(new Content(allString, false, null));
			boolean hasCode = false;
			int codeCnt =0;
			post.power -= 300;
			post.listContent = listCon;
			power += checkCodePower(post);
			post.power += power;
			post.listkeyCnt = sort;
			post.url = page.url;
			post.pageData = page;
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
		/*List<LinkTag> links = MyUtil.parseTags(page.html, LinkTag.class, "class", "fc03 m2a");
		for(LinkTag link:links){
			keys.add(link.getLinkText());
			if(test) System.out.println(link.getLinkText());
		}*/
//		
		return keys;
	}
	
	private String getTitle(PageData page) throws Exception {
		try {
			List<Span> heads = MyUtil.parseTags(page.html, Span.class, "class", "tcnt");
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
		Init.init();
		String url = "http://qianmacao.blog.163.com/blog/static/203397180201231210934444/";
		String searchKeys[] = new String[]{"hdu", "2289"};
		PageData pg = MyUtil.getPage(url, false);
		WpPosts post = new Spider4163().parseArticleSUrl(pg, searchKeys);
		System.out.println(post.listContent.get(0).text);
	}

	
	
}


