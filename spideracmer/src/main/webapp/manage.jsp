<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.sevlets.OJTask"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>My JSP 'manage.jsp' starting page</title>
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
		//logger.info(ip);
  if(session.getAttribute("login") == null && !ip.equals("127.0.0.1") && !ip.equals("202.108.77.42")){
  	response.sendRedirect("index.jsp");
  }
   %>
		输入URLs添加文章：
		<br>
		<form action="addArticle.jsp" method="post">
			<input type="hidden" name="issubmit" value="true">
			<textarea type="text" name="url" cols="120" rows="5"></textarea>
			<br>
			<br>
			输入作者博客URL添加，添加整个博客的文章：
			<br>
			<input type="text" name="author_url" style="width: 800px"><br>
			<br>
			时差:
			<input type="text" name="hour" value="0">
			固定链接:
			<input type="text" name="wpurl" value="">
			是否自动分类 : Yes:
			<input type="radio" value="yes" name="isAddTag">
			No:
			<input type="radio" value="no" name="isAddTag" checked="checked">|
			转载,是:
			<input type="radio" value="yes" name="isZhuan" checked="checked">
			否:
			<input type="radio" value="no" name="isZhuan" >
			<input type="submit" value="start">
			<jsp:include page="cate.jsp"></jsp:include>
		</form>
		<hr>
		<br>
		<br>


		<hr>
		<br>
		<br>
		输入OJ 和题目范围，自动搜索：
		<br>
		<form action="addOj.jsp" method="post">
			OJ:
			<input type="text" name="ojname" value="hdu">
			OJbak:
			<input type="text" name="ojname2" value="hoj">
			<br>
			START:
			<input type="text" name="start" value="1000">
			END:
			<input type="text" name="end" value="4500">
			时差:
			<input type="text" name="hour" value="24">
			<input type="hidden" name="issubmit" value="true">
			<input type="submit" value="start">
			<a href="stop.jsp?task=oj">停止任务</a>
		</form>
		<%
   if(OJTask.stop) out.println("任务已停止！");
   else out.println("任务进行中............");
    %>
		<br>
		Log:
		<a href="ojlog.jsp">日志查看</a>
		
		
		<hr/>
		<a href="addcnt.jsp?m=0">1-50</a>
		<a href="addcnt.jsp?m=1">200-</a>
		<a href="addcnt.jsp?m=2">600-</a>
		
		<a href="cmdJava.jsp?cmd=1">chageTime</a>
		
	</body>
</html>
