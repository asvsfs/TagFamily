<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.io.File" %>
<%--
  Created by IntelliJ IDEA.
  User: asvsfs
  Date: 4/26/2015
  Time: 11:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
  <script src="//code.jquery.com/jquery-1.11.2.min.js"></script>

  <script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.iframe-transport.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.fileupload.js"></script>
  <!-- bootstrap just to have good looking page -->
  <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>

  <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css" type="text/css" rel="stylesheet" />
  <link href="${pageContext.request.contextPath}/css/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css"/>
  <link href="${pageContext.request.contextPath}/css/dropzone.css" type="text/css" rel="stylesheet" />

  <script src="${pageContext.request.contextPath}/js/uploadFunc.js"></script>

  <!-- header style ignore it -->
  <link href="css/mystyle.css" rel="stylesheet">
  <link href="css/style.css" rel="stylesheet">
</head>
<body>
<table id="userTagImages" class="table">
  <%

    try{
      Class.forName("com.mysql.jdbc.Driver").newInstance();

      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
              "test", "test123");

      PreparedStatement preparedStatement;
      String selectSQL = "select * from tags INNER JOIN images ON tags.imageid=images.imageid where tagname = ?";
      String test = request.getParameter("tagimage");
      if(test.compareTo("on")==0){
        selectSQL = "select * from tags INNER JOIN tagimage ON tags.tagid=tagimage.tagid where tagname = ?";
        preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setString(1, request.getParameter("tagname"));
      }else{
        preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setString(1, request.getParameter("tagname"));
      }

      ResultSet rs = preparedStatement.executeQuery();
      int i = 0 ;
      while (rs.next()) {
        File file = new File(rs.getString("imagepath"));
        int id = rs.getInt("imageid");
  %>
  <tr id="tt<%=i++%>" >
    <td>
      <a href="/tagimage.jsp?f=<%= file.getPath() %>">
        <img class='imgholder' src="/fetchimage?f=<%= file.getPath() %>"  />
      </a>
    </td>

  </tr>
  <%
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  %>

</table>

</body>
</html>
