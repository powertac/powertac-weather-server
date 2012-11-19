# Power TAC Weather Server

## Introduction

The Power TAC Weather Server is a dynamic jsf web application for serving weather data from a database.

## Getting Started 

* Create a database on a properly configured MySQL server.

* Import the data into the database (this needs python > 2.6)

  Get and unzip the weather data files from :
  - http://knmi.nl/klimatologie/uurgegevens/datafiles/344/uurgeg_344_2001-2010.zip
  - http://knmi.nl/klimatologie/uurgegevens/datafiles/344/uurgeg_344_2011-2020.zip

  Run import_knmi_data.py :

  $python import_knmi_data.py

  You can check if all reports are imported for the given location and period.
  Both are declared in the script.

  $python check_weather_data.py

* If you don't have a tomcat server configured, download a copy of apache tomcat 7 (http://tomcat.apache.org).

* Run the ./startup.sh script for apache tomcat

* Copy the example config files, and edit the properties if needed.
  Usually only db user/pass and the file locations are needed
  $cp weatherserver.properties.template         weatherserver.properties

* Run `mvn compile tomcat7:deploy` to deploy to tomcat.
  If needed change the username/password in pom.xml.

