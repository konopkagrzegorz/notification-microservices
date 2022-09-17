package com.konopka.messageservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class MessageMapperTest {

    private MessageMapper messageMapper = new MessageMapper();


    @ParameterizedTest
    @MethodSource("messageToMessageDTOdata")
    void messageToMessageDTO(Message given, MessageDTO expected) {
        MessageDTO actual = messageMapper.messageToMessageDTO(given);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("messageDTOtoMessageData")
    void messageDtoToMessage(MessageDTO given, Message expected) {
        Message actual = messageMapper.messageDtoToMessage(given);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("dataStringToDate")
    void convertStringToDate(String given, LocalDate expected) {
        LocalDate actual = messageMapper.convertStringToDate(given);
        Assertions.assertThat(actual).isEqualTo(expected);
    }


    private static Stream<Arguments> dataStringToDate() {
        return Stream.of(
                arguments("16-10-1991", LocalDate.of(1991, 10, 16)),
                arguments("16-10-2000", LocalDate.of(2000, 10, 16)),
                arguments("28-02-2022", LocalDate.of(2022, 2, 28)),
                arguments("17-09-2021", LocalDate.of(2021, 9, 17))
        );
    }

    private static Stream<Arguments> messageToMessageDTOdata() {
        return Stream.of(
                arguments(new Message.MessageBuilder()
                                .id(1L)
                                .body("Example")
                                .status(Status.SENT)
                                .sendDate(LocalDate.now())
                                .emailUuid("c0272a2c-3688-11ed-a261-0242ac120002")
                                .build(),
                        new MessageDTO.MessageDTOBuilder()
                                .body("Example")
                                .status(Status.SENT)
                                .sendDate(LocalDate.now())
                                .emailUuid("c0272a2c-3688-11ed-a261-0242ac120002")
                                .build()),
                arguments(new Message.MessageBuilder()
                                .id(10L)
                                .body("Example")
                                .status(Status.NOT_SENT)
                                .sendDate(LocalDate.now())
                                .emailUuid("c0272a2c-3688-11ed-a261-0242ac120002")
                                .build(),
                        new MessageDTO.MessageDTOBuilder()
                                .body("Example")
                                .status(Status.NOT_SENT)
                                .sendDate(LocalDate.now())
                                .emailUuid("c0272a2c-3688-11ed-a261-0242ac120002")
                                .build())
        );
    }

    private static Stream<Arguments> messageDTOtoMessageData() {
        return Stream.of(
                arguments(new MessageDTO.MessageDTOBuilder()
                                .body("Example")
                                .status(Status.SENT)
                                .sendDate(LocalDate.now())
                                .emailUuid("c0272a2c-3688-11ed-a261-0242ac120002")
                                .build(),
                        new Message.MessageBuilder()
                                .id(null)
                                .body("Example")
                                .status(Status.SENT)
                                .sendDate(LocalDate.now())
                                .emailUuid("c0272a2c-3688-11ed-a261-0242ac120002")
                                .build()),
                arguments(new MessageDTO.MessageDTOBuilder()
                                .body("Example")
                                .status(Status.NOT_SENT)
                                .sendDate(LocalDate.now())
                                .emailUuid("c0272a2c-3688-11ed-a261-0242ac120002")
                                .build(),
                        new Message.MessageBuilder()
                                .id(null)
                                .body("Example")
                                .status(Status.NOT_SENT)
                                .sendDate(LocalDate.now())
                                .emailUuid("c0272a2c-3688-11ed-a261-0242ac120002")
                                .build())
        );
    }
}