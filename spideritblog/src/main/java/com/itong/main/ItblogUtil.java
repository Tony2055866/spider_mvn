package com.itong.main;




import java.io.File;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import com.util.DbUtil;
import org.apache.log4j.Logger;
import org.htmlparser.tags.LinkTag;

import com.itblog.sqider.Content;
import com.itblog.sqider.CtoSpider;
import com.itblog.sqider.PageData;
import com.itblog.sqider.Spider;
import com.itblog.sqider.Spider4Csdn;
import com.model.Log;
import com.model.LogDAO;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.model.WpTermTaxonomy;
import com.model.WpTerms;
import com.util.ImageUtil;
import com.util.ItblogInit;
import com.util.MyUtil;

public class ItblogUtil {

	static Logger logger = Logger.getLogger(ItblogUtil.class);
	public static final int MAX_DEEP = 10;
	static int dayTime = 24 *60  * 60 * 1000;
	static int hourTime = 60  * 60 * 1000;
	public static String adStrings[] = {
			"联系电话","业务QQ","信誉第一"
	};
	public static void setCommonPost(WpPosts post, int hours){
		setCommonPost(post, hours, (long) ItblogInit.ojSpiderUserId);
	}

	public static long lastTime = 0;
	public static void setCommonPost(WpPosts post, int hours, long author){
		Random random = new Random();
		/*long postTime = new Date().getTime() - hours * hourTime ;
		
		if(lastTime != 0){
			lastTime += (1000 * 60) * (random.nextInt(8) + 3) + random.nextInt(60000); //必须和上一篇相差 3分钟以上
			postTime = lastTime;
		}*/
		long postTime = System.currentTimeMillis() - 12 * 3600 * 1000;
		Timestamp tm = new Timestamp(postTime);
		Timestamp tm2 = new Timestamp(postTime -  60 * 8 * 60 * 1000);

		post.setPostAuthor(author);
		post.setPostDate(tm);
		post.setPostDateGmt(tm2);

		post.setPostStatus("publish");
		post.setCommentStatus("open");
		post.setPingStatus("open");
		post.setToPing("");
		post.setPinged("");
		post.setPostModified(tm);
		post.setPostModifiedGmt(tm2);
		post.setPostContentFiltered("");
		post.setPostParent(0L);
		post.setPostPassword("");
		post.setGuid("");
		post.setPostMimeType("");

		post.setMenuOrder(0);
		post.setPostType("post");
		post.setCommentCount(0L);

		lastTime = postTime;
	}


	public static String getText(WpPosts post) throws Exception{
		return getText(post, false);
	}
	/**
	 * 对于一般的文章，不需要判断 OJ等等。   一般性方法。 替换掉url，删除class等
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public static String getText(WpPosts post, boolean isZhuan) throws Exception {
		//	String text = proData.text + "\n";
		String text = "";
		for(int i=0; i<post.listContent.size(); i++){
			Content content = post.listContent.get(i);
			//logger.info(content.text);
			if(content == null) continue;
			if(!content.isCode ){
				logger.info("--------------------------------");
//				content.text = content.text.replaceAll("href=\"http://.+?\"", "");

				content.text = content.text.replaceAll("class=\".+?\"", "");
				//text += ImageUtil.modeyUrlOnly(content.text, post.url, post.host);
				//ImageUtil.modifyImgHtml(content.text, post.url, Init.host, post.url, Init.baseDownLoad, post.host);
				text += ImageUtil.modifyImgHtml(content.text, new PageData(post.host,post.url));
			}
			else{
				text += "<pre class=\"brush:" + content.lang + " \">";
				text += content.text.trim();
				text += "</pre>";
			}
		}
		if(isZhuan)
			text += "From：" + post.url;
		return text;
	}

	/**
	 * 解析并存储url所对应的文章
	 * @param post
	 * @throws Exception
	 */

	public static void deepCrawl(WpPosts post) throws Exception{
		logger.info("deepCrawl post.visitedUrls : " + post.visitedUrls + "  ; post.url:" + post.url);
		if(post.getPostContent() == null){
			String text = getText(post, false);
			post.setPostContent(text);
		}
		List<LinkTag> links = MyUtil.parseTags(post.getPostContent(), LinkTag.class, "href", null);

		for(LinkTag link:links){
			logger.info("try deepCrawl url: " + link);
			String url = link.getLink().trim();
			if(url == null || url.equals("")) continue;

			//visitedUrls 记录下所有访问过的
			//visitedUrlMap 记录所有抓取过的
			if(post.visitedUrls.contains(url)){
				if(!post.visitedUrlMap.containsKey(url)){
					replaceNoneUrl(post, url);
				}else{
					post.postContent = post.postContent.replaceAll(url ,  post.visitedUrlMap.get(url));

					//post.postContent = post.postContent.replaceAll("href=[\"']"+ url +"[\"']", "href=\"" + post.visitedUrlMap.get(url)+"\"");
				}
				continue;
			}

			Spider spider = getSpider(url);
			if(spider == null){
				replaceNoneUrl(post, url);
				continue;
			}
			post.visitedUrls.add(url);
			if(!post.visitedUrlMap.containsKey(url) && post.deep < MAX_DEEP){

				PageData pd = MyUtil.getPage(url);
				if(pd != null){
					WpPosts deepPost = spider.parseArticleSUrl(pd, null);
					if(deepPost != null){
						deepPost.deep = post.deep + 1;
						deepPost.visitedUrlMap = post.visitedUrlMap;
						deepPost.visitedUrls = post.visitedUrls;
						deepCrawl(deepPost);

						ItblogUtil.saveCommPost(deepPost, false);
						if(deepPost.getPostName() != null && deepPost.getId() > 0){
							logger.info("visitedUrlMap , put : " + url + "  ->   " + deepPost.getPostName() + "-" + deepPost.getId() + ".html");
							post.visitedUrlMap.put(url,  deepPost.getPostName() + "-" + deepPost.getId() + ".html");
						}
						Thread.sleep(new Random().nextInt(1000)); //随机休息一会儿
					}
				}
			}

			if(post.visitedUrlMap.containsKey(url)){
				logger.info("replace crawled url: " + url + " ; ->  to : " + post.visitedUrlMap.get(url));
				post.postContent = post.postContent.replaceAll("href=[\"']"+ url +"[\"']", "href='" + post.visitedUrlMap.get(url)+"'");
			}else{
				replaceNoneUrl(post, url);
			}
		}

	}

	private static void replaceNoneUrl(WpPosts post, String url) {
		String crawledUrl = DbUtil.getParsedUrl(url);
		if(crawledUrl != null){
            logger.info("replace crawled url from db: " + url + " ; ->  to : " + post.visitedUrlMap.get(url));
            post.postContent = post.postContent.replaceAll(url, crawledUrl);
        }else{
            logger.info("replace url: " + url);
            post.postContent = post.postContent.replaceAll("href=[\"']"+ url +"[\"']", "");
        }
	}

	public static Spider getSpider(String url){
		if(!url.startsWith("http")) return null;
		if(url.contains("blog.csdn.net") && !url.contains("m.blog.csdn") && url.contains("article/details")){
			return new Spider4Csdn();
		}else if(url.contains("blog.51cto.com")){
			return new CtoSpider();
		}
		return null;
	}



	public static void main(String[] args) {
		/*logger.info(StringEscapeUtils.unescapeHtml4("&nbsp; &nbsp; &nbsp;").trim().replaceAll("[\\s\\u00A0]+$", ""));
		logger.info(StringEscapeUtils.unescapeHtml4("&nbsp;").equals(" "));
		logger.info((int)StringEscapeUtils.unescapeHtml4("&nbsp;").charAt(0));
		logger.info((int)' ');
		logger.info("&nbsp; &nbsp;".replaceAll("&nbsp;", "dd"));*/
//		delete("index.html");
		String orgin = "href=\"www.baidu.com\",,abcda,href=\"www.baidu.com123\",,href=\"www.baidu.com' ";
		logger.info(orgin.replaceAll("href=[\"']www.baidu.com[\"']", ""));
	}

	/**
	 * 检测是否是广告内容
	 * @param allString
	 * @return
	 */
	public static boolean checkAd(String allString) {
		for(int i=0; i<adStrings.length; i++)
			if(allString.contains(adStrings[i])) return true;
		return false;
	}


	//listkeyCnt，记录所有的关键词出现的次数。只添加最多的两个
	public static String setMainTerms(WpPosts post) {
		List<Entry<WpTermTaxonomy, Integer>> terms = post.listkeyCnt;
		//finalPost.setWpCommentses(terms);
		int cnt = 0;
		String mainKeyWord = null;

		if(terms!=null){
//			logger.info("add Term Num: " + terms.size());
			logger.info("setMainTerms, all terms num: " + terms.size());
			int cnt1=0,cnt2=0;
			for(Entry<WpTermTaxonomy, Integer> entry:terms){
				WpTerms t = entry.getKey().getTerm();
				if(entry.getKey().getTaxonomy().equals("category")){
					if(cnt1 < 1) post.getTerms().add(entry.getKey());
					cnt1++;
				}else if(entry.getKey().getTaxonomy().equals("post_tag")){
					if(cnt2 <=1)post.getTerms().add(entry.getKey());
					cnt2++;
				}
			}
		}
		return mainKeyWord;
	}

	public static Set<String> getKeysByText(String text){
		Set<String> keys = new HashSet<String>();

		text = text.toLowerCase();
		for(String key: ItblogInit.catKeySet){
			if(text.contains(key)) keys.add(key);
		}
		for(String key: ItblogInit.tagKeySet){
			if(text.contains(key)) keys.add(key);
		}
		return keys;
	}

	public static void parseKeys(WpPosts post) {
		for(String key: ItblogInit.catKeySet){
			if(post.getPostTitle().toLowerCase().contains(key)){
				post.getTerms().add(ItblogInit.catTermTaxMap.get(key));
			}
		}
		for (String tag : ItblogInit.tagKeySet) {
			if (post.getPostContent() != null ){
				if( post.getPostContent().toLowerCase().contains(tag)) {
					post.getTerms().add(ItblogInit.tagTermTaxMap.get(tag));
				}
			}else{
				if(post.listContent != null){
					for(Content content:post.listContent){
						//!content.isCode && 
						if(content.text.toLowerCase().contains(tag))
							post.getTerms().add(ItblogInit.tagTermTaxMap.get(tag));
					}
				}
			}


		}
	}

	public static void saveCommPost(WpPosts post,boolean zhuan) throws Exception{
		//	Transaction tran = HibernateSessionFactory.openCurrentSession().beginTransaction();
		WpPostsDAO pdao = new WpPostsDAO();

		ItblogUtil.setCommonPost(post, 0, ItblogInit.ojSpiderUserId);

		if(post.getPostContent() == null){
			//获得 博客的 最终 文章内容
			String text = ItblogUtil.getText(post, zhuan);
			post.setPostContent(text);
		}

		post.setPostExcerpt(post.getPostTitle());
		String posturl = MyUtil.clearTitle(post.getPostTitle());
		if(posturl.length() < 2){
			posturl = "blog-"+posturl;
		}
		post.setPostName(posturl);
		pdao.save(post);
		//logger.info(wpPosts.getId());
		post.setGuid(ItblogInit.host + "/?p=" + post.getId());
		ItblogUtil.setMainTerms(post); //设置分类目录
		if(post.getTerms().size() == 0)
			post.getTerms().add(ItblogInit.Term_other);
		pdao.save(post);

		LogDAO ldao = new LogDAO();
		Log log = new Log();
		log.setTime(new Timestamp(System.currentTimeMillis()));
		log.setUrl(post.url);
		log.setOther(post.getPostName() + "-" + post.getId());
		log.setName(post.getPostTitle() + ";" + post.host);
		ldao.save(log);

		//tran.commit();
	}


	public static void delete(String strFile){
		ItblogInit.init();
		if(strFile.equals("")) return;
		File file = new File(ItblogInit.hostPath + "wp-content/cache/supercache/www.51itong.net/" + strFile);
		logger.info("delete file : " + file.getAbsolutePath());
		MyUtil.deleteFile(file);
	}

	public static boolean isNumber(String str){
		for(int i=0; i<str.length(); i++){
			if(str.charAt(i) < '0' || str.charAt(i) > '9') return false;
		}
		return true;
	}


}
