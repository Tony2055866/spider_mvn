<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'log.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <%
		String ip = request.getRemoteHost();
		//System.out.println(ip);
  if(session.getAttribute("login") == null && !ip.equals("127.0.0.1") && !ip.equals("202.108.77.42")){
  	response.sendRedirect("index.jsp");
  }
   %>
  <body>
    <%
    String line = request.getParameter("line");
    if( line == null || line.equals("")){
    	line = "50";
    }
   Runtime run = Runtime.getRuntime();
   Process p = run.exec("tail -" + line + " " + "/server/apache-tomcat-8.0.0-RC5/logs/catalina.out");
    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));  
    while ((line = input.readLine()) != null) {  
        // System.out.println(line);  
        out.println( line + "<br>");
    }  
     %>
  </body>
</html>
