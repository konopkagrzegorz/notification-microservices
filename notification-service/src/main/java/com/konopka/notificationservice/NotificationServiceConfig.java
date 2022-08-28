package com.konopka.notificationservice;

import lombok.SneakyThrows;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

@Configuration
public class NotificationServiceConfig {

    public final static String ACCOUNT_SID_KEY = "ACCOUNT_SID";
    public final static String AUTH_TOKEN_KEY = "AUTH_TOKEN";
    public final static String PHONE_NUMBER_KEY = "PHONE_NUMBER";
    public final static String PHONE_NUMBER_TO_KEY = "PHONE_NUMBER_TO";

    @Bean
    @SneakyThrows
    public Properties properties() {
        final InputStreamReader in = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("twilio.properties")));

        Properties properties = new Properties();
        properties.load(in);
        return properties;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(10000))
                .setReadTimeout(Duration.ofMillis(10000))
                .build();
    }
}
