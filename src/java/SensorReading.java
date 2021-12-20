import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SensorReading {
    int temperature;
    int humidity;
    int windSpeed;
    int uuid;
    String date;
    private Connection conn;
    private String databaseName;

    public SensorReading(int temp, int hum, int wind, String date, int uuid) throws SQLException {
        this.temperature = temp;
        this.humidity = hum;
        this.windSpeed = wind;
        this.date = date;
        this.uuid = uuid;
        this.databaseName = "weatherDB.db";
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);

    }

    public void createSensorReading() throws SQLException {

        String readingQuery = "insert into SensorReading values ('" + temperature + "', '" + humidity + "', '" + windSpeed +"', '" + date +"', '" + uuid +"')";
        System.out.println(readingQuery);
        Statement statement = conn.createStatement();
        statement.executeUpdate(readingQuery);
    }


}
