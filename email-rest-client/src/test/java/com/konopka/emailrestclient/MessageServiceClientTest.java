package com.konopka.emailrestclient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MessageServiceClient client;


    @Test
    void saveMessage_shouldCallMessageServiceClient() {
        ResponseEntity<MessageDTO> mockResponse = ResponseEntity.ok(new MessageDTO());

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT),
                any(HttpEntity.class), eq(MessageDTO.class)))
                .thenReturn(mockResponse);
        client.saveMessage(new EmailDTO());

        verify(restTemplate).exchange(anyString(), eq(HttpMethod.PUT),
                any(HttpEntity.class), eq(MessageDTO.class));
    }
}