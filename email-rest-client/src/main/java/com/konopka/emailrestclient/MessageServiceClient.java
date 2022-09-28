package com.konopka.emailrestclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Profile("dev")
public class MessageServiceClient implements MessageService {

    private static final String MESSAGE = "/message";

    @Value("${httpClients.message-service}")
    private String messageServiceApiHost;

    RestTemplate restTemplate;

    public MessageServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void saveMessage(EmailDTO emailDTO) {
        restTemplate.exchange(messageServiceApiHost + MESSAGE,
                HttpMethod.PUT, new HttpEntity<>(emailDTO), MessageDTO.class);
    }
}
