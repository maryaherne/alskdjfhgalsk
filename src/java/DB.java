import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    private Connection conn;
    private String databaseName;

    public DB() throws SQLException {
        this.databaseName = "weatherDB.db";
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);

    }

    //This function is called from the main to initially create the Database
    public boolean createDatabase(){

        File file = new File(databaseName);
        if(file.exists()){
            System.out.println("This database exists");
        }

        //Creates the database and table for Sensor and SensorData
        String sql = "create table Sensor(uuid INTEGER PRIMARY KEY, Country TEXT, City TEXT)";
        String sql2 = "create table SensorReading(Temperature INTEGER, Humidity INTEGER, WindSpeed INTEGER, Date TEXT, uuid INTEGER, FOREIGN KEY (uuid) REFERENCES Sensor(uuid) ON DELETE CASCADE \n" +
                "         ON UPDATE NO ACTION)";


        try {
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            st.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean populateDatabase() throws SQLException {

        // inserts 5 new sensors into Sensor table
        String italy = "insert into Sensor values ('100', 'Italy', 'Rome')";
        String japan = "insert into Sensor values ('101', 'Japan', 'Tokyo')";
        String ireland = "insert into Sensor values ('102', 'Ireland', 'Dublin')";
        String france = "insert into Sensor values ('103', 'France', 'Paris')";
        String usa = "insert into Sensor values ('104', 'America', 'Washington DC')";

        Statement statement = conn.createStatement();
        statement.executeUpdate(italy);
        statement.executeUpdate(japan);
        statement.executeUpdate(ireland);
        statement.executeUpdate(france);
        statement.executeUpdate(usa);

        // inserts 6 new sensor readings into SensorReading table
        String s1 = "insert into SensorReading values ('36', '40', '22', '2022-01-01', '100' )";
        String s2 = "insert into SensorReading values ('31', '45', '10', '2022-01-02', '100' )";
        String s3 = "insert into SensorReading values ('29', '61', '15', '2022-01-03', '100' )";
        String s4 = "insert into SensorReading values ('21', '39', '38', '2022-01-01', '101' )";
        String s5 = "insert into SensorReading values ('19', '35', '32', '2022-01-02', '101' )";
        String s6 = "insert into SensorReading values ('20', '33', '41', '2022-01-03', '101' )";

        statement.executeUpdate(s1);
        statement.executeUpdate(s2);
        statement.executeUpdate(s3);
        statement.executeUpdate(s4);
        statement.executeUpdate(s5);
        statement.executeUpdate(s6);

        return true;
    }
}
