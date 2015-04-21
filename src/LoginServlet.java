import javax.servlet.RequestDispatcher;
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
import java.util.Date;

/**
 * Created by asvsfs on 4/21/2015.
 */
@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pass = request.getParameter("username");
        String userid = request.getParameter("password");

        String res = BCrypt.hashpw("bijupsingapore123",BCrypt.gensalt());
        //Get user from db check password
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
                    "test", "test123");
            Statement st = con.createStatement();
            ResultSet rs;

            rs = st.executeQuery("select password from users where username='" + userid + "'");

            if (rs.next()) {
                String hashedPassFromDB = rs.getString("password");
                if (BCrypt.checkpw(pass, hashedPassFromDB)) {
                    //successful login
                    String token = JWT.makeJWT(userid, new Date().toString());
                    request.setAttribute("access_token",token);
                    request.setAttribute("userid",userid);
                    RequestDispatcher rd =
                        request.getRequestDispatcher("/successful.jsp");
                }else{
                    //nope
                }
            }

        }catch (Exception e){

        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
