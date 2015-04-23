<%--
  Created by IntelliJ IDEA.
  User: asvsfs
  Date: 4/23/2015
  Time: 10:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@ page import="org.jose4j.json.internal.json_simple.JSONObject" %>

<%
  try{
    String s[]=null;

    Class.forName("com.mysql.jdbc.Driver").newInstance();

    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
            "test", "test123");Statement st=con.createStatement();
    ResultSet rs = st.executeQuery("select username from users");

    List li = new ArrayList();

    while(rs.next())
    {
      li.add(rs.getString(1));
    }

    String[] str = new String[li.size()];
    Iterator it = li.iterator();

    int i = 0;
    while(it.hasNext())
    {
      String p = (String)it.next();
      str[i] = p;
      i++;
    }

    //jQuery related start
    String query = (String)request.getParameter("term");

    int cnt=1;
    for(int j=0;j<str.length;j++)
    {
      if(str[j].toUpperCase().startsWith(query.toUpperCase()))
      {
        JSONObject json = new JSONObject();

        json.put("label", str[j]);
        String jString = JSONObject.toJSONString(json);
        out.println(jString);
        //out.print(str[j]+"\n");
        if(cnt>=5)// 5=How many results have to show while we are typing(auto suggestions)
          break;
        cnt++;
      }
    }
    //jQuery related end

    rs.close();
    st.close();
    con.close();

  }
  catch(Exception e){
    e.printStackTrace();
  }

%>