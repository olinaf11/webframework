<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="test-show">Hello Servlet</a>
<a href="test2-insert?ok=ok">Coucou</a>
<form method="get" action="test2-login">
    <input type="text" name="ok" value="COCO">
    <input type="text" name="admin" value="admin">
    <input type="submit">
</form>
</body>
</html>
