package com.sqider;

import com.util.MyUtil;
import org.htmlparser.tags.LinkTag;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class So360 extends SearchEngine {
	public static void main(String[] args) {
		List<String> url = new So360().search(new String[]{"hdu","2394"}, 0);
		for(String str:url)System.out.println(str);
	}

	@Override
	public List<String> search(String[] keys, int page) {
		 return search(keys, page, 0);
	}

	@Override
	public List<String> search(String[] keys, int page, int method) {
		String url = "http://www.so.com/s?ie=utf-8&q="+URLEncoder.encode(keys[0]);
		for(int i=1; i<keys.length; i++)
			url += "+" + URLEncoder.encode(keys[i]);
		List<String> urls = new ArrayList<String>();
		System.out.println("360搜索:" + url);
		PageData pd = MyUtil.getPage(url);
		if(pd != null){
			List<LinkTag> links = MyUtil.parseTags(pd.html, LinkTag.class, "data-tp", null);
			
			for(LinkTag link:links){
				String title = link.getLinkText();
				if(MyUtil.rightTitle(title, keys))
					urls.add(link.getLink());
			}
		}
		
		
		return urls;
	}
	

}
