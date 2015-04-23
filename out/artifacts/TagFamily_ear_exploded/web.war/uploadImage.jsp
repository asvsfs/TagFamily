<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.io.File" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>

<%--
  Created by IntelliJ IDEA.
  User: asvsfs
  Date: 4/21/2015
  Time: 9:14 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
  <meta charset="utf-8">
  <title> File Upload </title>
  <script src="//code.jquery.com/jquery-1.11.2.min.js"></script>

  <script src="${pageContext.request.contextPath}/js/vendor/jquery-ui.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.iframe-transport.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.fileupload.js"></script
  <!-- bootstrap just to have good looking page -->
  <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
  <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css" type="text/css" rel="stylesheet" />

  <!-- we code these -->
  <link href="${pageContext.request.contextPath}/css/dropzone.css" type="text/css" rel="stylesheet" />
  <script src="${pageContext.request.contextPath}/js/uploadFunc.js"></script>

  <!-- header style ignore it -->
  <link href="css/mystyle.css" rel="stylesheet">
  <link href="css/style.css" rel="stylesheet">

</head>

<body>

<h1 style="text-align:center">File Upload<br></h1>

<!-- user twitter -->
<div id="user_twitter">
  <span>Your Twitter</span>
  <div class="input-prepend">
    <span class="add-on">@</span>
    <input class="span2" id="twitter" name="twitter" type="text" placeholder="Username">
  </div>
</div>
<div style="width:700px;padding:20px">

  <input id="fileupload" type="file" name="files[]" data-url="upload" multiple>

  <div id="dropzone" class="fade well">Drop files here</div>

  <div id="progress" class="progress">
    <div class="bar" style="width: 0%;"></div>
  </div>
  <h5 style="text-align:center"><i style="color:#ccc"><small>Max File Size: 2 Mb - Display last 20 files</small></i></h5>


  <table id="uploaded-files" class="table">
    <tr>
      <th>File Name</th>
      <th>File Size</th>
      <th>File Type</th>
      <th>Download</th>
      <th>Uploaded By</th>
    </tr>

    <%

    try{
      Class.forName("com.mysql.jdbc.Driver").newInstance();

      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
              "test", "test123");

        String selectSQL = "select * from images where userid = ?";

      PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
      preparedStatement.setInt(1, Integer.parseInt(session.getAttribute("userid").toString()));

      ResultSet rs = preparedStatement.executeQuery();
      int i = 0 ;
      while (rs.next()) {
        File file = new File(rs.getString("imagepath"));
        int id = rs.getInt("imageid");
    %>
    <tr id="tt<%=i++%>" >
      <td><%= file.getName() %></td>
      <td><%= file.getTotalSpace() %></td>
      <td><%= file.getName() %></td>
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
</div>
</body>
</html>