# NOTIFICATION MICROSERVICES

This project was developed to introduce myself into microservices architecture by practicing 
it with usage of modern tools like:
* docker
* eureka server for service discovery
* spring api gateway
* kafka with zookeeper
* elasticsearch stack (available only when running with docker)
* prometheus (avaiable only when running with docker)
* grafana (available only when running with docker)


General idea behind this project is that it is hard for me to remember all the things which
are important from my `gmail account` for instance `payments` notifications.

The goal is to send `SMS` when a filtering event will occur - for instance:
* deadline for payment for electricity is *22-08-2022* so I would like to get an `SMS` notification 
with appropriate `message body` two days before

## MICROSERVICES ARCHITECTURE

### ARCHITECTURE OVERVIEW

![architecture overview](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/notification-microservices-overview.svg)

### ACTION DIAGRAM

![action diagram](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/action-diagram.svg)

### EMAIL-FILTERING-SERVICE
Main task of this microservice is to filter the emails received from the gmail api via the email-rest-client 
based on the filters set. Example filters can be found in the `sample-data.sql` file where respectively:
* major - filter from whom the email is
* val - filter what must be in the body of the message

If the message meets the requirements specified in the filter, it will be stored in the `email-rest-client` 
database and after than it can be proceeded further by `message-service`.

### EMAIL-REST-CLIENT
Main task of this microservice is to fetch emails from `Gmail API` and if that message meets the requirements 
specified in `email-filtering-service` it will be stored in this microservice database. After that if the `message UUID` 
has not been found in the `message-service` then it will be stored in that microservice by appropriate `REST` call.

### MESSAGE-SERVICE
This microservice is responsible for creating a messages body based on emails received from `email-rest-client` and 
generate them based on `templates` and `key_pattern` filter.

In `sample-data.sql` examples can be found how a proper `template-key_pattern` searching feature should look like.

### NOTIFICATION-SERVICE
This service is responsible for fetching all not sent messages and check if the deadline is greater or equal `2 days`.
If this condition is met then a `REST` call to `twilio API` is sent - and status of message is updateded in `message-servie`.

### NOTIFICATION-EUREKA-SERVER
Microservice responsible for client discovery and matching the request to proper microservice no matter how many instances
you will have.

### NOTIFICATION-GATEWAY
Microservice responsible for having a possibility to use single context root path to communicate with all microservices
for instance:
* `email-filtering-service` is running on port `8082` and has an endpoint `/filtering/api`
* `email-rest-client` is running on port `8081` and has an endpoint `/email/api`
* `message-service` is running on port `8083` and has an endpoint `/msg/api`
* `notification-service` is running on port `8084` and has an endpoint `/notification/api`

In a result all those microservices will be available under `http://localhost:8080`

## HOW TO RUN THE PROJECT?
1. Please refer to [this](https://www.youtube.com/watch?v=-rcRf7yswfM) youtube video from *0:00* to *7:00*
2. Copy your credentials and paste them to `email-rest-client` under *resources*, please refer to `sample-credentials.json`.
3. Create an environment variable in your OS `GMAIL_REFRESH_TOKEN` - it is needed to create a `.jar` files.
4. Register your account on [TWILIO](https://www.twilio.com)
5. Generate your phone number and `ACCOUNT_SID` `AUTH_TOKEN`.
6. Change the name of `sample-twilio.properties` file into `twilio.properties`.
7. Add values to keys in this file.
8. If you would like to run this project locally with **H2 database**:
   * in `email-filtering-service` add filters according to `sample-data.sql` and name created file `data.sql` 
   * in `message-service` add template and pattern similar to `sample-data.sql` and name created file `data.sql`
9. If you would like to run this project with **PostgreSQL database** and **docker**:
   * Create a file `.env` in `email-rest-client` - refer to `.sample-env` - this file wille be needed to create an environment 
   variable during creating a docker container. 
10. Go to the root of the project - `notification-microservices`. 
11. Type `mvn clean package` in your terminal. 
12. After successfully created `.jar` files you can run project in 2 ways:
    * running project locally:
      * go to each project `root catalog` and type `java -jar target/<application-name>`:
      * building order:
        * notification-eureka-server
        * email-filtering-service
        * message-service
        * email-rest-client
        * notification-service
        * notification-gateway
    * running project with docker compose:
      * add entries in databases for `email-filtering-service` and `message-service`, please refer to `sample-data.sql` 
      * go to the `root` of the project and type in your terminal `docker compose up` - it will create images 
and containers for this project. 
13. In `postman-collection` folder you can find a `notification-microservices.json` which you can import to **Postman**
and check what calls you can make.
14. For `elasticsearch stack` please change the ownership of files listed below:
    * `filebeat.yml` in `filebeat` folder to: `root:root` with `-rw-r--r--` access
    * `logstash.conf` in `logstash/pipeline` folder to: `your_username:your_username` with `-rw-rw-r--` access
15. During first time login into `kibana` under `http://localhost:5601` you will need to configure dashboard
    * step - 1
    ![kibana-step-1](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/kibana-step1.png)
    * step - 2
    ![kibana-step-2](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/kibana-step2.png)
    * step - 3
    ![kibana-step-3](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/kibana-step3.png)
    * step - 4
    ![kibana-step-4](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/kibana-step4.png)
16. After configuration, you can filter logs for instance:
![kibana-step-5](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/kibana-step5.png)

17. Grafana provisioning
    * visit `http://localhost:4000`
    * login with credentials `user: admin, password: root`
      ![grafana-login](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/grafana-login.png)
    * go to `Dashoard` and `Import dashboard`
    * import `notifiation-microservices.json` from `./grafana/provisioning/dashboards/` </br>
    After importing you should see dashboard similar to this one:
      ![grafana-import](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/grafana-import.png)
18. Prometheus
    * visit `http://localhost:9090`
    * you can check available services under `http://localhost:9090/targets`
      ![prometheus-targets](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/prometheus-targets.png)
    
## EXAMPLE OF RUNNING APPLICATION

![sample-notification](https://github.com/konopkagrzegorz/notification-microservices/blob/master/images/sample-notification.png)

## LICENSE
Please refer to `LICENSE` file.
