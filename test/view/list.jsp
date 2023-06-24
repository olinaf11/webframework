<%
  String[] test = (String[])request.getAttribute("ls");
  String sess = (String)request.getAttribute("session");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>test</title>
</head>
<body>
    <% for(String t : test){%>
        <p><%=t%></p>
    <% } %>
    <p><%=sess%></p>
</body>
</html>
