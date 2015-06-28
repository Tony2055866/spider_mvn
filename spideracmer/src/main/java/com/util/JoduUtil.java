package com.util;

import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.model.WpTermTaxonomy;
import com.model.WpTermTaxonomyDAO;
import com.sqider.Content;
import com.sqider.PageData;
import com.sqider.PreTag;
import com.sqider.ProblemData;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Node;
import org.htmlparser.tags.DefinitionListBullet;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JoduUtil {
	static HttpContext context;
	static HttpClient client;
	public static WpTermTaxonomyDAO wtdao = new WpTermTaxonomyDAO();
	public static WpTermTaxonomy joudu =wtdao.findById(33L);
	public static WpTermTaxonomy kaoyan =wtdao.findById(107L);
	public static WpTermTaxonomy kaoyanTag =wtdao.findById(110L);
	
	
	static int dayTime = 24 *60  * 60 * 1000;
	
	static{
		 BasicHttpParams httpParams = new BasicHttpParams();
		 HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		 HttpConnectionParams.setSoTimeout(httpParams, 10000);
		 client = new DefaultHttpClient(httpParams);
		 context = new BasicHttpContext();
		HttpGet get = new HttpGet("http://ac.jobdu.com/index.php");
		
		get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		get.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get.setHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		try {
			//HttpResponse responseTest = client.execute(get, context);
			//EntityUtils.consume(responseTest.getEntity());
			HttpPost post = new HttpPost("http://ac.jobdu.com/login.php");
			 //构造最简单的字符串数据   
		     StringEntity reqEntity = new StringEntity("user_id=%E4%BB%8E%E6%AD%A4%E9%86%89&password=19902055");
		     reqEntity.setContentType("application/x-www-form-urlencoded");   
		  // 设置请求的数据   
		     post.setEntity(reqEntity);   
		     // 执行   
		     HttpResponse response = client.execute(post, context);
		     EntityUtils.consume(response.getEntity());
		     
		     response = client.execute(get, context);
		     EntityUtils.consume(response.getEntity());
		    /* String charset = null, total = "";
		     HttpEntity entity = response.getEntity();
				if (entity != null) {
					if(charset == null)
					  charset = EntityUtils.getContentCharSet(entity);
					BufferedReader br = null;
					if(charset == null || charset == "")
						 br = new BufferedReader(new InputStreamReader(entity
							.getContent()));
					else
						 br = new BufferedReader(new InputStreamReader(entity
									.getContent(), charset));
					String line = null;
					while ((line = br.readLine()) != null) {
						total += line+ "\n";
						// System.out.println(line);
					}
				}
				System.out.println(total);*/
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static List<String> getACLinks() throws Exception{
		List<String> problems = new ArrayList<String>();
		//for(int i=1; i<10; i++){
			HttpGet get = new HttpGet("http://ac.jobdu.com/profile.php?user=%E4%BB%8E%E6%AD%A4%E9%86%89");
			HttpResponse response = client.execute(get, context);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				
				BufferedReader br = null;
					 br = new BufferedReader(new InputStreamReader(entity
						.getContent()));
			
				String line = null;
				boolean flag = false;
				while ((line = br.readLine()) != null) {
					/*if(line.contains("ac.gif") || line.contains("em")){
						if(flag && line.contains("<em>")){
						  String pro = StringUtils.substringBetween(line, "<em>", "</em>");
						  //System.out.println(pro);
						  problems.add(pro);
						}
						if(line.contains("ac.gif"))
							flag = true;
						else flag = false;
						
					}*/
					if(line.contains("color-bg-1"))
						 problems.add(StringUtils.substringBetween(line, "color-bg-1\">", "</span>"));
					
					 //System.out.println(line);
				}
			//}
		}
		return problems;
	}
	
	public static ProblemData getPorblemStr(String problem) throws Exception{
		ProblemData pd = new ProblemData();
		//HttpGet get = new HttpGet("http://ac.jobdu.com/problem.php?pid=" + problem);
		//HttpResponse response = client.execute(get, context);
		
		PageData page = MyUtil.getPage("http://ac.jobdu.com/problem.php?pid=" + problem);
		List<DefinitionListBullet> titles = MyUtil.parseTags(page.html, DefinitionListBullet.class, "class", "title-hd");
		
		pd.title = titles.get(0).getStringText().trim();
		
		List<Div> divs = MyUtil.parseTags(page.html, Div.class, "class", "topic-desc-bd");
		Div body = divs.get(0);
		NodeList nlist = body.getChildren();
		String ftext = "";
		for(int i=0; i<nlist.size(); i++){
			Node node = nlist.elementAt(i);
			String text = node.toHtml();
			if(text.contains("来源")){
				pd.source = StringUtils.substringBetween(text, "txt-ul\">", "</i>");
			}
			else if(text.contains("答疑")){
				
			}else{
				ftext += text;
			}
		}
		
		ftext = ImageUtil.modifyImgHtml(ftext, 
				"http://ac.jobdu.com/problem.php",
				Init.host + "img/jiudu/", "", Init.baseDownLoad  + "/jiudu/",
				"http://ac.jobdu.com/");
		
		pd.text = ftext;
		return pd; 
	}
	
	public static List<Content> getCodes(String problem) throws Exception{
		List<Content> codes= new ArrayList<Content>();
		PageData page = MyUtil.getPage("http://ac.jobdu.com/status.php?pid=" + problem + "&user_id=%E4%BB%8E%E6%AD%A4%E9%86%89");
		List<TableRow> rows = MyUtil.parseTags(page.html, TableRow.class, null, null);
		//System.out.println(codes.get(0).getStringText());
		boolean Java = false, cpp = false;
		for(TableRow row:rows){
			String text = row.toHtml();
			if(text.contains("Accepted")){
				String sid = StringUtils.substringBetween(row.toHtml(), "<td>", "</td>").trim() ;
				if(!Java && text.contains("Java")){
					Java = true;
					codes.add( getCode(sid, "java") );
				}else if(!cpp){
					cpp = true;
					codes.add( getCode(sid, "cpp") );
				}
			}
		}
		
		return codes;
	}
	
	private static Content getCode(String sid, String string) throws Exception{
		String text = "";
		//PageData pd = MyUtil.getPage("http://ac.jobdu.com/showsource.php?sid=" + sid);
		String html = "";
		HttpGet get = new HttpGet("http://ac.jobdu.com/showsource.php?sid=" + sid);
		HttpResponse response = client.execute(get, context);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader br = null;
				 br = new BufferedReader(new InputStreamReader(entity
					.getContent()));
		
			String line = null;
			boolean flag = false;
			while ((line = br.readLine()) != null) {
				html += line + "\n";
			}
		}
		
		List<PreTag> codes = MyUtil.parseTags(html, PreTag.class, "class", null);
		text = codes.get(0).getStringText().replace("从此醉", "coder");
		//System.out.println(text);
		Content content = new Content(text, true, string);
		return content;
	}

	public static void main(String[] args) throws Exception {
		Init.init(true);
		
		List<String> acPros = getACLinks();
		int cnt=0;
		int s= 2;
		if(args.length >0)
		 s = Integer.parseInt(args[0]);
		
		for(int i=s; i<acPros.size(); i++){
			
			String pro = acPros.get(i);
	//	    String pro = "1044";
			ProblemData pd = getPorblemStr(pro);
			WpPostsDAO pdao = new WpPostsDAO();
			
			WpPosts wp = new WpPosts();
			int start = pd.title.indexOf('：');
			if(start == -1) start = pd.title.indexOf(':');
			if(start != -1){
				pd.title = pd.title.substring(start+1);
				wp.setPostTitle("九度-" + pro + "-" + pd.title + "[解题代码]");
			}else
				wp.setPostTitle("九度-"+ pd.title);
			wp.listContent = getCodes(pro);
			setCommonPost(wp);
			setTextPost(wp, pd);
			wp.setPostExcerpt(wp.getPostTitle());
			wp.setPostName("jiudu-" + pro);
			pdao.save(wp);
			
			
			wp.setGuid(Init.host + "/?p=" + wp.getId());
			if(joudu!=null)
			wp.getTerms().add(joudu);
			if(pd.source != null && ( pd.source.contains("研究生") )){
				if(kaoyanTag!=null)
				wp.getTerms().add(kaoyanTag);
				if(kaoyan!=null)
				wp.getTerms().add(kaoyan);
			}
			pdao.save(wp);
			
			Thread.sleep(1000);
			
		}
		//System.out.println(acPros.size());
	}
	
	private static void setTextPost(WpPosts wp, ProblemData pd) {
		String text = "";
		if(pd.source != null && pd.source.length() > 0)
			 text = "题目来源：" + pd.source + "\n";
		text += pd.text + "\n <hr>";
		for(Content code:wp.listContent){
			text += code.lang + " 代码如下：";
			text += "<pre class=\"brush:" + code.lang + " \">";
			text += code.text.trim();
			text += "</pre> <br>";
		}
		wp.setPostContent(text);
	}

	public static void setCommonPost(WpPosts post){
		long postTime = new Date().getTime() - dayTime/2 ;
		Timestamp tm = new Timestamp(postTime);
		Timestamp tm2 = new Timestamp(postTime -  60 * 8 * 60 * 1000);
		
		post.setPostAuthor(1L);
		post.setPostDate(tm);
		post.setPostDateGmt(tm2);
		post.setPostStatus("publish");
		post.setCommentStatus("open");
		post.setPingStatus("open");
		post.setPostAuthor(1L);
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
	}
}
