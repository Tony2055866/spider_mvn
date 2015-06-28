<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.test.ChangeDateTime"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'cmdJava.jsp' starting page</title>
    
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
    String cmd = request.getParameter("cmd");
    if(cmd.equals("1")){
    	ChangeDateTime.main(null);
    }
     %>
  </body>
</html>
