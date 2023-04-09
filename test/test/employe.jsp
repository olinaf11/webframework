<%
  String[] list = (String[]) request.getAttribute("ls");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>test</title>
</head>
<body>
    <h2>Test reussi</h2>
    <ul>
      <%for(String s : list){ %>
          <li><%=s%></li>
      <%}%>
    </ul>
</body>
</html>
