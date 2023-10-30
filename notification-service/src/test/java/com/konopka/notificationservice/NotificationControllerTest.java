package com.konopka.notificationservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    SmsServiceClient smsServiceClient;

    @Mock
    MessageClientService messageClientService;

    @InjectMocks
    NotificationController notificationController;

    @Test
    void notificationCall_shouldReturnOK() {

        Mockito.when(messageClientService.getNotSentMessages()).thenReturn(List.of(
                new MessageDTO.MessageDTOBuilder()
                        .body("Message 1")
                        .sendDate(LocalDate.now())
                        .emailUuid(UUID.randomUUID().toString())
                        .status(Status.NOT_SENT).sendDate(LocalDate.now().minus(3, ChronoUnit.DAYS))
                        .build(),
                new MessageDTO.MessageDTOBuilder()
                        .body("Message 2")
                        .sendDate(LocalDate.now())
                        .emailUuid(UUID.randomUUID().toString())
                        .status(Status.NOT_SENT).sendDate(LocalDate.now())
                        .build()));

        ResponseEntity<Void> expected = ResponseEntity.ok().build();
        Assertions.assertThat(notificationController.notification().getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(notificationController.notification().getBody()).isEqualTo(expected.getBody());
    }
}