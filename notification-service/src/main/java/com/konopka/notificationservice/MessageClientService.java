package com.konopka.notificationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class MessageClientService {

    private static final String NOT_SENT = "NOT_SENT";
    private static final String MESSAGES = "/messages";
    private static final String MESSAGE = "/message";

    @Value("${httpClients.message-service}")
    private String messageService;

    private final RestTemplate restTemplate;

    @Autowired
    public MessageClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MessageDTO> getNotSentMessages() {
        ResponseEntity<List<MessageDTO>> responseEntity =
                restTemplate.exchange(messageService + MESSAGES + "?status=" + NOT_SENT,HttpMethod.GET,null,
                        new ParameterizedTypeReference<>() {
                        });
        return responseEntity.getBody();
    }

    public void updateMessage(MessageDTO messageDTO) {
        messageDTO.setStatus(Status.SENT);
        ResponseEntity<Void> response = restTemplate.exchange(messageService + MESSAGE,
                HttpMethod.PUT, new HttpEntity<>(messageDTO), Void.class, messageDTO.getEmailUuid());
    }
}
