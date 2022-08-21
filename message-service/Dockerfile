FROM openjdk:17-alpine

COPY ./target/message-service.jar /opt/message-service/message-service.jar

WORKDIR /opt/message-service

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "message-service.jar"]

