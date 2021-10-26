# PowerTAC Weather Server

## Requirements

- Java 11
- Maven 3.6
- Access to a MySQL/MariaDB database containing forecasts as well as weather reports compliant with the existing schema.


## Configuration

Create a configuration file(`application.properties`) in the project root with the following parameters. 

```properties
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.url=jdbc:mysql://localhost:3306/database
server.port=8080
```


## Running

To start the Weather Server change to the project root and run the Maven `exec` command with the goal `java`:

```shell
$ mvn exec:java
```

## Usage

### Getting weather data

#### Request

`http://<WEATHERSERVER_URL>/data?weatherDate=<DATE>&weatherLocation=<LOCATION>`

- `<WEATHERSERVER_URL>`: address of your Weather Server instance
- `<LOCATION>`: lowercase name of a location provided by the Weather Server
- `<DATE>` is a date in the format `yyyyMMddHH`.

To get the data for Rotterdam on the first of March 2009 at 00:00, the URL would look something like this:

`http://weatherserver/data?weatherDate=2009030100&weatherLocation=rotterdam`

#### Response

The response is an XML object containing both a list of weather reports and a list of weather forecasts. The response
body for Rotterdam on the first of March 2009 at 00:00 would look like this (with some rows excluded):

```xml
<data>
    <weatherReports>
        <weatherReport location="rotterdam" date="2009-03-01 16:00:00" temp="9.8" winddir="120.0" windspeed="2.0" cloudcover="1.0"/>
        <weatherReport location="rotterdam" date="2009-03-01 17:00:00" temp="9.4" winddir="220.0" windspeed="2.0" cloudcover="1.0"/>
        <weatherReport location="rotterdam" date="2009-03-01 18:00:00" temp="8.4" winddir="250.0" windspeed="3.0" cloudcover="1.0"/>
        ...
        <weatherReport location="rotterdam" date="2009-03-02 13:00:00" temp="8.9" winddir="290.0" windspeed="5.0" cloudcover="0.5"/>
        <weatherReport location="rotterdam" date="2009-03-02 14:00:00" temp="7.9" winddir="260.0" windspeed="5.0" cloudcover="0.125"/>
        <weatherReport location="rotterdam" date="2009-03-02 15:00:00" temp="7.2" winddir="260.0" windspeed="4.0" cloudcover="0.125"/>
    </weatherReports>
    <weatherForecasts>
        <weatherForecast origin="2009-03-01 16:00:00" id="1" date="2009-03-01 17:00:00" location="rotterdam" temp="8.79903" winddir="224.0" windspeed="1.03478" cloudcover="1.0"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="2" date="2009-03-01 18:00:00" location="rotterdam" temp="7.48767" winddir="253.0" windspeed="1.63819" cloudcover="0.956411"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="3" date="2009-03-01 19:00:00" location="rotterdam" temp="7.60748" winddir="253.0" windspeed="3.28671" cloudcover="0.893238"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="4" date="2009-03-01 20:00:00" location="rotterdam" temp="5.12002" winddir="250.0" windspeed="3.37835" cloudcover="0.870785"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="5" date="2009-03-01 21:00:00" location="rotterdam" temp="5.82658" winddir="266.0" windspeed="2.19784" cloudcover="0.861428"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="6" date="2009-03-01 22:00:00" location="rotterdam" temp="5.32494" winddir="289.0" windspeed="4.38346" cloudcover="0.874694"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="7" date="2009-03-01 23:00:00" location="rotterdam" temp="5.68104" winddir="296.0" windspeed="4.11899" cloudcover="0.924079"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="8" date="2009-03-02 00:00:00" location="rotterdam" temp="6.2728" winddir="319.0" windspeed="4.62803" cloudcover="0.979624"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="9" date="2009-03-02 01:00:00" location="rotterdam" temp="5.71032" winddir="316.0" windspeed="3.50898" cloudcover="0.984156"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="10" date="2009-03-02 02:00:00" location="rotterdam" temp="5.22607" winddir="317.0" windspeed="4.55201" cloudcover="1.0"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="11" date="2009-03-02 03:00:00" location="rotterdam" temp="4.81038" winddir="319.0" windspeed="4.45586" cloudcover="0.989622"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="12" date="2009-03-02 04:00:00" location="rotterdam" temp="5.23421" winddir="341.0" windspeed="3.52098" cloudcover="1.0"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="13" date="2009-03-02 05:00:00" location="rotterdam" temp="4.68585" winddir="283.0" windspeed="2.43234" cloudcover="0.876685"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="14" date="2009-03-02 06:00:00" location="rotterdam" temp="3.60984" winddir="295.0" windspeed="2.65487" cloudcover="0.985317"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="15" date="2009-03-02 07:00:00" location="rotterdam" temp="4.92337" winddir="304.0" windspeed="3.46325" cloudcover="0.951814"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="16" date="2009-03-02 08:00:00" location="rotterdam" temp="6.058" winddir="321.0" windspeed="4.47375" cloudcover="0.952412"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="17" date="2009-03-02 09:00:00" location="rotterdam" temp="6.13145" winddir="308.0" windspeed="4.60974" cloudcover="0.963318"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="18" date="2009-03-02 10:00:00" location="rotterdam" temp="6.80905" winddir="310.0" windspeed="4.42165" cloudcover="0.968171"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="19" date="2009-03-02 11:00:00" location="rotterdam" temp="6.64504" winddir="291.0" windspeed="3.28764" cloudcover="0.257411"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="20" date="2009-03-02 12:00:00" location="rotterdam" temp="8.09295" winddir="311.0" windspeed="5.19269" cloudcover="0.902992"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="21" date="2009-03-02 13:00:00" location="rotterdam" temp="7.88947" winddir="312.0" windspeed="5.23844" cloudcover="0.540124"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="22" date="2009-03-02 14:00:00" location="rotterdam" temp="6.68119" winddir="283.0" windspeed="5.52182" cloudcover="0.158046"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="23" date="2009-03-02 15:00:00" location="rotterdam" temp="5.95279" winddir="283.0" windspeed="4.35443" cloudcover="0.138749"/>
        <weatherForecast origin="2009-03-01 16:00:00" id="24" date="2009-03-02 16:00:00" location="rotterdam" temp="5.24224" winddir="283.0" windspeed="3.23396" cloudcover="0.0"/>
        <weatherForecast origin="2009-03-01 17:00:00" id="1" date="2009-03-01 18:00:00" location="rotterdam" temp="7.79903" winddir="254.0" windspeed="2.03478" cloudcover="1.0"/>
        <weatherForecast origin="2009-03-01 17:00:00" id="2" date="2009-03-01 19:00:00" location="rotterdam" temp="7.48767" winddir="253.0" windspeed="2.63819" cloudcover="0.956411"/>
        <weatherForecast origin="2009-03-01 17:00:00" id="3" date="2009-03-01 20:00:00" location="rotterdam" temp="6.30748" winddir="243.0" windspeed="3.28671" cloudcover="0.893238"/>
        ...
        <weatherForecast origin="2009-03-02 15:00:00" id="21" date="2009-03-03 12:00:00" location="rotterdam" temp="6.17128" winddir="220.0" windspeed="6.65931" cloudcover="0.947594"/>
        <weatherForecast origin="2009-03-02 15:00:00" id="22" date="2009-03-03 13:00:00" location="rotterdam" temp="6.83209" winddir="211.0" windspeed="7.57136" cloudcover="0.940452"/>
        <weatherForecast origin="2009-03-02 15:00:00" id="23" date="2009-03-03 14:00:00" location="rotterdam" temp="7.26372" winddir="211.0" windspeed="6.50627" cloudcover="0.930712"/>
        <weatherForecast origin="2009-03-02 15:00:00" id="24" date="2009-03-03 15:00:00" location="rotterdam" temp="7.06854" winddir="210.0" windspeed="6.49681" cloudcover="0.919241"/>
    </weatherForecasts>
</data>
```

### Getting data availability

### Request

`http://weatherserver/locations`

### Response

This endpoint provides a JSON array of locations including time ranges for both reports and forecasts that are available
for these locations.

```json
[
    {
        "name": "rotterdam",
        "minReportTime": "2008-12-31T23:00:00Z",
        "maxReportTime": "2011-12-31T22:00:00Z",
        "minForecastTime": "2008-12-31T23:00:00Z",
        "maxForecastTime": "2011-12-30T22:00:00Z"
    },
    {
        "name": "cheyenne",
        "minReportTime": "2013-03-31T15:00:00Z",
        "maxReportTime": "2015-04-24T02:00:00Z",
        "minForecastTime": "2013-03-31T19:00:00Z",
        "maxForecastTime": "2015-04-23T08:00:00Z"
    }
]
```
