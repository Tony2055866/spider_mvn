<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.sevlets.OJTask"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="com.main.Main"%>
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
		String ip = request.getRemoteHost();
		//System.out.println(ip);
  if(session.getAttribute("login") == null && !ip.equals("127.0.0.1") && !ip.equals("202.108.77.42")){
  	response.sendRedirect("index.jsp");
  }
   %>
   
    <%
   String submit =  request.getParameter("issubmit");
     if(submit.equals("true") ){
     	if(OJTask.stop){
     	String ojType = request.getParameter("ojname");
     	     	String ojType2 = request.getParameter("ojname2");
     	     	String start = request.getParameter("start");
     	     	String end = request.getParameter("end");
     	     	String hour = request.getParameter("hour");
     	Main.delayHours = Integer.parseInt(hour);
     	OJTask task = new OJTask(ojType, ojType2, Integer.parseInt(start), 
     	Integer.parseInt(end), Integer.parseInt(hour));
     	task.start();
     		out.println("<h1>任务开始执行</h1>");
     	}else{
     		out.println("<h1>正在执行！</h1>");
     	}
     	
     }
    
     %>
     
  </body>
</html>
