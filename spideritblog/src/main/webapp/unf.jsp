<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.main.Main"%>
<%@page import="com.main.UnfJob"%>
<%@page import="com.util.Init"%>
<%

String stop = request.getParameter("stop");
String from = request.getParameter("from");
if(from == null || from.equals("")) return;
UnfJob.from = from;
if(stop != null && stop.equals("true")){
	UnfJob.unf = false;
}else{
Init.init();
		try {
			new Thread(){
				public void run() {
					
					try {
						UnfJob.getUnfinished();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
}


if(UnfJob.unf)out.println("working ....");

%>
<a href="?stop=true">stop</a>

