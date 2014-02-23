<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
	<h2>Hello World!</h2>
<%
	com.blstream.myhoard.db.dao.CollectionDAO dao = new com.blstream.myhoard.db.dao.CollectionDAO();
	java.util.List<com.blstream.myhoard.db.model.CollectionDS> list = dao.getList();
	for (int i = 0; i < list.size(); i++)
		out.println(list.get(i));
%>
</body>
</html>