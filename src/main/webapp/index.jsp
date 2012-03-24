<?xml version='1.0' encoding='UTF-8' ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Powertac 2012 WeatherServer</title>
</head>


<body>
	<h2>Weather Server REST API Page</h2>
	<f:view>
		<p>For the weather service module in PowerTac Server this provides
			a RESTful interface to request weather. This page contains the
			specification for the api.</p>
		<br />
		<b>REST Specification</b>
		<p>
			To make REST calls on the Weather Server, you will use the
			index.xhtml page, as the example below: 
		
		<br />
		<blockquote>
			<i>http://url.to.weather.server:8080/WeatherServer/faces/index.xhtml?weatherId=1</i>
		</blockquote>

		<br /> Required Parameters: 
		<br />
		<ul>
			<li><b>weatherId=<i>{specific-weather-record-id}</i></b> or <b>weatherDate=<i>{date-of-specific-weather-record-in-this-format[ddmmyyyy]}</i></b>
				- This allows you to specify the date or record id of weather data
				that you want. The weatherId parameter is the primary key in the
				weather record database; whereas, the weatherDate parameter is the
				date in the following format [ddmmyyyy]</li>

			<li><b>weatherLocation=<i>{location-string-for-weather-of-a-region}</i></b>
				- This allows you to specify the location you wish to receive
				weather. This parameter is the name of the location as a string.</li>
		</ul>				
			Optional Parameters:
		<br />
		<ul>
			<li><b>type=<i>{report|forecast|energy}</i></b> - Specify the
				data you wish the Weather Server to respond with for this REST call.
				The data is packaged in xml. The "report" type will give only
				weather reports, the "forecast" type will only give weather
				forecasts, and the "energy" type will give only energy bid reports.
				If this parameter is not specified, all three types are given.</li>
		</ul>
		</p>
	</f:view>
</body>
</html>