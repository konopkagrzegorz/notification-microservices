package com.konopka.messageservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    MessageRepository messageRepository;

    @Mock
    MessageMapper messageMapper;

    @InjectMocks
    MessageService messageService;

    @Test
    void findAllMessagesWithStatus_shouldReturnListOfMessages() {
        Mockito.when(messageMapper.messageToMessageDTO(
                new Message.MessageBuilder()
                        .id(1L)
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-1")
                        .build())).thenReturn(
                new MessageDTO.MessageDTOBuilder()
                        .emailUuid("UUID-1")
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .build());

        Mockito.when(messageMapper.messageToMessageDTO(
                new Message.MessageBuilder()
                        .id(2L)
                        .body("Example 2")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-2")
                        .build())).thenReturn(
                new MessageDTO.MessageDTOBuilder()
                        .emailUuid("UUID-2")
                        .body("Example 2")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .build());

        Mockito.when(messageRepository.findByStatus(Status.SENT)).thenReturn(List.of(
                new Message.MessageBuilder()
                        .id(1L)
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-1")
                        .build(),
                new Message.MessageBuilder()
                        .id(2L)
                        .body("Example 2")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-2")
                        .build()));

        List<MessageDTO> actual = messageService.findAllMessagesWithStatus(Status.SENT);
        List<MessageDTO> expected = List.of(new MessageDTO.MessageDTOBuilder()
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-1")
                        .build(),
                new MessageDTO.MessageDTOBuilder()
                        .body("Example 2")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-2")
                        .build());

        Assertions.assertThat(actual).hasSameElementsAs(expected);
    }

    @ParameterizedTest
    @MethodSource("dataForFindMyMessageUuid")
    void findByEmailUuid_shouldReturnOptionalOfMessage(String search, Message message, MessageDTO messageDTO) {
        Mockito.when(messageMapper.messageToMessageDTO(message)).thenReturn(messageDTO);
        Mockito.when(messageRepository.findByEmailUuid(search)).thenReturn(Optional.of(message));

        Optional<MessageDTO> actual = messageService.findByEmailUuid(search);
        Optional<MessageDTO> expected = Optional.of(messageDTO);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void save() {
        Mockito.when(messageMapper.messageDtoToMessage(
                new MessageDTO.MessageDTOBuilder()
                        .emailUuid("UUID-1")
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .build())).thenReturn(
                new Message.MessageBuilder()
                        .id(1L)
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-1")
                        .build());
        Mockito.when(messageRepository.save(any())).thenReturn(
                new Message.MessageBuilder()
                        .id(1L)
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-1")
                        .build());

        Optional<MessageDTO> actual = messageService.save(new MessageDTO.MessageDTOBuilder()
                .emailUuid("UUID-1")
                .body("Example 1")
                .sendDate(LocalDate.now())
                .status(Status.SENT)
                .build());

        Optional<MessageDTO> expected = Optional.of(new MessageDTO.MessageDTOBuilder()
                .emailUuid("UUID-1")
                .body("Example 1")
                .sendDate(LocalDate.now())
                .status(Status.SENT)
                .build());

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update() {
        //TODO
    }

    @Test
    void findAll_shouldReturnAllMessages() {
        Mockito.when(messageMapper.messageToMessageDTO(
                new Message.MessageBuilder()
                        .id(1L)
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-1")
                        .build())).thenReturn(
                new MessageDTO.MessageDTOBuilder()
                        .emailUuid("UUID-1")
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .build());

        Mockito.when(messageMapper.messageToMessageDTO(
                new Message.MessageBuilder()
                        .id(2L)
                        .body("Example 2")
                        .sendDate(LocalDate.now())
                        .status(Status.NOT_SENT)
                        .emailUuid("UUID-2")
                        .build())).thenReturn(
                new MessageDTO.MessageDTOBuilder()
                        .emailUuid("UUID-2")
                        .body("Example 2")
                        .sendDate(LocalDate.now())
                        .status(Status.NOT_SENT)
                        .build());

        Mockito.when(messageRepository.findAll()).thenReturn(List.of(
                new Message.MessageBuilder()
                        .id(1L)
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-1")
                        .build(),
                new Message.MessageBuilder()
                        .id(2L)
                        .body("Example 2")
                        .sendDate(LocalDate.now())
                        .status(Status.NOT_SENT)
                        .emailUuid("UUID-2")
                        .build()));

        List<MessageDTO> actual = messageService.findAll();
        List<MessageDTO> expected = List.of(new MessageDTO.MessageDTOBuilder()
                        .body("Example 1")
                        .sendDate(LocalDate.now())
                        .status(Status.SENT)
                        .emailUuid("UUID-1")
                        .build(),
                new MessageDTO.MessageDTOBuilder()
                        .body("Example 2")
                        .sendDate(LocalDate.now())
                        .status(Status.NOT_SENT)
                        .emailUuid("UUID-2")
                        .build());

        Assertions.assertThat(actual).hasSameElementsAs(expected);
    }

    private static Stream<Arguments> dataForFindMyMessageUuid() {
        return Stream.of(
                arguments("UUID-1",
                        new Message.MessageBuilder()
                                .id(1L)
                                .body("Example 1")
                                .sendDate(LocalDate.now())
                                .status(Status.SENT)
                                .emailUuid("UUID-1")
                                .build(),
                        new MessageDTO.MessageDTOBuilder()
                                .body("Example 1")
                                .sendDate(LocalDate.now())
                                .status(Status.SENT)
                                .emailUuid("UUID-1")
                                .build()),
                arguments("UUID-2",
                        new Message.MessageBuilder()
                                .id(2L)
                                .body("Example 2")
                                .sendDate(LocalDate.now())
                                .status(Status.NOT_SENT)
                                .emailUuid("UUID-2")
                                .build(),
                        new MessageDTO.MessageDTOBuilder()
                                .body("Example 2")
                                .sendDate(LocalDate.now())
                                .status(Status.NOT_SENT)
                                .emailUuid("UUID-2")
                                .build()));
    }
}
