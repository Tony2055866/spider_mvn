<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.sevlets.OJTask"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="com.main.AddByArticleUrl"%>
<%@page import="com.model.WpTermTaxonomy"%>
<%@page import="com.dao.TermDao"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'addOj.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
  </head>
  
  <body>
    <%
   
    //接收  usls，每个url是一篇文章
   String submit =  request.getParameter("issubmit");
     if(submit.equals("true") ){
     
     	String url = request.getParameter("url");
     	//System.out.println(urls);
     	String urls[] = url.split(";");
     	String cats[] = request.getParameterValues("cat[]");
     	String tags[] = request.getParameterValues("tag[]");
     	Set<WpTermTaxonomy> set = new HashSet();
     	if(cats != null){
     		for(int i=0; i<cats.length; i++){
     			int index = Integer.parseInt(cats[i]);
     			set.add( TermDao.getCateTerms().get(index) );
     		}
     	}
     	if(tags != null){
     		for(int i=0; i<tags.length; i++){
 //System.out.println( "TermDao.getTagTerms()" + TermDao.getTagTerms().size() + " tags[i]  " + tags[i] );
     			int index = Integer.parseInt(tags[i]);
     			set.add( TermDao.getTagTerms().get(index) );
     		}
     	}
     	int hours = Integer.parseInt( request.getParameter("hour") );
     	String isAddTag = request.getParameter("isAddTag");
 System.out.println("isAddTag: " + isAddTag);
     	String wpurl =  request.getParameter("wpurl"); //文章的固定链接
     	String isZhuan = request.getParameter("isZhuan");
     	AddByArticleUrl.isZhuan = isZhuan.equals("yes");
     	
     	//逐个 获取每个文章url
     	if(url != null && url.trim().length() > 1){
     		for(int i=0; i<urls.length; i++){
     			if(wpurl!=null && !wpurl.trim().equals("")) wpurl += (i+1);
     			AddByArticleUrl.addbyUrl(urls[i], set, hours, wpurl, isAddTag.equals("yes"));
     		}
     	}
     	
     	//根据 文章列表页面的URL，逐个获取买个url
     	String author_url = request.getParameter("");
     	if(author_url != null && author_url.trim().length() > 1){
     		AddByArticleUrl.addByAuthor_url( author_url, set, hours, wpurl, isAddTag.equals("yes"));
     	}
     }
  
     %>
     成功！！
     <a href="manage.jsp">返回</a>
  </body>
</html>
