package com.konopka.notificationeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class NotificationEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationEurekaServerApplication.class, args);
	}

}
