package com.konopka.emailrestclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableEurekaClient
public class EmailRestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailRestClientApplication.class, args);
	}

}
