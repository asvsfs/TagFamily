/**
 * Created by asvsfs on 4/21/2015.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!

public class LoadDBDriver {
    public static boolean bInit = false;
    public static void main(String[] args) {
        try {
            if(bInit)
                return;

            bInit = true;
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }
    }
}
