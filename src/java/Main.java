import java.io.IOException;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {

//        int uuid = 100;
//        Connection conn = DriverManager.getConnection("jdbc:sqlite:weatherDB.db");

        //dbSetup();
        WebServer ws = new WebServer();
        ws.startServer();

    }


    public static boolean dbSetup() throws SQLException {
        DB db = new DB();
        db.createDatabase();
        db.populateDatabase();

        return true;
    }
}
