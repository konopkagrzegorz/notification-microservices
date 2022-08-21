package com.konopka.messageservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/msg/api")
public class MessageController {
    private final MessageService messageService;
    private final MessageParsingService messageParsingService;

    public MessageController(MessageService messageService, MessageParsingService messageParsingService) {
        this.messageService = messageService;
        this.messageParsingService = messageParsingService;
    }

    @PutMapping("/message")
    public ResponseEntity<MessageDTO> getMessage(@RequestBody EmailDTO emailDTO) {
        if (messageService.findByEmailUuid(emailDTO.getMessageId()).isPresent())
            return ResponseEntity.ok().build();
        Optional<MessageDTO> messageDTO = messageParsingService.convertEmailDTOtoMessageDTO(emailDTO);
        if (messageDTO.isEmpty())
            return ResponseEntity.noContent().build();
        messageService.save(messageDTO.get());
        return new ResponseEntity<>(messageDTO.get(),HttpStatus.ACCEPTED);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getMessages() {
        return ResponseEntity.ok(messageService.findAll());
    }
}
