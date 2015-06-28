<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.sevlets.OJTask"%>
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
		<title>My JSP 'manage.jsp' starting page</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function onload(){
			var catdiv = document.getElementById("catDiv");
			//alert(catdiv);
		}
		function autoTagClick(){
			var catdiv = document.getElementById("catDiv");
			catdiv.style.display="none";
			
		}
		function addTagClick(){
			var catdiv = document.getElementById("catDiv");
			catdiv.style.display="block";
			//alert(catdiv)
		}
	</script>
	</head>

	<body onload="onload()">
		<%
		//session.setAttribute("task1",Task51cto.getTask());
		//session.setAttribute("task2",TaskCsdn.getTask());
			String ip = request.getRemoteHost();
				//System.out.println(ip);
		  if(session.getAttribute("login") == null){
		  	response.sendRedirect("index.jsp");
		  }
			String tmp = null;
			if( (tmp=request.getParameter("reload")) != null && !tmp.equals("")){
				ItblogInit.inited = false;
				ItblogInit.init();
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
			<input type="radio" value="yes" name="isAddTag" checked="checked" onclick="autoTagClick()">
			No:
			<input type="radio" value="no" name="isAddTag" onclick="addTagClick()">|
			转载,是:
			<input type="radio" value="yes" name="isZhuan" checked="checked">
			否:
			<input type="radio" value="no" name="isZhuan" >
			<input type="submit" value="start">
			<div id="catDiv" name="catDiv" style="display: none">
				<jsp:include page="cate.jsp"></jsp:include>
			</div>
		</form>
		<hr>
		<br>
		<br>

	<hr>
	<font color="red">51CTO</font>的爬虫； 状态： <%=Task51cto.getTask().running ? "<font color='green'>运行中。。</font>":"<font color='red'>停止</font>"%>；
	运行时间：<%=Task51cto.getTask().startTime %>;<br>
	已抓取的数量： <%=Task51cto.getTask().count%>; 剩余:  <%=Task51cto.getTask().remain%>
	<div>
		<form action="TaskStartStop.jsp" style="display: inline;">
		
			间隔抓取时间(小时): <input type="text" value="<%=Task51cto.getTask().sleepTimeHours %>" name="time">
			  每篇文章间隔(秒):  <input type="text" value="<%=Task51cto.getTask().sleeptimePerUrl %>" name="timePerUrl"> 
			  抓取页数 <input type="text" name="pages" value="<%=Task51cto.getTask().pages %>"> 
			<input type="hidden" name="taskId" value="1"> <input type="hidden" name="type" value="start">
			<input type="submit" value="Start">
		</form>
		<form action="TaskStartStop.jsp" style="display: inline;">
			<input type="hidden" name="taskId" value="1"> <input type="hidden" name="type" value="stop">
			<input type="submit" value="Stop">
		</form>
	</div>
	
	<br>
	<font color="red">CSDN</font>的爬虫； 状态： <%=TaskCsdn.getTask().running ? "<font color='green'>运行中。。</font>":"<font color='red'>停止</font>"%>；
	运行时间：<%=TaskCsdn.getTask().startTime %>;<br>
	已抓取的数量： <%=TaskCsdn.getTask().count%>；剩余:  <%=TaskCsdn.getTask().remain%>
	<div>
		<form action="TaskStartStop.jsp" style="display: inline;">
		间隔抓取时间(小时): <input type="text" value="<%=TaskCsdn.getTask().sleepTimeHours %>" name="time">
			  每篇文章间隔(秒):  <input type="text" value="<%=TaskCsdn.getTask().sleeptimePerUrl %>" name="timePerUrl"> 
			  抓取页数 <input type="text" name="pages" value="<%=TaskCsdn.getTask().pages %>"> 
			<input type="hidden" name="taskId" value="2"> <input type="hidden" name="type" value="start">
			<input type="submit" value="Start">
		</form>
		
		<form action="TaskStartStop.jsp" style="display: inline;">
			<input type="hidden" name="taskId" value="2"> <input type="hidden" name="type" value="stop">
			<input type="submit" value="Stop">
		</form>
	</div>
	
	<hr>
	
	重新初始化：<a href="?reload=true">Reload</a> 
	</body>
</html>
