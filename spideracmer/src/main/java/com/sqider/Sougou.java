package com.sqider;

import com.main.Main;
import com.model.WpPosts;
import com.util.MyUtil;
import org.apache.log4j.Logger;
import org.htmlparser.tags.LinkTag;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Sougou extends SearchEngine{
	static Logger logger = Logger.getLogger(Sougou.class);

	
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
		
		PageData pd = MyUtil.getPage(url ,false);
		if(pd == null){
			logger.info(this.getClass() + ":搜索失败!");
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
