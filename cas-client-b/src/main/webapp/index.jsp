<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>CAS客户端2</title>
</head>
<body>
	<%
		AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
		String userName = principal.getName();
	%>
	<br />--------------------------------------------
	<br />
	<h1>登录成功，CAS客户端2。</h1>
	<br />
	<h2>
		当前登录用户：<%=userName%></h2>
	<br />
	<a href="http://sso.demo.com:8280/casclient2/home.jsp">进入客户端2</a>
	<br />
	<a href="https://sso.demo.com:8443/cas/logout">退出</a>
	<br />
	
	
<h1>CAS Example Java Web App</h1>
<p>A sample web application that exercises the CAS protocol features via the Java CAS Client.</p>
<hr>

<p><b>Authenticated User Id:</b> <a href="logout.jsp" title="Click here to log out"><%= request.getRemoteUser() %>
</a></p>

<%
    if (request.getUserPrincipal() != null) {
       // AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();

        final Map attributes = principal.getAttributes();

        if (attributes != null) {
            Iterator attributeNames = attributes.keySet().iterator();
            out.println("<b>Attributes:</b>");

            if (attributeNames.hasNext()) {
                out.println("<hr><table border='3pt' width='100%'>");
                out.println("<th colspan='2'>Attributes</th>");
                out.println("<tr><td><b>Key</b></td><td><b>Value</b></td></tr>");

                for (; attributeNames.hasNext(); ) {
                    out.println("<tr><td>");
                    String attributeName = (String) attributeNames.next();
                    out.println(attributeName);
                    out.println("</td><td>");
                    final Object attributeValue = attributes.get(attributeName);

                    if (attributeValue instanceof List) {
                        final List values = (List) attributeValue;
                        out.println("<strong>Multi-valued attribute: " + values.size() + "</strong>");
                        out.println("<ul>");
                        for (Object value : values) {
                            out.println("<li>" + value + "</li>");
                        }
                        out.println("</ul>");
                    } else {
                        out.println(attributeValue);
                    }
                    out.println("</td></tr>");
                }
                out.println("</table>");
            } else {
                out.print("No attributes are supplied by the CAS server.</p>");
            }
        } else {
            out.println("<pre>The attribute map is empty. Review your CAS filter configurations.</pre>");
        }
    } else {
        out.println("<pre>The user principal is empty from the request object. Review the wrapper filter configuration.</pre>");
    }
%>
	
	
</body>
</html>
