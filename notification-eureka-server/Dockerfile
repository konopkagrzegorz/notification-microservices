FROM openjdk:17-alpine

COPY ./target/notification-eureka-server.jar /opt/notification-eureka-server/notification-eureka-server.jar

WORKDIR /opt/notification-eureka-server

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "notification-eureka-server.jar"]

