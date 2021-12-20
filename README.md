If sample db is not downloaded, to initially create and populate the database, please uncomment line 10 in the main. 
This will create and populate the database. Comment out again after it has run once.

Running it should create the web server at port 8001.

Using the following queries you can create sensors, create sensor readings, query all sensor readings by sensor id, or query sensor readings by date. 
The data is returned as JSON.

To create Sensors: http://localhost:8001/createSensor?sensor_id=105&sensor_country=Poland&sensor_city=Warsaw

To create a Sensor Reading: http://localhost:8001/createReading?Temperature=40&Humidity=68&WindSpeed=13&Date=2022-01-02&uuid=100

To retrieve a list of all Sensor Readings by Sensor id: http://localhost:8001/getSensorData?sensor_id=100

To retrieve a list of all Sensor Readings for a specific Sensor within a Date Range: http://localhost:8001/getSensorDataByDate?Sensor_id=100&FirstDate=2022-01-01&LastDate=2022-01-04


If I had more time on this project, I would love to include testing and input validation. 
Unit testing is very important and it would be ideal to have unit tests with each of the types of requests that can be made. 
Also, input validation in the form of checking that the values added in a query are of the right type would improve the standard of this rest api. 

I would also include the capability to return the average values of humidity or temerature over the time specified.

Please let me know if you have and questions regarding my solution.
