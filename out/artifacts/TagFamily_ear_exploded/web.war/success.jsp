<%
  if ((session.getAttribute("userid") == null) || (session.getAttribute("userid") == "")) {
%>
You are not logged in<br/>
<a href="index.jsp">Please Login</a>
<%} else {
%>
<script>
  setTimeout(function() {
    document.location = "/uploadImage.jsp";
  }, 1000); // <-- this is the delay in milliseconds
</script>

Welcome <%=session.getAttribute("userid")%>
<a href='logout.jsp'>Log out</a>
<%
  }
%>

