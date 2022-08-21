FROM openjdk:17-alpine

COPY ./target/email-filtering-service.jar /opt/email-filtering-service/email-filtering-service.jar

WORKDIR /opt/email-filtering-service

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "email-filtering-service.jar"]

