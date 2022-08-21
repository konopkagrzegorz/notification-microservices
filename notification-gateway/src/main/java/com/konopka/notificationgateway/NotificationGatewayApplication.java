package com.konopka.notificationgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NotificationGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationGatewayApplication.class, args);
	}

}
