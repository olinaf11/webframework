<%@ page import="model.Test"%>
<%
  Test test = (Test) request.getAttribute("test");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>test</title>
</head>
<body>
    <p><%=test.getNom()%></p>
    <p><%=test.getId()%></p>
    <p><%=test.getFileUpload().getName()%></p>
</body>
</html>
