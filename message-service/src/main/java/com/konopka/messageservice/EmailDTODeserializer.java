package com.konopka.messageservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class EmailDTODeserializer implements Deserializer<EmailDTO> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public EmailDTO deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.info("Null received at deserializing an email from: {}", topic);
                return null;
            }
            log.debug("Serializing an email form: {}", topic);
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), EmailDTO.class);
        } catch (Exception e) {
            log.error("Error when deserializing byte[] to EmailDTO");
            throw new SerializationException("Error when deserializing byte[] to EmailDTO");
        }
    }

    @Override
    public void close() {
    }
}
