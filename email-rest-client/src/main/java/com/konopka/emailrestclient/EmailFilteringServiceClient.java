package com.konopka.emailrestclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailFilteringServiceClient {

    private static final String FILTER = "/filter";

    @Value("${email.filtering.service}")
    private String emailFilteringServiceApiHost;

    RestTemplate restTemplate;

    @Autowired
    public EmailFilteringServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Boolean isMailInFilteringService(EmailDTO emailDTO) {
        String val = emailFilteringServiceApiHost + FILTER;
        ResponseEntity<Boolean> response = restTemplate
                .exchange(val, HttpMethod.PUT,
                        new HttpEntity<>(emailDTO), Boolean.class);
        return response.getBody();
    }
}
