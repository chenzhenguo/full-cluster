<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>CAS客户端1</title>
</head>
<body>
	<%
		AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
		String userName = principal.getName();
	%>
	<br />--------------------------------------------
	<br />
	<h1>登录成功，CAS客户端1。</h1>
	<br />
	<h2>
		当前登录用户：<%=userName%></h2>
	<br />
	<a href="http://sso.demo.com:8180/casclient1/home.jsp">进入客户端1</a>
	<br />
	<a href="https://sso.demo.com:8443/cas/logout">退出</a>
	<br />
	
	<h3>==================================</h3>


	
</body>
</html>
