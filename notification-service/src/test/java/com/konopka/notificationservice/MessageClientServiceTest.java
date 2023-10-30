package com.konopka.notificationservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.konopka.notificationservice.Status.NOT_SENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private MessageClientService client;

    @BeforeEach
    void setUp() {
        client = new MessageClientService(restTemplate, "message-service");
    }

    @Test
    void getNotSentMessages() {
        List<MessageDTO> given = List.of(MessageDTO.builder().status(NOT_SENT).build(),
                MessageDTO.builder().status(NOT_SENT).build());

        ResponseEntity<List<MessageDTO>> responseEntity = new ResponseEntity<>(given, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("message-service/messages?status=" + NOT_SENT),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseEntity);

        client.getNotSentMessages();

        verify(restTemplate).exchange(eq("message-service/messages?status=NOT_SENT"), eq(HttpMethod.GET),
                eq(null), any(ParameterizedTypeReference.class));
    }

    @Test
    void updateMessage() {
        String emailUuid = UUID.randomUUID().toString();
        MessageDTO given = MessageDTO.builder().status(NOT_SENT).emailUuid(emailUuid).build();

        client.updateMessage(given);

        verify(restTemplate).exchange(
                eq("message-service/message"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Void.class),
                eq(given.getEmailUuid())
        );
        assertEquals(Status.SENT, given.getStatus());
    }
}