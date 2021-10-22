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