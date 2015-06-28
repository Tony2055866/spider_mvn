<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.sevlets.Database"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  <%
  String username = request.getParameter("username");
  String password = request.getParameter("password");
  if(username !=  null && password != null && password != ""){
  	if(username.equals("admin") && password.equals(Database.password)){
  		session.setAttribute("login", true);
  		response.sendRedirect("manage.jsp");
  		//request.getRequestDispatcher("manage.jsp").forward(request, response);
  		return;
  	}
  }
   // out.println(request.getRealPath("/"));
   %>

  <body>
    <form method="post">
    	<input name="username" value="admin">
    	<br> <input type="password" name="password" value="">
    	<br> <input type="submit" value="登录">
    </form>
  </body>
</html>
