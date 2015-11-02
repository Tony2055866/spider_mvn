package com.parseleetcode;

import com.main.Util;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.model.WpTermTaxonomy;
import com.util.Init;
import com.util.MyUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;

public class LeetCodeMain {
	static String chaps[] = {"","chapLinearList","chapLinearList","chapString","chapStackAndQueue","chapTree",
		"chapSorting","chapSearching","chapBruteforce","chapBFS","chapDFS","chapDivideAndConquer"
		,"chapGreedy","chapDynamicProgramming","chapGraph","chapImplement"};
	static String chapsZN[] = {"","线性表","字符串","栈","二叉树",
		"排序","搜索","枚举","BFS","DFS","分治"
		,"贪心","动态规划","图论","模拟"};
	static Map<String,String> catMap = new HashMap<String,String>();
	static Set<WpTermTaxonomy> leetTerms = new HashSet<WpTermTaxonomy>();
	
	static File outMapFile = null;
	static PrintWriter pw;
	static Logger logger = Logger.getLogger(LeetCodeMain.class);
	
	static Set<String> finSet = new HashSet<String>();
	static Map<String,String> finSetMap = new HashMap<String,String>();
	public static void main(String[] args) throws Exception {
		Init.init();
		outMapFile = new File(Init.ojsourcesDir + File.separator + "leetcodeMapUrl.txt");
		
		if(!outMapFile.exists()) outMapFile.createNewFile();
		
		Scanner s = new Scanner(outMapFile);
		while(s.hasNext()){
			String l = s.nextLine();
			String data[] = l.split(";");
			if(data.length == 2){
				finSet.add(data[1].trim());
				finSetMap.put(data[1].trim(), data[0].trim());
			}
		}
		
		pw = new PrintWriter(new FileOutputStream(outMapFile,true));
		if(Init.catTermTaxMap.get("leetcode") != null)
			leetTerms.add( Init.catTermTaxMap.get("leetcode") );
		if(Init.tagTermTaxMap.get("leetcode") != null)
			leetTerms.add( Init.tagTermTaxMap.get("leetcode") );
		//logger.info("catKeySet : " + Init.catKeySet);
		//work();
		//workNew();
		//getAll();
		//logger.info(getMatchKey("暴力枚举"));
	
		UpdateAll();
	}
	
	public static void UpdateAll(){
		
		
		
		try {
		Scanner s = new Scanner(MyUtil.getPage("http://www.acmerblog.com/leetcode_problems.txt").html);
		int i=0;
		Scanner codeScanner = null;
		
		
		while(s.hasNextLine()){
			String title = s.nextLine().trim();
			if(title.length() == 0) continue;
			if(title.indexOf('.') != -1)
				title = title.substring(0,title.indexOf('.')).trim();
			
			if(title.startsWith("title")){
				i++;
				codeScanner = new Scanner(new File(Init.ojsourcesDir + File.separator +
						"leetcpptext" + File.separator + chaps[i] + ".tex"));
				logger.info("------------------------------------" + chaps[i]);
//				while(codeScanner.hasNext()){
//					if(codeScanner.nextLine().contains("\\label{sec")) break;
//				}
				continue;
			}
			if(i == 8) continue;
			if(title.equals("Unique Paths") || title.equals("Unique Paths II")) continue;
			
			if(finSetMap.get(title) == null) continue;
			String urls[] = finSetMap.get(title).split(";")[0].split("-");
			String idstr = urls[urls.length-1];
			WpPostsDAO dao = new WpPostsDAO();
			
			WpPosts post = dao.findById(Long.parseLong(idstr));
			dao.getSession().close();

			String allCodes = "";
			codeScanner.reset();
			
			while(codeScanner.hasNext()){
				String line = codeScanner.nextLine();
				logger.info(line);
				if(line.contains("section") && line.contains(title)) break;
			}
			
			
			logger.info("---------------------");
			logger.info(title);
			if(!codeScanner.hasNextLine()) continue;
			logger.info(codeScanner.nextLine());

				//logger.info("line:" + line);
					while(codeScanner.hasNext()){
						String line = codeScanner.nextLine();
						
						if(line.contains("section") && line.contains("分析"))
							break;
					}
					
					String txt = "";
					while(codeScanner.hasNext()){
						String line = codeScanner.nextLine();
						if(line.startsWith("\\subsubsection"))
							break;
						txt += line + "\n";
					}
					
					while(codeScanner.hasNext()){
						String line = codeScanner.nextLine();
						if(line.contains("相关题目") && line.contains("section")){
							break;
						}
						allCodes += (line + "\n");
					}
					
					//方法2：用\fn{one}记录到当前处理的元素为止，二进制1出现“1次”（mod 3 之后的
					String allitems = "";
					while(codeScanner.hasNext()){
						String line = codeScanner.nextLine();
						allitems += (line + "\n");
						if(line.contains("\\myenddot")){
							break;
						}
					}
			//logger.info("txt :" + txt);
			
			String codes[] = StringUtils.substringsBetween(allCodes, "begin{Code}", "\\end{Code}");
			String items[] = StringUtils.substringsBetween(allitems, "item", "，");
			
			txt = txt.replaceAll("\\\\fn", "").replaceAll("\\\\myurl", "").replaceAll("\\\\code", "")
			.replaceAll("\\\\begindot", "").replaceAll("\\\\myenddot", "");
			//UpdateLeetCode.update(post,codes, items, txt ,dao);
			String text = post.getPostContent();
			txt = StringEscapeUtils.escapeHtml4(txt);
			String code ="";
			for(int k=0; k<codes.length; k++){
				if(codes[k].contains("class") && codes[k].contains("Solution")){
					code += "代码" + (k+1);
					code += "<pre class='brush:cpp'>" + StringEscapeUtils.escapeHtml4(codes[k]) + "</pre>\n";
				}
			}
			text = text.replace("<p>无</p>", "<p>"+ txt +"</p>\n" + code );
			if(items!= null && items.length > 0){
				text += "<strong>相关题目</strong><br>\n";
				for(int k=0; k<items.length; k++){
					String url = LeetCodeMain.finSetMap.get(items[k].trim());
					text += "<a href='/" + url + ".html'>" + items[k].trim() + "</a>\n";
				}
			}
			//logger.info("update ------------------------------------");
			//logger.info(text);
			
			post.setPostContent(text);
			dao.save(post);
			
			
			try {
				//logger.info(codes[0]);
				/*if(items != null && items.length > 0)
					logger.info("相关：" + items[0]);
				else
					logger.info(allitems);*/
			} catch (Exception e) {
				logger.info("-----------");
				logger.info(allCodes);
				
				e.printStackTrace();
				return;
			}
			
			
		}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void getAll() {
		// TODO Auto-generated method stub
		for(String title: LeetCode.map.keySet()){
			
			if(finSet.contains(title)) continue;
			
			LeetCode leet = new LeetCode(title);
			leet.initCode();
			
			WpPosts post = new WpPosts();
			post.setPostAuthor(1l);
			Util.setCommonPost(post, 24 * 60);
			if( catMap.get(title) != null)
				post.setPostTitle("LeetCode-" + title + "[" + catMap.get(title)+  "]");
			else post.setPostTitle("LeetCode-" +title + "题解");
			
			post.setPostName( ("leetcode-solution-" + MyUtil.clearTitleToUrl(title)));

			String content = "<h3><strong>"+ title +"</strong></h3>" + "\n";
			
			String des;
			try {
				des = leet.getDes();
			} catch (Exception e) {
				continue;
			}
			if(des == null) continue;
			content += des + "\n";
			content +="<strong>分析</strong><br>";
			content += "<p>" + "无" + "</p>";
			
			if(leet.javaCode != null){
				content += "Java代码:";
				content += "<pre class='brush:java'>" + leet.javaCode + "</pre>";
			}
			if(leet.pythonCode != null){
				content += "Python代码:";
				content += "<pre class='brush:python'>" + leet.pythonCode + "</pre>\n";
			}
			post.setPostContent(content);
			post.setPostExcerpt(post.getPostTitle());
			
			post.getTerms().addAll(leetTerms);
			logger.info("post.getTerms() :" + post.getTerms());
			
			WpPostsDAO pdao = new WpPostsDAO();
			pdao.save(post);
			post.getTerms().addAll(getMatchKey(catMap.get(title)));
			logger.info("post.getTerms() :" + post.getTerms());
			post.setGuid(Init.host + "/?p=" + post.getId());
			pdao.save(post);
			
			pw.println(post.getPostName()  + "-" + post.getId() + " ;  " + title);
			pw.flush();
		}
		
		pw.close();
		                                        
	}

	
	//生成目录用
	public static void workNew(){
		
		//logger.info(LeetCode.map.keySet());
		try {
			Scanner s = new Scanner(MyUtil.getPage("http://www.acmerblog.com/leetcode_problems.txt").html);
			String artile = "";
			String lastTitle = "";
			while(s.hasNextLine()){
				String line = s.nextLine().trim();
				if(line.length() == 0) continue;
				if(line.indexOf('.') != -1)
					line = line.substring(0,line.indexOf('.')).trim();
				String url = MyUtil.clearTitleToUrl(line);
				//logger.info(url);
				
				if(line.startsWith("title")){
					artile += "<h2><strong>" + line.substring(5)+ "</strong></h2>\n";
					 lastTitle = line.substring(5);
				}else{
					if(LeetCode.map.get(line) == null){
						logger.info(line);
						throw new Exception("woqu a ");
					}
					catMap.put(line, lastTitle);
					artile += "<a href='/"+ finSetMap.get(line) +".html'>"  + line+ "</a> &nbsp;";
				}
			}
			logger.info(artile);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void work() throws FileNotFoundException {
		String path = "E:\\学习文档\\leetcode-master\\C++\\";
		
		List<List<WpPosts>> chapsList = new ArrayList<List<WpPosts>>();
		
		Scanner textScanner = new Scanner(new File("D:\\workspace\\ACMER\\parsePDF\\leetcode.txt"));
		textScanner.useDelimiter("\r\n");
		for(int i=1; i<2; i++){
			List<WpPosts> list = new ArrayList<WpPosts>();
			chapsList.add(list);
			Scanner s = new Scanner(new File(path + chaps[i] + ".tex"));
			
			String title = "";
			String curContent = "";
			String preLine = "";
			String url = "";
			String txt = "";
			String code = "";
			//List<String> contents;
			List<String> codes;
			List<String> relates;
			
			s.useDelimiter("\r\n");
			
			while(textScanner.hasNextLine()){
				String tline = textScanner.nextLine();
				if(tline.trim().equals("描述")) break;
			}
			
			while(s.hasNextLine()){
				
				while(s.hasNextLine()){
					String line = s.nextLine();
					//logger.info(line);
					if(line.contains("label") && line.contains("sec:")){
						title = StringUtils.substringBetween(preLine, "section{", "}");
						url = "leetcode-" + StringUtils.substringBetween(line, "section{", "}");
						url = URLEncoder.encode(url);
						logger.info("Find title : " + title);
						curContent = "";
						break;
					}
					curContent += line + "\n";
					preLine = line;
				}
				
				
				//找分析
				//contents = new ArrayList<String>();
				while(textScanner.hasNextLine()){
					String tline = textScanner.nextLine();
//					logger.info("-------------------");
//					logger.info(tline);
					if(tline.trim().equals("分析")){
						txt = "";
						while(true){
							tline = textScanner.nextLine();
							if(tline.trim().startsWith("//LeetCode")){
								//contents.add(txt);
								logger.info("----------txt----------");
								logger.info(txt.substring(0, (txt.indexOf("代码")+txt.length())%txt.length()));
								break;
							}
							txt += tline + "\n";
						}
						
					}
					if(tline.trim().equals("描述")) break;
				}
				
				while(s.hasNextLine()){
					String tline = s.nextLine();
					if(tline.contains("分析")) break;
				}
				
				//找代码
				codes = new ArrayList<String>();
				while(s.hasNextLine()){
					String tline = s.nextLine();
					if(tline.trim().endsWith("begin{Code}")){
						code = "";
						while(true){
							tline = s.nextLine();
							if(tline.trim().endsWith("end{Code}")){
								codes.add(code);
								code = "";
								break;
							}
							code += tline + "\n"; 
						}
						
					}
					if(tline.trim().contains("相关题目")) break;
				}
				
				logger.info("codes.size() : " + codes.size());
				
				//找相关题目
				relates = new ArrayList<String>();
				while(s.hasNextLine()){
					String tline = s.nextLine();
					if(tline.trim().contains("item") && tline.contains("sec:")){
						relates.add(StringUtils.substringBetween(tline, "sec:", "}"));
					}
					if(tline.trim().contains("myenddot")) break;
				}
				
				//WpPosts post = createPost(title, codes, txt, relates, i);
				//savePost(post, i);
				logger.info("-------------------------------");
				logger.info("title :" + title);
				logger.info("txt :" + txt);
				//if(codes.size() > 0)
					logger.info("codes :" + codes.get(0));
				
				
			}
		
		}
	}
	
	private static void savePost(WpPosts post,int index) {
		WpPostsDAO pdao = new WpPostsDAO();
		post.setTerms(leetTerms);
		pdao.save(post);
		
		post.setGuid(Init.host + "/?p=" + post.getId());
		List<String> keys = new ArrayList<String>();
		keys.add(chapsZN[index]);
		
		
		pdao.save(post);
		
	}
	private static WpPosts createPost(String title, List<String> codes,
			String txt, List<String> relates, int index) {
		WpPosts post = new WpPosts();
		post.setPostAuthor(1l);
		Util.setCommonPost(post, 24 * 60);
		
		LeetCode leetcode = new LeetCode(title);
		post.setPostTitle(title + "[" + chapsZN[index]+  "]");
		post.setPostName( ("leetcode-solution-" + MyUtil.clearTitleToUrl(title)));

		LeetCode leet = new LeetCode(title.trim());
		
		String content = "<h3><strong>"+ title +"</strong></h3>" + "\n";
		content += leet.getDes() + "\n";
		content +="<strong>分析</strong><br>";
		content += "<p>" + txt + "</p>";
		for(int i=1; i<codes.size(); i++){
			content += "代码" + i + ":";
			String code = codes.get(i-1);
			content += "<pre class='brush:cpp'>" + code + "</pre>";
		}
		leet.initCode();
		if(leet.javaCode != null){
			content += "Java代码:";
			content += "<pre class='brush:java'>" + leet.javaCode + "</pre>";
		}
		if(leet.pythonCode != null){
			content += "Java代码:";
			content += "<pre class='brush:python'>" + leet.pythonCode + "</pre>";
		}
		post.setPostContent(content);
		post.setPostExcerpt(post.getPostTitle());
		return post;
	}
	
	
	public static Set<WpTermTaxonomy> getMatchKey(String cat){
		
		Set<WpTermTaxonomy> set = new HashSet<WpTermTaxonomy>();
		if(cat == null) return set;
		logger.info("getMatchKey : " + cat);
		for(String key: Init.catKeySet){
			WpTermTaxonomy tax = Init.catTermTaxMap.get(key);
			//for(String contentKey:keys){
				if(key.toLowerCase().equals(cat.toLowerCase()) ){
					set.add(tax);
				//}
		}
		}
		
		for(String key: Init.tagKeySet){
			WpTermTaxonomy tax = Init.tagTermTaxMap.get(key);
				if(key.toLowerCase().equals(cat.toLowerCase()) ){
					set.add(tax);
		}
		}
		return set;
	}
	
	
}