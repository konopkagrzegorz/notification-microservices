FROM openjdk:17-alpine

COPY ./target/email-rest-client.jar /opt/email-rest-client/email-rest-client.jar

WORKDIR /opt/email-rest-client

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "email-rest-client.jar"]

