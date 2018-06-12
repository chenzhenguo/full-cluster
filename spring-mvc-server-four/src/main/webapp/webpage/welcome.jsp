<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>welcome导向页</title>
<script>
  function formSubmit() {
    document.getElementById("logoutForm").submit();
  }
</script>
</head>
<body>
	<h3>welcome导向页</h3>
	<h1>标题: ${title}</h1>	
	<h1>消息 : ${message}</h1>	
</body>
</html>