<%@page contentType="text/html; charset=UTF-8" %>

<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="org.hibernate.SessionFactory, org.hibernate.Session" %>
<%@page import="com.blstream.myhoard.db.dao.*" %>
<%@page import="com.blstream.myhoard.db.model.*" %>
<%@page import="java.util.List, java.util.Arrays" %>
<!DOCTYPE html>
<html>
    <body>
        <h1>Team: Java2</h1>
        <p>Version: ${version}</p>
        <p>Commit hash: ${commitNumber}</p>

<!--        <a href="index.jsp?kill">Ubij procesy</a><br/>
        <a href="index.jsp?collections">Wylistuj kolekcje</a><br/>
        <a href="index.jsp?media">Wylistuj media</a><br/>-->
        <a href="${pageContext.request.contextPath}/uploadFile.jsp">Go to uploadFile.jsp</a><br/>

        <%
/*            ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            SessionFactory sessionFactory = (SessionFactory)context.getBean("sessionFactory");
            if (request.getParameter("kill") != null) {
                Session sesja = sessionFactory.getCurrentSession();
                int n = 0;
                sesja.beginTransaction();
                List<Object[]> list = (List<Object[]>)sesja.createSQLQuery("select Id, Info from information_schema.processlist;").list();
                list.remove(0);
                for (Object[] i : list)
                    if (!"show processlist".equals(i[1]) && !"select Id, Info from information_schema.processlist".equals(i[1])) {
                        sesja.createSQLQuery("kill " + i[0] + ";").executeUpdate();
                        n++;
                    }
                sesja.getTransaction().commit();
                out.println("Liczba ubitych procesów: " + n + "<br/>");
            }
            if (request.getParameter("collections") != null) {
                CollectionDAO dao = new CollectionDAO();
                dao.setSessionFactory(sessionFactory);
                out.println("\t<table border=\"1\">\n\t\t<tr><td>id</td><td>owner</td><td>name</td><td>description</td><td>tags</td><td>items_number</td><td>created_date</td><td>modified_date</td></tr>");
                for (CollectionDS i : dao.getList())
                    out.println(String.format("\t\t<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%d</td><td>%s</td><td>%s</td></tr>", i.getId(), i.getOwner(), i.getName(), i.getDescription(), i.getTags(), i.getItemsNumber(), i.getCreatedDate(), i.getModifiedDate()));
                out.print("\t</table>");
            } else if (request.getParameter("media") != null) {
                MediaDAO dao = new MediaDAO();
                dao.setSessionFactory(sessionFactory);
                out.println("\t<table border=\"1\">\n\t\t<tr><td>id</td><td>created_date</td></tr>");
                for (MediaDS i : dao.getList())
                    out.println(String.format("\t\t<tr><td>%d</td><td>%s</td></tr>", i.getId(), i.getCreatedDate()));
                out.print("\t</table>");
            }
*/        %>
    </body>
</html>
