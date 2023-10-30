package com.konopka.emailrestclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class EmailDTOSerializerTest {

    private static final UUID EMAIL_UUID = UUID.randomUUID();

    private EmailDTOSerializer serializer = new EmailDTOSerializer();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deserializeReturnSerializedObject() {
        EmailDTO given = EmailDTO.builder()
                .from("example@example.com")
                .subject("Subject")
                .body("Body")
                .messageId(EMAIL_UUID.toString())
                .build();

        String actual = new String(serializer.serialize("", given), StandardCharsets.UTF_8);

        Assertions.assertThat(actual).contains("example@example.com", "Subject", "Body", EMAIL_UUID.toString());
    }

    @Test
    void deserializeReturnNull() {
        byte[] actual = serializer.serialize("", null);

        Assertions.assertThat(actual).isNull();
    }

    @Test
    void deserializeThrowsSerializationException() throws JsonProcessingException {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        Mockito.when(objectMapper.writeValueAsBytes(any())).thenThrow(JsonProcessingException.class);
        EmailDTOSerializer emailDTOSerializer = new EmailDTOSerializer(objectMapper);

        Assertions.assertThatThrownBy(() -> emailDTOSerializer.serialize(anyString(), new EmailDTO()))
                .isExactlyInstanceOf(SerializationException.class)
                .hasMessage("Error when serializing EmailDTO to byte[]");
    }
}