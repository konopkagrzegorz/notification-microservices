package com.konopka.emailrestclient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class EmailFilteringServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmailFilteringServiceClient client;

    @Test
    public void shouldReturnResponseFromFilteringServiceClient() {

        EmailDTO emailDTO = EmailDTO.builder().messageId(UUID.randomUUID().toString()).build();

        ResponseEntity<Boolean> mockResponse = ResponseEntity.ok(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT),
                any(HttpEntity.class), eq(Boolean.class)))
                .thenReturn(mockResponse);


        Boolean result = client.isMailInFilteringService(emailDTO);

        assertThat(result).isTrue();
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.PUT),
                any(HttpEntity.class), eq(Boolean.class));
    }
}