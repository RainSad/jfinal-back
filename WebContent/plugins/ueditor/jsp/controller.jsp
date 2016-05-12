<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter,org.json.JSONObject"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	String result=new ActionEnter( request, rootPath ).exec();
	JSONObject json=new JSONObject(result);
	if(json.has("url")){
	  json.put("url", "/jfinal"+json.getString("url"));
	  out.write(json.toString());
	}else{
		rootPath=rootPath.replaceAll("\\\\", "/");
        result= result.replaceAll(rootPath, "/jfinal/");
	    out.write(result);
	}
%>