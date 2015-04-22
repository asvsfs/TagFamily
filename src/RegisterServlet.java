
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created by asvsfs on 4/21/2015.
 */
@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
            ex.printStackTrace();
        }

        String pass = request.getParameter("uname");
        String userid = request.getParameter("pass");
        String adminPass = request.getParameter("adminpassword");//bijupsingapore123

        if(BCrypt.checkpw(adminPass,"$2a$10$aitLwA6yZTsClT7GGU058O9UdEWaXIH8NzAfkw1xuzNi7Mi7Fhfu.")){
            //ADMIN OK
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
                    "test", "test123");

            pass = BCrypt.hashpw(pass,BCrypt.gensalt());

            String queryString = "INSERT INTO users (username,password)  " +
                    "VALUES (?,?)";

            PreparedStatement ps = con.prepareStatement(queryString);
            ps.setString(1, userid);
            ps.setString(2, pass);

            ps.execute();
            response.setStatus(200);
            response.sendRedirect("Congratulation you made it!");
        }catch(Exception e){
            e.printStackTrace();
            response.setStatus(500);
            response.sendRedirect("Contact Administrator");
        }
        }else{
            response.sendRedirect("Not Authorized");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
