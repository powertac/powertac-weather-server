FROM maven:3-openjdk-11 AS build
WORKDIR /opt/powertac/weather-server/build
COPY . .
RUN mvn clean package

FROM openjdk:11-jre-slim
WORKDIR /opt/powertac/weather-server
ENV WEATHER_SERVER_JAR=weatherserver-0.0.1-SNAPSHOT.jar
COPY --from=build /opt/powertac/weather-server/build/target/${WEATHER_SERVER_JAR} ./${WEATHER_SERVER_JAR}
ENTRYPOINT java -jar /opt/powertac/weather-server/${WEATHER_SERVER_JAR}