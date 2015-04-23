import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by asvsfs on 4/21/2015.
 */
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.json.internal.json_simple.JSONObject;

@WebServlet(name = "FileUploadServlet")
//this to be used with Java Servlet 3.0 API
@MultipartConfig(fileSizeThreshold=1024*1024*10,    // 10 MB
        maxFileSize=1024*1024*2,          // 2 MB
        maxRequestSize=1024*1024*100)      // 100 MB
public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // this will store uploaded files
    private static List<FileMeta> files = new LinkedList<FileMeta>();
    /***************************************************
     * URL: /upload
     * doPost(): upload the files and other parameters
     ****************************************************/
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
            ex.printStackTrace();
        }

        // 1. Upload File Using Java Servlet API
        //files.addAll(MultipartRequestHandler.uploadByJavaServletAPI(request));

        // 1. Upload File Using Apache FileUpload
        List<FileMeta> resFiles = MultipartRequestHandler.uploadByApacheFileUpload(request);

        if(resFiles == null || resFiles.size()<1){
            response.setContentType("application/json");
            JSONObject jobj = new JSONObject();
            jobj.put("error",true);
            jobj.put("message", "Something is wrong with file");
            response.sendRedirect(jobj.toJSONString());
            return;
        }
        files.addAll(resFiles);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
                    "test", "test123");
            String sql = "insert into images(imagepath,userid) values (?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            int index = 0 ;
            String userid =request.getSession().getAttribute("userid").toString();
            for(FileMeta met: resFiles){
                if(met.isBad == false) {
                    ps.setString(1, met.getFileAddress());
                    ps.setInt(2, Integer.parseInt(userid));
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            ps.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        // Remove some files Do you need this?
        while(files.size() > 20)
        {
            files.remove(0);
        }

        // 2. Set response type to json
        response.setContentType("application/json");

        // 3. Convert List<FileMeta> into JSON format
        ObjectMapper mapper = new ObjectMapper();

        // 4. Send resutl to client
        mapper.writeValue(response.getOutputStream(), resFiles);

    }
    /***************************************************
     * URL: /upload?f=value
     * doGet(): get file of index "f" from List<FileMeta> as an attachment
     ****************************************************/
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

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

        // 1. Get f from URL fetchimage?f="?"
        String value = request.getParameter("f");

        try {
            // 5. Copy file inputstream to response outputstream
            File ff = new File(value);
            InputStream input = new FileInputStream(ff);
            OutputStream output = response.getOutputStream();
            byte[] buffer = new byte[1024*10];

            for (int length = 0; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }

            output.close();
            input.close();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}