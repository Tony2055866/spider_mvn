<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.util.JdbcUtil"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page import="com.duoshuo.AddCommentLocal" %>
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
		String ip = request.getRemoteHost();
		//logger.info(ip);
  if(session.getAttribute("login") == null && !ip.equals("127.0.0.1") && !ip.equals("202.108.77.25")){
  	response.sendRedirect("index.jsp");
  }
   %>
   
   <%
   String m = request.getParameter("m");
       String comment = request.getParameter("comment");
       if(m != null){
           int mi = Integer.parseInt(m);
           logger.info(mi);
           Connection conn = JdbcUtil.getConnection();
           String sql[] = new String[]{
                   "update wp_postmeta set meta_value= FLOOR( 20+RAND() *50) where meta_key='post_views_count' and meta_value < 50;",
                   "update wp_postmeta set meta_value= FLOOR( 50+RAND() *100) where meta_key='post_views_count' and meta_value >= 50 and meta_value < 200;",
                   "update wp_postmeta set meta_value= FLOOR( 200+RAND() *300) where meta_key='post_views_count' and meta_value >= 200 and meta_value < 600;"
           };
           if(mi >= 0 && mi <= 2){
               PreparedStatement ps = conn.prepareStatement(sql[mi]);
               int res = ps.executeUpdate();
               out.println("update <br>" + res + "  <br>lines");
               logger.info(sql[mi]);
           }
           conn.close();
       }else if(comment != null){
           int com = Integer.parseInt(comment);
           AddCommentLocal.commentLimit = com;
           if(com == 0){
               AddCommentLocal.running = false;
           }
           if(AddCommentLocal.running == false){
               AddCommentLocal.main(null);
           }else{
               out.println("cnt:" + AddCommentLocal.cnt + "   all:" + AddCommentLocal.all);
           }

       }else{
            out.println("nothing!");
       }

    %>
  </body>
</html>
