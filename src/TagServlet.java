
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.UUID;

/**
 * Created by asvsfs on 4/23/2015.
 */
@WebServlet(name = "TagServlet")
public class TagServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
            ex.printStackTrace();
        }

        String filePath =
                request.getServletContext().getInitParameter("file-upload");



        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
                    "test", "test123");

            // 1. get received JSON data from request
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String json = "";
            if(br != null){
                json = br.readLine();
            }
            JSONObject jobj = new JSONObject(json);

            String image = jobj.getString("image");
            String name = jobj.getString("name");
            int posx = jobj.getInt("posx");
            int posy = jobj.getInt("posx");
            int poswidth = jobj.getInt("poswidth");
            int posheight = jobj.getInt("posheight");

            image =image.replace("/fetchimage?f=","");
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



            String sqltag = "insert into tags(imageid,x,y,width,height) values(?,?,?,?,?)";
            ps = connection.prepareStatement(sqltag, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, id);
            ps.setInt(2,posx);
            ps.setInt(3,posy);;
            ps.setInt(4,poswidth);
            ps.setInt(5,posheight);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            int last_inserted_id =-1;
            if(rs.next())
            {
                last_inserted_id = rs.getInt(1);
            }
            if(last_inserted_id < 0){
                response.setStatus(500);
                response.sendRedirect("Internal error");
            }


            BufferedImage bigImg = ImageIO.read(new File(image));
            // The above line throws an checked IOException which must be caught.
            final int width = poswidth;
            final int height = posheight;
            final int rows = 5;
            final int cols = 5;
            BufferedImage tagimage = bigImg.getSubimage(
                    posx,
                    posy,
                    width,
                    height
            );

            File newfile = new File(filePath+ UUID.randomUUID()+".jpg");
            ImageIO.write(tagimage, "jpg",newfile);

            String sql = "insert into tagimage(tagid,tagimagepath) values(?,?)";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, last_inserted_id);
            ps.setString(2, newfile.getPath());
            ps.execute();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
