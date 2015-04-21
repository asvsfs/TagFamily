import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by asvsfs on 4/21/2015.
 */
@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pass = request.getParameter("username");
        String userid = request.getParameter("password");
        String adminPass = request.getParameter("adminpassword");//bijupsingapore123

        if(BCrypt.checkpw(adminPass,"$2a$10$aitLwA6yZTsClT7GGU058O9UdEWaXIH8NzAfkw1xuzNi7Mi7Fhfu.")){
            //ADMIN OK
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
                    "test", "test123");
            Statement st = con.createStatement();
            ResultSet rs;

            pass = BCrypt.hashpw(pass,BCrypt.gensalt());
            String insertTableSQL = "INSERT INTO users"
                    + "( username, password) " + "VALUES"
                    + "("+userid+","+pass+")";

            rs = st.executeQuery(insertTableSQL);

        }catch(Exception e){

        }
        }else{
            response.sendRedirect("Not Authorized");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
