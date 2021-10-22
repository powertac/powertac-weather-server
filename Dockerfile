FROM maven:3-openjdk-11 AS build
WORKDIR /opt/powertac/weatherserver/build
COPY . .
RUN mvn clean package

FROM openjdk:11-jre-slim
WORKDIR /opt/powertac/weatherserver
ENV WEATHER_SERVER_JAR=weatherserver-0.0.1-SNAPSHOT.jar
COPY --from=build /opt/powertac/weatherserver/build/target/${WEATHER_SERVER_JAR} ./${WEATHER_SERVER_JAR}
ENTRYPOINT java -jar /opt/powertac/orchestrator/${WEATHER_SERVER_JAR}