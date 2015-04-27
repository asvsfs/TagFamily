import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by asvsfs on 4/26/2015.
 */
@WebServlet(name = "DeleteServlet")
public class DeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. get received JSON data from request
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null){
            json = br.readLine();
        }
        JSONObject jobj = new JSONObject(json);

        String image = jobj.getString("image");image =image.replace("/fetchimage?f=", "");
        String name = jobj.getString("name");

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
                    "test", "test123");

            String getImageSql = "select imageid from images where imagepath = ?";
            PreparedStatement ps = connection.prepareStatement(getImageSql);
            ps.setString(1,image);
            ResultSet rs =ps.executeQuery();
            int id = -1;
            while(rs.next()){
                id = rs.getInt("imageid");
            }
            if(id < 1)
                return;


            String selectSQL ="SELECT * FROM tagimage INNER JOIN tags ON tagimage.tagid=tags.tagid INNER JOIN images ON tags.imageid = images.imageid ";
            String deleteTagimage = "DELETE tagimage FROM tagimage WHERE tagimage.tagid=?";
            String deleteTag = "DELETE tags FROM tags WHERE tags.tagid = ?";
            ps = connection.prepareStatement(selectSQL);

            rs = ps.executeQuery();
            id = -1 ;
            while(rs.next()){
                File file = new File(rs.getString("imagepath"));

                if(file.delete()){
                    System.out.println(file.getName() + " is deleted!");
                }else{
                    System.out.println("Delete operation is failed.");
                }
                id = rs.getInt("tagid");

            }

            ps = connection.prepareStatement(deleteTagimage);
            ps.setInt(1,id);
            ps.execute();
            ps = connection.prepareStatement(deleteTag);
            ps.setInt(1,id);
            ps.execute();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
