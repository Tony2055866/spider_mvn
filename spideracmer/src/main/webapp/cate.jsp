<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.model.WpTermTaxonomyDAO"%>
<%@page import="com.dao.TermDao"%>
<%@page import="com.model.WpTermTaxonomy"%>
<%@page import="com.util.HibernateSessionFactory"%>
<br>
<%

TermDao dao = new TermDao();
List<WpTermTaxonomy> listCat = TermDao.getCateTerms();
List<WpTermTaxonomy> listTag = TermDao.getTagTerms();
 int cnt = 0;
 for(WpTermTaxonomy cat:listCat){
 %>
 	<input value="<%=cnt%>" type="checkbox" name="cat[]"/>
 <%
 out.println( cat.getTerm().getName() + "&nbsp;&nbsp;&nbsp;");
 cnt++;
 } %>
 <hr>
 <%
 cnt=0;
  for(WpTermTaxonomy cat:listTag){
%>
 	<input value="<%=cnt%>" type="checkbox" name="tag[]"/>
 <%
 	out.println( cat.getTerm().getName() + "&nbsp;&nbsp;&nbsp;");
 cnt++;
 }


  %>
