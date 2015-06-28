<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.sevlets.OJTask"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.RandomAccessFile"%>
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
    if(OJTask.ojlogFile == null) return;
    
    InputStream in = new FileInputStream(OJTask.ojlogFile);
         Scanner scanner = new Scanner(in);
    
   
     out.println(OJTask.ojlogFile.getAbsolutePath() + "<br>");
          RandomAccessFile rmf= new RandomAccessFile(OJTask.ojlogFile, "r");
     int maxlen = 20000;
   	 if(rmf.length() > 20000){
				byte[] buffer = new byte[maxlen];
				rmf.seek(in.available()-maxlen);
				rmf.read(buffer, 0, maxlen);
		    	String str = new String(buffer,"utf-8");
		    	out.println(str);
	}else{
    	while(scanner.hasNextLine()){
    	 String line = scanner.nextLine();
     	 out.println( line +"<br>");
     	}
    }
    
     scanner.close();
     in.close();
     %>
     
  </body>
</html>
