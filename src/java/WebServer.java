import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebServer {
    private Connection conn;
    private String databaseName;
    private HttpServer server;
    private String str = new String();

    public WebServer() throws SQLException, IOException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.databaseName = "weatherDB.db";
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
        this.server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        this.str = "";
    }

    public void startServer() {

        server.start();
        createURL();
    }


    public void createURL(){
//        When looking to receive Sensor Readings, use a query in the format of /getSensorData?sensor_id=100
//        This will return all server readings for the Sensor with ID 100.

        HttpContext hc = this.server.createContext("/getSensorData");
        hc.setHandler(httpExchange -> {
            String query = httpExchange.getRequestURI().getQuery();
            System.out.println(query);
            Map <String,String> map = queryToMap(query);
            JSONObject jo = new JSONObject();
            JSONArray jArray = new JSONArray();
            System.out.println(map);

            try {
                Statement st = conn.createStatement();
                ResultSet results = st.executeQuery("select * from SensorReading where uuid = "+map.get("sensor_id"));

                while (results.next()) {
                    //converts the date from string to type LocalDate
//                    String dat = results.getString("Date");
//                    String string = "January 2, 2010";
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);
//                    LocalDate date = LocalDate.parse(dat, formatter);
//                    System.out.println(date);


                    int temp = results.getInt("Temperature");
                    int hum = results.getInt("Humidity");
                    int id = results.getInt("uuid");
                    String date = results.getString("Date");

//                  Creates the JSON Object and puts the entry into the JSON Array
                    JSONObject entry = new JSONObject();
                    entry.put("id", map.get("sensor_id"));
                    entry.put("temperature", temp);
                    entry.put("humidity", hum);
                    entry.put("Date", date);

                    jArray.put(entry);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Creates the output stream and sends the sensor reading data back in JSON
            httpExchange.sendResponseHeaders(200, str.length());
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            OutputStream os = httpExchange.getResponseBody();
            os.write(jArray.toString().getBytes(StandardCharsets.UTF_8));
            os.close();
            os.flush();
        });

        HttpContext hc2 = this.server.createContext("/");
        hc2.setHandler(new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {

                String home = "Type /getSensorData?sensor_id=100 to query all readings from sensor 100";
                httpExchange.sendResponseHeaders(200, home.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(home.getBytes(StandardCharsets.UTF_8));
                os.close();

            }
        });

        //The /createSensor context is used for creating new sensors. Use the query in the format /createSensor?sensor_id=200&sensor_country=Brazil&sensor_city=Brasilia
        //This will create a sensor in the db with the sensor id, country and city.
        HttpContext hc3 = this.server.createContext("/createSensor");
        hc3.setHandler(new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                String query = httpExchange.getRequestURI().getQuery();
                Map <String,String> map = queryToMap(query);
                System.out.println(map);

                try {
                    Sensor s = new Sensor(Integer.parseInt(map.get("sensor_id")), map.get("sensor_country"), map.get("sensor_city"));
                    s.createNewSensor();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("created");
            }
        });


        //This context is for creating a new sensor reading in the db. Use the query /createReading?Temperature=40&Humidity=68&WindSpeed=13&Date=01/01/22&uuid=100
        HttpContext hc4 = this.server.createContext("/createReading");
        hc4.setHandler(new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {

                String readingQuery = httpExchange.getRequestURI().getQuery();
                Map <String,String> map = queryToMap(readingQuery);
                System.out.println(map);
                try {

                    SensorReading sr = new SensorReading(Integer.parseInt(map.get("Temperature")), Integer.parseInt(map.get("Humidity")), Integer.parseInt(map.get("WindSpeed")), map.get("Date"), Integer.parseInt(map.get("uuid")));
                    sr.createSensorReading();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("created a new sensor reading");
            }
        });

        //Use the query
        HttpContext hc5 = this.server.createContext("/getSensorDataByDate");
        hc5.setHandler(new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                String readingQuery = httpExchange.getRequestURI().getQuery();
                Map <String,String> map = queryToMap(readingQuery);
                System.out.println(map);
                JSONArray jArray = new JSONArray();
                String firstDate = map.get("FirstDate");
                String lastDate = map.get("LastDate");

                try {
                    Statement dateSt = conn.createStatement();

                    //Query /getSensorDataByDate?Sensor_id=100&FirstDate=2022-01-01&LastDate=2022-01-04
                    ResultSet resultsDate = dateSt.executeQuery("SELECT * FROM SensorReading where uuid = " + map.get("Sensor_id")+" AND Date >= date('" + firstDate + "') AND Date <=  date('" + lastDate + "')");
                    while (resultsDate.next()) {

                        int temp = resultsDate.getInt("Temperature");
                        int ws = resultsDate.getInt("WindSpeed");
                        int hum = resultsDate.getInt("Humidity");;
                        String date = resultsDate.getString("Date");

//                  Creates the JSON Object and puts the entry into the JSON Array
                        JSONObject entry = new JSONObject();
                        entry.put("id", map.get("Sensor_id"));
                        entry.put("temperature", temp);
                        entry.put("humidity", hum);
                        entry.put("windSpeed", ws);
                        entry.put("Date", date);

                        jArray.put(entry);


                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                httpExchange.sendResponseHeaders(200, str.length());
                httpExchange.getResponseHeaders().set("Content-Type", "application/json");
                OutputStream os = httpExchange.getResponseBody();
                os.write(jArray.toString().getBytes(StandardCharsets.UTF_8));
                os.close();
                os.flush();

            }


        });


    }








    public Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
