# NOTIFICATION MICROSERVICES

This project was developed to introduce myself into microservices architecture by practicing 
it with usage of modern organizing tools like:
* docker
* eureka server for service discovery
* spring api gateway
* spring boot actuator


General idea behind this project is that it is hard for me to remember all the things which
are important from my `gmail account` for instance `payments` notifications.

The goal is to send `SMS` when a filtering event will occur - for instance:
* deadline for payment for electricity is *22-08-2022* so I would like to get an `SMS` notification 
with appropriate `message body` two days before

## MICROSERVICES ARCHITECTURE
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

### NOTIFICATION-EUREKA-SERVER
Microservice responsible for client discovery and matching the request to proper microservice no matter how many instances
you will have.
### NOTIFICATION-GATEWAY
Microservice responsible for having a possibility to use single context root path to communicate with all microservices
for instance:
* `email-filtering-service` is running on port `8082` and has an endpoint `/filtering/api`
* `email-rest-client` is running on port `8081` and has an endpoint `/email/api`
* `message-service` is running on port `8083` and has an endpoint `/msg/api`

In a result all those microservices will be available under `http://localhost:8080` 

//TODO CREATE ANOTHER MICROSERVICE WHICH WILL BE RESPONSIBLE FOR SENDING AN EMAIL
