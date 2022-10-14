package com.konopka.messageservice;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiResponses({@ApiResponse(message = "OK", code = 200, response = MessageDTO.class),
                  @ApiResponse(message = "Accepted", code = 204, response = MessageDTO.class),
                  @ApiResponse(message = "No Content", code = 204),
                  @ApiResponse(message = "Bad request",code = 400),
                  @ApiResponse(message = "Server error",code = 500)})
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
    @ApiResponses({@ApiResponse(message = "OK", code = 200),
            @ApiResponse(message = "Bad request",code = 400),
            @ApiResponse(message = "Server error",code = 500)})
    public ResponseEntity<Void> updateMessage(@RequestBody MessageDTO messageDTO) {
        log.info("Calling the {} service to update a message", messageService.getClass().getSimpleName());
        messageService.update(messageDTO);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/messages")
    @ApiResponses({@ApiResponse(message = "OK", code = 200, response = MessageDTO.class),
                   @ApiResponse(message = "Bad request",code = 400),
                   @ApiResponse(message = "Server error",code = 500)})
    public ResponseEntity<List<MessageDTO>> getMessages(
            @ApiParam(name = "Status to filter", example = "NOT_SENT")
            @RequestParam(required = false) Status status) {
        if (Objects.nonNull(status)) {
            log.info("Calling the {} service to get all messages, with status: {}",
                    messageService.getClass().getSimpleName(), status);
            return ResponseEntity.ok(messageService.findAllMessagesWithStatus(status));
        }
        log.info("Calling the {} service to get all messages", messageService.getClass().getSimpleName());
        return ResponseEntity.ok(messageService.findAll());
    }
}
