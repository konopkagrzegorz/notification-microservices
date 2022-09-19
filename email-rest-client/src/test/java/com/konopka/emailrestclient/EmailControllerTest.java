package com.konopka.emailrestclient;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

    @Mock
    EmailService emailService;

    @InjectMocks
    EmailController emailController;

    @ParameterizedTest
    @MethodSource("dataForGetEmails")
    void getEmails_shouldReturnListOfEmails(List<EmailDTO> given, ResponseEntity<List<EmailDTO>> expected) throws IOException {
        Mockito.when(emailService.getNewMessages()).thenReturn(given);
        ResponseEntity<List<EmailDTO>> actual = emailController.getEmails();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> dataForGetEmails() {
        return Stream.of(
                arguments(generateEmailDtoList(), ResponseEntity.ok(generateEmailDtoList())),
                arguments(Collections.emptyList(), ResponseEntity.ok(Collections.emptyList())));
    }

    private static List<EmailDTO> generateEmailDtoList() {
        return List.of(
                new EmailDTO.EmailDTOBuilder()
                        .from("Example Example <example@example.com>")
                        .subject("Payment")
                        .body("New payment")
                        .date("21-02-2022")
                        .messageId("df55a06a-6640-4a3d-9893-8cc08327fec7")
                        .build(),
                new EmailDTO.EmailDTOBuilder()
                        .from("Example Example <example@example.org>")
                        .subject("Invoice")
                        .body("New invoice")
                        .date("22-02-2022")
                        .messageId("df55a06a-6640-4a3d-9893-8cc08327fec8")
                        .build(),
                new EmailDTO.EmailDTOBuilder()
                        .from("Example Example <example@example.biz>")
                        .subject("Cancellation")
                        .body("New cancellation")
                        .date("23-02-2022")
                        .messageId("df55a06a-6640-4a3d-9893-8cc08327fec9")
                        .build());
    }
}
