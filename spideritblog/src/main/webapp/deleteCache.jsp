<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.util.JdbcUtil"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.io.File"%>
<%@page import="com.util.MyUtil"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'addcnt.jsp' starting page</title>
    
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
  String url = request.getParameter("url");
  
  
  if(url!=null && url.length() > 0){
  //int i = url.indexOf("www");
  //url = url.substring(i);
  //System.out.println(url);
  if(url.endsWith("/")) url = url.substring(0, url.length()-1);
  if(url.endsWith("com") || url.endsWith("net")){
	  url = url.substring(url.indexOf("www")) + "/index.html";
  }
  else{
	  url = url.substring(url.indexOf("www")); 
  }
  
  //if(url.endsWith("/")) url += "index.html";
  	
  File file = new File("/server/acmerblog/wp-content/cache/supercache/" + url);
  	if(request.getParameter("site").equals("2")){
  		 file = new File("/server/itblog/wp-content/cache/supercache/" + url);
  	}
  	out.println("file path:" + file.getAbsolutePath() + "<br>");
  	
  	if(file.exists()){
  		if(file.isDirectory() && !file.getAbsolutePath().endsWith("com") && !file.getAbsolutePath().endsWith("net")){
  			MyUtil.deleteFile(file);
  		}
  		else
  			file.delete();
  	}else{
  		out.println("file not exits!!");
  	}
  }
   %>
  <br>
  		acm之家:<form action="" method="post">
  			<input name="url" type="text" size="100"  ><br>
  			<input type="submit" value="删除">
  			<input name="site" value="1" type="hidden">
  		</form>
  		<br>
  		itong:<form action="" method="post">
  			<input name="url" type="text" size="100"><br>
  			<input type="submit" value="删除">
  			<input name="site" value="2" type="hidden">
  		</form>
  </body>
</html>
