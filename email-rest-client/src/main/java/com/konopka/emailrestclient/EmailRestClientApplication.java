package com.konopka.emailrestclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class EmailRestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailRestClientApplication.class, args);
	}

}
