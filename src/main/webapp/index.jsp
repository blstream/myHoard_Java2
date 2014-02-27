<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="com.blstream.myhoard.db.dao.CollectionDAO" %>
<%@page import="com.blstream.myhoard.db.model.CollectionDS" %>
<!DOCTYPE html>
<html>
<body>
	<h1>Team: Java2</h1>
	<p>Version: ${version}</p>

	<a href="index.jsp?collections">Wylistuj kolekcje</a><br/>

<%
	if (request.getParameter("collections") != null) {
		CollectionDAO dao = new CollectionDAO();
		out.println("\t<table border=\"1\">\n\t\t<tr><td>id</td><td>owner</td><td>name</td><td>description</td><td>tags</td><td>items_number</td><td>created_date</td><td>modified_date</td>");
		for (CollectionDS i :dao.getList())
			out.println(String.format("\t\t<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%d</td><td>%s</td><td>%s</td>", i.getId(), i.getOwner(), i.getName(), i.getDescription(), i.getTags(), i.getItemsNumber(), i.getCreatedDate(), i.getModifiedDate()));
		out.print("\t</table>");
	}
%>
</body>
</html>
