package com.konopka.messageservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Profile("docker")
public class EmailDTOConsumer {

    private static final String MESSAGE = "/message";

    @Value("${httpClients.message-service}")
    private String messageServiceApiHost;

    RestTemplate restTemplate;

    public EmailDTOConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @KafkaListener(topics = "message-service-topic",groupId = "group_id")
    public void consumeMessage(EmailDTO message) {
        log.debug("Received a message with uuid: {}", message.getMessageId());
        restTemplate.exchange(messageServiceApiHost + MESSAGE,
                HttpMethod.PUT, new HttpEntity<>(message), MessageDTO.class);
    }
}
