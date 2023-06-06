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
      <form method="post" action="test-upload" enctype="multipart/form-data">
        <input type="text" name="id" value="101">
        <input type="file" name="fileUpload">
        <input type="text" name="nom" value="COCO">
        <input type="submit">
      </form>
    </ul>
</body>
</html>
