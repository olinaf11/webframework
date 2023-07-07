<%
  String[] test = (String[])request.getAttribute("ls");
  String sess = (String)request.getAttribute("session");
  String json = (String)request.getAttribute("json");
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
    <p><%=json%></p>
    <a href="test2-json.do">Test json</a>
</body>
</html>
