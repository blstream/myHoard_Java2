<html>
<body>
	<h2>Hello World!</h2>
<%
//	org.hibernate.SessionFactory sessionFactory = new org.hibernate.cfg.Configuration()
//		.configure("hibernate.cfg.xml").buildSessionFactory();
	com.java2.model.CollectionDAO dao = new com.java2.model.CollectionDAO();
//	com.java2.model.CollectionDS c = new com.java2.model.CollectionDS();
//
//	c.setOwner("NN");
//	c.setName("Pierwsza kolekcja");
//	c.setDescription("Kolekcja utworzona na potrzeby testu");
//	c.setTags("");
//	c.setItemsNumber(0);
//	c.setCreatedDate(java.sql.Timestamp.valueOf("2000-01-01 00:00:00"));
//	c.setModifiedDate(java.sql.Timestamp.valueOf("2000-01-01 00:00:00"));
//
//	dao.create(c);
//	out.println("Dodano nowy obiekt, otrzymano jego id: " + c.getId());

//	dao.remove(2);

	java.util.List<com.java2.model.CollectionDS> list = dao.getList();
	for (int i = 0; i < list.size(); i++)
		out.println(list.get(i));
%>
</body>
</html>