# Power TAC Weather Server

## Introduction

The Power TAC Weather Server is a dynamic jsf web application for serving weather data from a database.

## Getting Started 

* Create a database on a properly configured MySQL server.

* Import the sql 

http://knmi.nl/klimatologie/uurgegevens/datafiles/344/uurgeg_344_2001-2010.zip
http://knmi.nl/klimatologie/uurgegevens/datafiles/344/uurgeg_344_2011-2020.zip

* Download a copy of apache tomcat 7 (http://tomcat.apache.org).

* Run the ./startup.sh script for apache tomcat

* Copy the example config files, and edit the properties if needed.
  Usually only db user/pass and the file locations are needed
  $cp src/main/resources/tournament.properties.template src/main/resources/tournament.properties
  $cp src/main/resources/hibernate.cfg.xml.template     src/main/resources/hibernate.cfg.xml
  $cp src/main/resources/log4j.cfg.xml.template         src/main/resources/log4j.cfg.xml

* Navigate to the tournament scheduler project and run `mvn compile tomcat7:deploy`

