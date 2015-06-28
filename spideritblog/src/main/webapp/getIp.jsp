<%@page language="java" import="java.util.*,java.io.*,java.net.*,java.text.*" pageEncoding="UTF-8"%>
I know who you are:
<%=request.getRemoteAddr()%>
<%
String ip = request.getRemoteAddr();
Cookie[] cookies = request.getCookies();

boolean decuryCookie = false;
boolean isDGame = false;
String username = null;
for(Cookie cookie:cookies){
	if(cookie.getName().equals("COMPANY_ID") || cookie.getName().equals("COOKIE_SUPPORT")
	|| cookie.getName().equals("GUEST_LANGUAGE_ID") || cookie.getName().equals("USER_UUID")
		|| cookie.getName().equals("COOKIE_SUPPORT") || cookie.getName().equals("SCREEN_NAME")
	){
		decuryCookie = true;
	}
	
	if(cookie.getName().equals("userId") 
	){
		isDGame = true;
	}
	
	if( cookie.getName().equals("username")
	){
		isDGame = true;
		username = cookie.getValue();
	}
}

if(ip != null && ( ip.startsWith("114.255.40") || ip.startsWith("202.108.77d") || isDGame || decuryCookie)){
        File file = new File("/home/gaotong/visitIp.txt");
        if(!file.exists()){
                file.createNewFile();
        }
		
        String p = request.getParameter("p");
        String ref =  request.getHeader("referer");
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ip += " -" + p +  "  time:" + format1.format(new Date())  + "  " + ref + "  ; ";
        if(isDGame) ip += username;
        ip += "\n";
        FileOutputStream fos = new FileOutputStream(file,true);
        fos.write(ip.getBytes());
        fos.close();
}
%>
