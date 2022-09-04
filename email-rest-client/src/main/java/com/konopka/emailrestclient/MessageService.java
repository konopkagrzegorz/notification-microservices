package com.konopka.emailrestclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
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
        log.debug("Calling {} to create and save a message {}", emailFilteringServiceApiHost, emailDTO.getMessageId());
        ResponseEntity<MessageDTO> response = restTemplate
                .exchange(emailFilteringServiceApiHost + MESSAGE, HttpMethod.PUT,
                        new HttpEntity<>(emailDTO), MessageDTO.class);
        return response;
    }
}
