<%@ page import ="java.sql.*" %>
<%@ page import="org.jose4j.jwt.JwtClaims" %>
<%@ page import="org.jose4j.json.internal.json_simple.JSONObject" %>
<%@ page import="java.util.*" %>

<%
  String userid = request.getParameter("uname");
  String pwd = request.getParameter("pass");
  Class.forName("com.mysql.jdbc.Driver");
  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
          "test", "test123");
  Statement st = con.createStatement();
  ResultSet rs;
  rs = st.executeQuery("select userid from users where username='" + userid + "' and password='" + pwd + "'");
  if (rs.next()) {


    //generate token
    session.setAttribute("userid", userid);
    //out.println("welcome " + userid);
    //out.println("<a href='logout.jsp'>Log out</a>");
    response.sendRedirect("success.jsp");
  } else {
    out.println("Invalid password <a href='index.jsp'>try again</a>");
  }
%>