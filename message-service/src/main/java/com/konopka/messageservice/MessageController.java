package com.konopka.messageservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/msg/api")
public class MessageController {
    private final MessageService messageService;
    private final MessageParsingService messageParsingService;

    public MessageController(MessageService messageService, MessageParsingService messageParsingService) {
        this.messageService = messageService;
        this.messageParsingService = messageParsingService;
    }

    @PostMapping("/message")
    public ResponseEntity<MessageDTO> getMessage(@RequestBody EmailDTO emailDTO) {
        log.info("Calling the {} service to create a message", messageService.getClass().getSimpleName());
        if (messageService.findByEmailUuid(emailDTO.getMessageId()).isPresent()) {
            log.debug("Found EmailUuid: {} in service, no saving", emailDTO.getMessageId());
            return ResponseEntity.ok().build();
        }
        Optional<MessageDTO> messageDTO = messageParsingService.convertEmailDTOtoMessageDTO(emailDTO);
        if (messageDTO.isEmpty()) {
            log.debug("Message body is empty, exiting");
            return ResponseEntity.noContent().build();
        }
        messageService.save(messageDTO.get());
        return new ResponseEntity<>(messageDTO.get(),HttpStatus.ACCEPTED);
    }

    @PutMapping("/message")
    public ResponseEntity<Void> updateMessage(@RequestBody MessageDTO messageDTO) {
        log.info("Calling the {} service to update a message", messageService.getClass().getSimpleName());
        messageService.update(messageDTO);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@RequestParam(required = false) Status status) {
        if (Objects.nonNull(status)) {
            log.info("Calling the {} service to get all messages, with status: {}",
                    messageService.getClass().getSimpleName(), status);
            return ResponseEntity.ok(messageService.findAllMessagesWithStatus(status));
        }
        log.info("Calling the {} service to get all messages", messageService.getClass().getSimpleName());
        return ResponseEntity.ok(messageService.findAll());
    }
}
