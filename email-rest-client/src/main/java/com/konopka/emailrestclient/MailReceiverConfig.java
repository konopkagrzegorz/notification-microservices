package com.konopka.emailrestclient;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Objects;

import static com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.load;


@Slf4j
@Configuration
@PropertySource("classpath:application-dev.properties")
public class MailReceiverConfig {

    @Value("${spring.gmail.application.name}")
    private String APPLICATION_NAME;

    @Value("${spring.gmail.refresh.token}")
    private String REFRESH_TOKEN;
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Gmail gmail() throws IOException, GeneralSecurityException {
        final InputStreamReader in = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("credentials.json"));
        Gmail service;
        GoogleClientSecrets clientSecrets = load(JSON_FACTORY, in);

        Credential authorize = new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret())
                .build()
                .setRefreshToken(REFRESH_TOKEN);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize)
                .setApplicationName(APPLICATION_NAME).build();
        return service;
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
