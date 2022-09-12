package com.konopka.emailrestclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("docker")
public class MessageServiceEventProducer implements MessageService {

    private final String MESSAGE_TOPIC = "message-service-topic";
    private final KafkaTemplate<String, EmailDTO> kafkaTemplate;

    @Autowired
    public MessageServiceEventProducer(KafkaTemplate<String, EmailDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void saveMessage(EmailDTO emailDTO) {
        log.debug("Sending an email event: {}", emailDTO.getMessageId());
        kafkaTemplate.send(MESSAGE_TOPIC, emailDTO);
    }
}
