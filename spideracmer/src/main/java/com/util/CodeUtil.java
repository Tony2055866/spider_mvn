package com.util;

import org.apache.commons.io.IOUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;

import java.io.FileInputStream;
import java.util.Scanner;

public class CodeUtil {

	
	public static String getCode(String html){
		
		html = html.replaceAll("<br\\s*/*>", "\n");
		StringBuffer sb = new StringBuffer();
		try {
			Parser parser = new Parser();
			parser.setInputHTML(html);
			
			NodeList nodeList = parser.parse(new NodeFilter() {
				@Override
				public boolean accept(Node node) {
					// TODO Auto-generated method stub
					return true;
				}
			});
			
			for(int i=0; i<nodeList.size(); i++){
				Node node = nodeList.elementAt(i);
				if(node instanceof TextNode){
					sb.append(node.toPlainTextString());
				}

			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		String cleanCode = "";
		Scanner s = new Scanner(sb.toString());
		String line;
		if(s.hasNextInt() && s.hasNextLine()){
			int first = s.nextInt();
			if(s.hasNextLine()){
				cleanCode = s.nextLine() + "\n";
			}
			if(first == 1){
				while(s.hasNextInt()&& s.hasNextLine()){
					
					first = s.nextInt();
					line = s.nextLine();
					cleanCode += line + "\n";
				}
				return cleanCode;
			}else{
				return sb.toString();
			}
		
		}
		
		return sb.toString();
	}
	
	public static String getLang(String code){
		if(code == null) return "cpp";
		code = code.toLowerCase();
		if(code.contains("#include")) return "cpp";
		if(code.contains("import")) return "Java";
		if(code.contains("public static void")) return "Java";
		if(code.contains("System.out.println")) return "Java";
		if(code.contains("System.in")) return "Java";
		if(code.contains("using namespace")) return "cpp";
		if(code.contains("<?php")) return "php";
		if(code.contains("scanf")) return "cpp";
		if(code.contains("end program")) return "Fortran";
		if(code.contains("end  program")) return "Fortran";
		if(code.contains("typename ")) return "cpp";
		if(code.contains("struct ")) return "cpp";
		//if(code.contains("end") && code.contains("end")) return "Pascal";
		return "other";
	}
	//static String allLang = "AppleScriptAS3BashColdFusionCppCSharpCssDelphiDiffErlangGroovyJava";
//	public static boolean checkbrush(String lang){
//		return allLang.contains(lang.toLowerCase());
//	}
	
	public static void main(String[] args) {
		try {
			String testHtml = IOUtils.toString(new FileInputStream("E:\\Workspaces\\ACMER\\spider-hib\\src\\test.txt"));
			//测试获取代码
			/*
			System.out.println(testHtml);
			System.out.println(getCode(testHtml));
			
			System.out.println(StringEscapeUtils.escapeHtml4(testHtml));*/
			Scanner s = new Scanner(testHtml);
			String total = "";
			String line;
			if(s.hasNextInt()){
				int first = s.nextInt();
				total = s.nextLine() + "\n";
				if(first == 1){
					while(s.hasNextInt()){
						
						first = s.nextInt();
						line = s.nextLine();
						total += line + "\n";
					}
				}else{
				
				}
				
				
			}
			System.out.println(total);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
