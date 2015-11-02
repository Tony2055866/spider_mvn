<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.util.JdbcUtil"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.io.File"%>
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
  int i = url.indexOf("www");
  url = url.substring(i);
  logger.info(url);
  if(url.endsWith("com")) url += "/index.html";
  if(url.endsWith("/")) url += "index.html";
  	File file = new File("/server/acmerblog/wp-content/cache/supercache/" + url);
  	logger.info("/server/acmerblog/wp-content/cache/supercache/" + url);
  	if(file.exists()){
  		if(file.isDirectory())
  			out.println("isDirectory!!");
  		else
  			file.delete();
  	}else{
  		out.println("file not exits!!");
  	}
  }
   %>
  
  		<form action="">
  			<input name="url" type="text" maxlength="300"><br>
  			<input type="submit" value="删除">
  		</form>
  </body>
</html>
