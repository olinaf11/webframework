<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>test</title>
</head>
<body>
    <h2>Test reussi</h2>
    <ul>
      <form method="post" action="test-upload" enctype="multipart/form-data">
        <input type="text" name="id" value="101">
        <input type="file" name="fileUpload">
        <input type="text" name="nom" value="COCO">
        <input type="submit">
      </form>
      <a href="test2-list">List emp</a>
      <a href="test2-logout">Log out</a>
      <a href="test2-logout2">Log out2</a>
    </ul>
</body>
</html>
