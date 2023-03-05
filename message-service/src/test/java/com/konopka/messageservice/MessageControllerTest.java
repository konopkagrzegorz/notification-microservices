package com.konopka.messageservice;

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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    MessageService messageService;

    @Mock
    MessageParsingService messageParsingService;

    @InjectMocks
    MessageController messageController;

    @Test
    void createMessage_shouldReturnCreated() {
        Mockito.when(messageService.findByEmailUuid(any())).thenReturn(Optional.empty());
        MessageDTO messageDTO = MessageDTO.builder()
                .body("Exampld")
                .emailUuid("121uiud")
                .sendDate(LocalDate.now())
                .status(Status.NOT_SENT).build();

        ResponseEntity<MessageDTO> expected = new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
        ResponseEntity<MessageDTO> actual = messageController.createMessage(messageDTO);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createMessageWithMessage_shouldReturnNoContent() {
        MessageDTO messageDTO = MessageDTO.builder()
                .body("Example")
                .emailUuid(UUID.randomUUID().toString())
                .sendDate(LocalDate.now())
                .status(Status.NOT_SENT).build();
        Mockito.when(messageService.findByEmailUuid(any())).thenReturn(Optional.of(messageDTO));

        ResponseEntity<MessageDTO> expected = ResponseEntity.noContent().build();
        ResponseEntity<MessageDTO> actual = messageController.createMessage(messageDTO);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createMessage_shouldReturnEmptyResponseEntity() {
        Mockito.when(messageService.findByEmailUuid(any())).thenReturn(Optional.of(new MessageDTO()));

        ResponseEntity<MessageDTO> expected = ResponseEntity.ok().build();
        ResponseEntity<MessageDTO> actual = messageController.createMessageFromEmail(new EmailDTO());

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createMessage_shouldReturnNoContent() {
        Mockito.when(messageService.findByEmailUuid(any())).thenReturn(Optional.empty());

        ResponseEntity<MessageDTO> expected = ResponseEntity.noContent().build();
        ResponseEntity<MessageDTO> actual = messageController.createMessageFromEmail(new EmailDTO());

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createMessage_shouldReturnAccepted() {
        MessageDTO messageDTO = new MessageDTO.MessageDTOBuilder()
                .body("body")
                .emailUuid("uuid")
                .sendDate(LocalDate.now())
                .status(Status.NOT_SENT)
                .build();

        Mockito.when(messageService.findByEmailUuid(any())).thenReturn(Optional.empty());
        Mockito.when(messageParsingService.convertEmailDTOtoMessageDTO(any()))
                .thenReturn(Optional.of(messageDTO));
        Mockito.when(messageService.save(messageDTO)).thenReturn(Optional.of(messageDTO));

        ResponseEntity<MessageDTO> expected = ResponseEntity.accepted().body(messageDTO);
        ResponseEntity<MessageDTO> actual = messageController.createMessageFromEmail(new EmailDTO());

        Assertions.assertThat(actual).isEqualTo(expected);
    }


    @Test
    void getMessages_shouldReturnAllMessages() {
        Mockito.when(messageService.findAll()).thenReturn(getAllMessages());
        ResponseEntity<List<MessageDTO>> expected = ResponseEntity.ok(getAllMessages());
        ResponseEntity<List<MessageDTO>> actual = messageController.getMessages(null);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getMessages_shouldReturnNotSentMessages() {
        Mockito.when(messageService.findAllMessagesWithStatus(Status.NOT_SENT)).thenReturn(getNotSentMessages());
        ResponseEntity<List<MessageDTO>> expected = ResponseEntity.ok(getNotSentMessages());
        ResponseEntity<List<MessageDTO>> actual = messageController.getMessages(Status.NOT_SENT);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getMessages_shouldReturnSentMessages() {
        Mockito.when(messageService.findAllMessagesWithStatus(Status.SENT)).thenReturn(getSentMessages());
        ResponseEntity<List<MessageDTO>> expected = ResponseEntity.ok(getSentMessages());
        ResponseEntity<List<MessageDTO>> actual = messageController.getMessages(Status.SENT);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private List<MessageDTO> getAllMessages() {
        return List.of(
                new MessageDTO.MessageDTOBuilder()
                        .body("body 1")
                        .status(Status.NOT_SENT)
                        .sendDate(LocalDate.now())
                        .emailUuid("UUID-1")
                        .build(),
                new MessageDTO.MessageDTOBuilder()
                        .body("body 1")
                        .status(Status.SENT)
                        .sendDate(LocalDate.now())
                        .emailUuid("UUID-2")
                        .build());
    }

    private List<MessageDTO> getNotSentMessages() {
        return List.of(
                new MessageDTO.MessageDTOBuilder()
                        .body("body 1")
                        .status(Status.NOT_SENT)
                        .sendDate(LocalDate.now())
                        .emailUuid("UUID-1")
                        .build(),
                new MessageDTO.MessageDTOBuilder()
                        .body("body 2")
                        .status(Status.NOT_SENT)
                        .sendDate(LocalDate.now())
                        .emailUuid("UUID-2")
                        .build());
    }

    private List<MessageDTO> getSentMessages() {
        return List.of(
                new MessageDTO.MessageDTOBuilder()
                        .body("body 1")
                        .status(Status.SENT)
                        .sendDate(LocalDate.now())
                        .emailUuid("UUID-1")
                        .build());
    }
}
