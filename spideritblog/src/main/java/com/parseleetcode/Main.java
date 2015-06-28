package com.parseleetcode;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


import org.apache.commons.lang3.StringUtils;

import com.itong.main.ItblogUtil;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.model.WpTermTaxonomy;
import com.util.ItblogInit;


public class Main {
	static String chaps[] = {"","chapLinearList","chapString","chapStackAndQueue","chapTree",
		"chapSorting","chapSearching","chapBruteforce","chapBFS","chapDFS","chapDivideAndConquer"
		,"chapGreedy","chapDynamicProgramming","chapGraph","chapImplement"};
	static String chapsZN[] = {"","线性表","字符串","栈","二叉树",
		"排序","搜索","枚举","BFS","DFS","分治"
		,"贪心","动态规划","图论","模拟"};
	static Map<String,Object> map = new HashMap<String,Object>();
	static Set<WpTermTaxonomy> leetTerms = new HashSet<WpTermTaxonomy>();
	public static void main(String[] args) throws Exception {
		/*map.put("chapTrick", new int[]{1,2});
		int[] tags = (int[]) map.get("");*/
		ItblogInit.init();
		List<String> keys = new ArrayList<String>();
		keys.add("leetcode");
		leetTerms = getMatchKeys(keys);
		
		String path = "E:\\学习文档\\leetcode-master\\C++\\";
		String part1,part2;
		
		
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
					//System.out.println(line);
					if(line.contains("label") && line.contains("sec:")){
						title = StringUtils.substringBetween(preLine,"section{", "}");
						url = "leetcode-" + StringUtils.substringBetween(line,"section{", "}");
						url = URLEncoder.encode(url);
						System.out.println(title);
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
//					System.out.println("-------------------");
//					System.out.println(tline);
					if(tline.trim().equals("分析")){
						txt = "";
						while(true){
							tline = textScanner.nextLine();
							if(tline.trim().startsWith("//LeetCode")){
								//contents.add(txt);
								System.out.println("----------txt----------");
								System.out.println(txt.substring(0, (txt.indexOf("代码")+txt.length())%txt.length()));
								break;
							}
							txt += tline + "\n";
						}
						
					}
					if(tline.trim().equals("描述")) break;
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
				
				System.out.println("codes.size() : " + codes.size());
				
				//找相关题目
				relates = new ArrayList<String>();
				while(s.hasNextLine()){
					String tline = s.nextLine();
					if(tline.trim().contains("item") && tline.contains("sec:")){
						relates.add(StringUtils.substringBetween(tline, "sec:", "}"));
					}
					if(tline.trim().contains("myenddot")) break;
				}
				
				WpPosts post = createPost(title, codes, txt, relates, i);
				savePost(post, i);
			}
		
		}
		
	}
	
	private static void savePost(WpPosts post,int index) {
		WpPostsDAO pdao = new WpPostsDAO();
		
		pdao.save(post);
		
		
		post.setGuid(ItblogInit.host + "/?p=" + post.getId());
		List<String> keys = new ArrayList<String>();
		keys.add(chapsZN[index]);
		post.getTerms().addAll(getMatchKeys(keys));
		
		pdao.save(post);
		
	}
	private static WpPosts createPost(String title, List<String> codes,
			String txt, List<String> relates, int index) {
		WpPosts post = new WpPosts();
		post.setPostAuthor(1l);
		ItblogUtil.setCommonPost(post, 24 * 60);
		
		LeetCode leetcode = new LeetCode(title);
		post.setPostTitle(title + "[" + chapsZN[index]+  "]");
		post.setPostName( URLEncoder.encode(("leetcode-" + title)));

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
	
	
	public static Set<WpTermTaxonomy> getMatchKeys(List<String> keys){
		
		Set<WpTermTaxonomy> set = new HashSet<WpTermTaxonomy>();
		
		for(String key: ItblogInit.catKeySet){
			WpTermTaxonomy tax = ItblogInit.catTermTaxMap.get(key);
			for(String contentKey:keys){
				if(contentKey.toLowerCase().contains(key) ){
					set.add(tax);
				}
		}
		
		}
		return set;
	}
	
	
}