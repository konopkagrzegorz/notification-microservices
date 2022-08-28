FROM openjdk:17-alpine

COPY ./target/notification-service.jar /opt/notification-service/notification-service.jar

WORKDIR /opt/notification-service

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "notification-service.jar"]

