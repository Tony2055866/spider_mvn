package com.itblog.sqider;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;

import com.main.Main;
import com.model.WpPosts;
import com.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sougou extends SearchEngine{

	private static Logger logger = LoggerFactory.getLogger(Sougou.class);

	/**
	 * 
	 * @param searchKeys
	 * @param page
	 * @param falg 
	 * @return
	 */
	private WpPosts search(String[] searchKeys, PageData page, int falg) {
		
		return null;
	}
	
	public static void main(String[] args) {
		List<String> url = new Sougou().search(new String[]{"hdu","2394"}, 0);
		for(String str:url)logger.info(str);
	}

	@Override
	public List<String> search(String[] keys, int page) {
		// TODO Auto-generated method stub
		 return search(keys, page, 0);
	}

	
	@Override
	public List<String> search(String[] keys, int page, int method) {
		String url="";
		List<String> urls = new ArrayList<String>();

		url="http://www.sogou.com/web?ie=utf8&query="+ URLEncoder.encode(keys[0]);
		for(int i=1; i<keys.length; i++) url += "+" + URLEncoder.encode(keys[i]);
logger.info(url);
		if(Main.out != null)
			Main.out.println(url);		
		
		PageData pd = MyUtil.getPage(url ,false);
		if(pd == null){
			logger.info(this.getClass()+":搜索失败!");
			return urls;
		}
		List<LinkTag> links = MyUtil.parseTags(pd.html, LinkTag.class, "name", "dttl");
		for(LinkTag link:links){
			String title = link.getLinkText();
				if(MyUtil.rightTitle(title, keys))
					urls.add(link.getLink());
		}
		
		return urls;
	}

}
