package com.konopka.messageservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@TestPropertySource("classpath:application.yml")
@Sql("classpath:data-test.sql")
class MessageRepositoryIT {

    @Autowired
    MessageRepository messageRepository;

    @ParameterizedTest
    @MethodSource("dataForMessageByStatus")
    void findByStatus_shouldReturnNotEmptyList(Status given, List<Message> expected) {
        List<Message> actual = messageRepository.findByStatus(given);
        Assertions.assertThat(actual).hasSameElementsAs(expected);

    }

    @ParameterizedTest
    @MethodSource("dataForMessageByEmailUuid")
    void findByEmailUuid_shouldReturnMessage(String given, Optional<Message> expected) {
        Optional<Message> actual = messageRepository.findByEmailUuid(given);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> dataForMessageByStatus() {
        return Stream.of(
                arguments(Status.NOT_SENT, List.of(
                        new Message.MessageBuilder()
                                .id(10L)
                                .body("Message")
                                .status(Status.NOT_SENT)
                                .sendDate(LocalDate.of(2021, 10, 16))
                                .emailUuid("e976f0d4-22d1-48be-a724-ef6c3879f428")
                                .build(),
                        new Message.MessageBuilder()
                                .id(20L)
                                .body("Message")
                                .status(Status.NOT_SENT)
                                .sendDate(LocalDate.of(2021, 10, 16))
                                .emailUuid("e976f0d4-22d1-48be-a724-ef6c3879f429")
                                .build())
                ),
                arguments(Status.SENT, List.of(
                        new Message.MessageBuilder()
                                .id(30L)
                                .body("Message")
                                .status(Status.SENT)
                                .sendDate(LocalDate.of(2021, 10, 16))
                                .emailUuid("e976f0d4-22d1-48be-a724-ef6c3879f430")
                                .build(),
                        new Message.MessageBuilder()
                                .id(40L)
                                .body("Message")
                                .status(Status.SENT)
                                .sendDate(LocalDate.of(2021, 10, 16))
                                .emailUuid("e976f0d4-22d1-48be-a724-ef6c3879f431")
                                .build())
                ));
    }

    private static Stream<Arguments> dataForMessageByEmailUuid() {
        return Stream.of(
                arguments("e976f0d4-22d1-48be-a724-ef6c3879f428",
                        Optional.of(new Message.MessageBuilder()
                        .id(10L)
                        .body("Message")
                        .status(Status.NOT_SENT)
                        .sendDate(LocalDate.of(2021, 10, 16))
                        .emailUuid("e976f0d4-22d1-48be-a724-ef6c3879f428")
                        .build())),
                arguments("e976f0d4-22d1-48be-a724-ef6c3879f429",
                        Optional.of(new Message.MessageBuilder()
                        .id(20L)
                        .body("Message")
                        .status(Status.NOT_SENT)
                        .sendDate(LocalDate.of(2021, 10, 16))
                        .emailUuid("e976f0d4-22d1-48be-a724-ef6c3879f429")
                        .build())),
                arguments("e976f0d4-22d1-48be-a724-ef6c3879f430",
                        Optional.of(new Message.MessageBuilder()
                        .id(30L)
                        .body("Message")
                        .status(Status.SENT)
                        .sendDate(LocalDate.of(2021, 10, 16))
                        .emailUuid("e976f0d4-22d1-48be-a724-ef6c3879f430")
                        .build())),
                arguments("e976f0d4-22d1-48be-a724-ef6c3879f431", Optional.of(new Message.MessageBuilder()
                        .id(40L)
                        .body("Message")
                        .status(Status.SENT)
                        .sendDate(LocalDate.of(2021, 10, 16))
                        .emailUuid("e976f0d4-22d1-48be-a724-ef6c3879f431")
                        .build())),
                arguments("SHOULD-NOT-BE-FOUND,", Optional.empty())
        );
    }
}