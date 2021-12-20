import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Sensor {
    private int uuid;
    private String country;
    private String city;
    private Connection conn;
    private String databaseName;

    public Sensor(int uuid, String country, String city) throws SQLException {

        this.uuid = uuid;
        this.country = country;
        this.city = city;
        this.databaseName = "weatherDB.db";
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);

    }


    public void createNewSensor() throws SQLException {

        String sensorQuery = "insert into Sensor values ('" + uuid + "', '" + country + "', '" + city +"')";

        System.out.println(sensorQuery);
        Statement statement = conn.createStatement();
        statement.executeUpdate(sensorQuery);

    }

}
