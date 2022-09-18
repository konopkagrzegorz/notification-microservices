package com.konopka.emailrestclient;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class EmailMapperTest {

    EmailMapper emailMapper = new EmailMapper();

    @ParameterizedTest
    @MethodSource("dataForEmailDtoToEmail")
    void emailDtoToEmail(EmailDTO given, Email expected) {
        Email actual = emailMapper.emailDtoToEmail(given);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("dataForEmailToEmailDTO")
    void emailToEmailDto(Email given, EmailDTO expected) {
        EmailDTO actual = emailMapper.emailToEmailDto(given);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> dataForEmailToEmailDTO() {
        return Stream.of(
                arguments(new Email.EmailBuilder()
                                .id(1L)
                                .body("body")
                                .from("example@example.com")
                                .date("11-07-2021")
                                .messageId("UUID-1")
                                .subject("Example 1")
                                .build(),
                        new EmailDTO.EmailDTOBuilder()
                                .body("body")
                                .from("example@example.com")
                                .date("11-07-2021")
                                .messageId("UUID-1")
                                .subject("Example 1")
                                .build()),
                arguments(new Email.EmailBuilder()
                                .id(10L)
                                .body("body 2")
                                .from("example@example.org")
                                .date("11-07-2022")
                                .messageId("UUID-2")
                                .subject("Example 2")
                                .build(),
                        new EmailDTO.EmailDTOBuilder()
                                .body("body 2")
                                .from("example@example.org")
                                .date("11-07-2022")
                                .messageId("UUID-2")
                                .subject("Example 2")
                                .build()));
    }

    private static Stream<Arguments> dataForEmailDtoToEmail() {
        return Stream.of(
                arguments(new EmailDTO.EmailDTOBuilder()
                                .body("body")
                                .from("example@example.com")
                                .date("11-07-2021")
                                .messageId("UUID-1")
                                .subject("Example 1")
                                .build(),
                        new Email.EmailBuilder()
                                .id(null)
                                .body("body")
                                .from("example@example.com")
                                .date("11-07-2021")
                                .messageId("UUID-1")
                                .subject("Example 1")
                                .build()),
                arguments(new EmailDTO.EmailDTOBuilder()
                                .body("body 2")
                                .from("example@example.org")
                                .date("11-07-2022")
                                .messageId("UUID-2")
                                .subject("Example 2")
                                .build(),
                        new Email.EmailBuilder()
                                .id(null)
                                .body("body 2")
                                .from("example@example.org")
                                .date("11-07-2022")
                                .messageId("UUID-2")
                                .subject("Example 2")
                                .build()));
    }
}
