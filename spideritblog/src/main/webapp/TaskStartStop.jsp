<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.itong.task.MyTask"%>
<%@page import="com.itong.task.Task51cto"%>
<%@page import="com.itong.task.TaskCsdn"%>
<%@page import="com.util.ItblogInit"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'TaskStartStop.jsp' starting page</title>
    
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
   String type = request.getParameter("type");
   String taskId = request.getParameter("taskId");
   MyTask task = null;
   if(taskId.equals("1")){
	   task = Task51cto.getTask();
   }else{
	   task = TaskCsdn.getTask();
   }
   if(type.equals("start")){
	 	ItblogInit.init();
		task.sleepTimeHours = Integer.parseInt(request.getParameter("time"));
	 	task.sleeptimePerUrl = Integer.parseInt(request.getParameter("timePerUrl"));
		task.pages = Integer.parseInt(request.getParameter("pages"));
	   if(!task.isAlive()) task.start();
		if(!task.running){
		   task.restart();
	   }
	   out.println("Started!......");
   }else{
	   task.stopTask();
	   out.println("Stoped!......");
   }
   %>
   
   
  </body>
</html>
