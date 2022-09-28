package com.konopka.emailrestclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class EmailDTOSerializer implements Serializer<EmailDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, EmailDTO data) {
        try {
            if (data == null) {
                log.info("Null received at serializing an email from: {}", topic);
                return new byte[0];
            }
            log.debug("Serializing an email form: {}", topic);
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            log.error("Error during serializing an email: {}", data);
            throw new SerializationException("Error when serializing EmailDTO to byte[]");
        }
    }

    @Override
    public void close() {
    }

}
