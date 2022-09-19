package com.konopka.emailrestclient;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@Sql("classpath:data-test.sql")
class EmailRepositoryIT {

    @Autowired
    EmailRepository emailRepository;

    @ParameterizedTest
    @MethodSource("dataForFindByMessageId")
    void findEmailByMessageId_shouldReturnExpectedOptional(String emailUuid, Optional<Email> expected) {
        Optional<Email> actual = emailRepository.findEmailByMessageId(emailUuid);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> dataForFindByMessageId() {
        return Stream.of(
                arguments("df55a06a-6640-4a3d-9893-8cc08327fec7",
                        Optional.of(new Email.EmailBuilder()
                                .id(10L)
                                .from("Example Example <example@example.com>")
                                .subject("Payment")
                                .body("New payment")
                                .date("21-02-2022")
                                .messageId("df55a06a-6640-4a3d-9893-8cc08327fec7")
                                .build())),
                arguments("df55a06a-6640-4a3d-9893-8cc08327fec8",
                        Optional.of(new Email.EmailBuilder()
                                .id(20L)
                                .from("Example Example <example@example.org>")
                                .subject("Invoice")
                                .body("New invoice")
                                .date("22-02-2022")
                                .messageId("df55a06a-6640-4a3d-9893-8cc08327fec8")
                                .build())),
                arguments("df55a06a-6640-4a3d-9893-8cc08327fec9",
                        Optional.of(new Email.EmailBuilder()
                                .id(30L)
                                .from("Example Example <example@example.biz>")
                                .subject("Cancellation")
                                .body("New cancellation")
                                .date("23-02-2022")
                                .messageId("df55a06a-6640-4a3d-9893-8cc08327fec9")
                                .build())),
                arguments("df55a06a-6640-4a3d-9893-8cc08327fed2", Optional.empty()));
    }
}
