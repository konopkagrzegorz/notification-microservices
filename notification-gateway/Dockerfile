FROM openjdk:17-alpine

COPY ./target/notification-gateway.jar /opt/notification-gateway/notification-gateway.jar

WORKDIR /opt/notification-gateway

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "notification-gateway.jar"]

