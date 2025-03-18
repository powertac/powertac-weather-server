FROM maven:3-eclipse-temurin-21 AS build
WORKDIR /opt/powertac/weather-server/build
COPY . .
RUN mvn clean package

FROM eclipse-temurin:21-alpine
WORKDIR /opt/powertac/weather-server
ENV WEATHER_SERVER_JAR=weatherserver-0.1.0.jar
COPY --from=build /opt/powertac/weather-server/build/target/${WEATHER_SERVER_JAR} ./${WEATHER_SERVER_JAR}
ENTRYPOINT java -jar /opt/powertac/weather-server/${WEATHER_SERVER_JAR}