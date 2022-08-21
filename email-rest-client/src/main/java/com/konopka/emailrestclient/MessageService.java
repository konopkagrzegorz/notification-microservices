package com.konopka.emailrestclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageService {

    private static final String MESSAGE = "/message";

    @Value("${message.service}")
    private String emailFilteringServiceApiHost;

    RestTemplate restTemplate;

    public MessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<MessageDTO> saveMessage(EmailDTO emailDTO) {
        ResponseEntity<MessageDTO> response = restTemplate
                .exchange(emailFilteringServiceApiHost + MESSAGE, HttpMethod.PUT,
                        new HttpEntity<>(emailDTO), MessageDTO.class);
        return response;
    }
}
