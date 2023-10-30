package com.konopka.messageservice;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MessageController {
    private final MessageService messageService;
    private final MessageParsingService messageParsingService;

    public MessageController(MessageService messageService, MessageParsingService messageParsingService) {
        this.messageService = messageService;
        this.messageParsingService = messageParsingService;
    }

    @Timed(value = "create.message.manually",
    description = "Create message - Total execution time of saving a message",
    percentiles = {0.5, 0.7, 0.9, 0.95})
    @Operation(description = "Create message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(examples = @ExampleObject(name = "", value = ""))),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(examples = @ExampleObject(name = "", value = "No content"))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(examples = @ExampleObject(name = "", value = "Bad request"))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(examples = @ExampleObject(name = "", value = "Server error")))
    })
    @PostMapping("/create")
    public ResponseEntity<MessageDTO> createMessage(@Parameter( schema = @Schema(implementation = MessageDTO.class)) @RequestBody MessageDTO messageDTO) {
        log.info("Calling the {} service to create a message", messageService.getClass().getSimpleName());
        if (messageService.findByEmailUuid(messageDTO.getEmailUuid()).isPresent()) {
            log.debug("Found EmailUuid: {} in service, no saving", messageDTO.getEmailUuid());
            return ResponseEntity.noContent().build();
        }
        messageService.save(messageDTO);
        return new ResponseEntity<>(messageDTO,HttpStatus.CREATED);
    }

    @Timed(value = "create.message.from.emails",
            description = "Create message - Total execution time of saving a message",
            percentiles = {0.5, 0.7, 0.9, 0.95})
    @Operation(description = "Create message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(examples = @ExampleObject(name = "", value = ""))),
            @ApiResponse(responseCode = "202", description = "Accepted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))
                    }),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "No content"))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Bad request"))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Server error")))
    })
    @PostMapping("/message")
    public ResponseEntity<MessageDTO> createMessageFromEmail(@Parameter( schema = @Schema(implementation = EmailDTO.class)) @RequestBody EmailDTO emailDTO) {
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

    @Timed(value = "update.message",
            description = "Create message - Total execution time of saving a message",
            percentiles = {0.5, 0.7, 0.9, 0.95})
    @Operation(description = "Update message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Bad request"))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Not found"))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Server error")))
    })
    @PutMapping("/message")
    public ResponseEntity<Void> updateMessage(@Parameter( schema = @Schema(implementation = MessageDTO.class)) @RequestBody MessageDTO messageDTO) {
        log.info("Calling the {} service to update a message", messageService.getClass().getSimpleName());
        messageService.update(messageDTO);
        return ResponseEntity.ok().build();
    }

    @Timed(value = "get.messages",
            description = "Create message - Total execution time of saving a message",
            percentiles = {0.5, 0.7, 0.9, 0.95})
    @Operation(description = "Get messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageDTO.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Bad request"))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Not found"))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Server error")))
    })
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@Parameter(
            name = "Message status", example = "NOT_SENT") @RequestParam(required = false) Status status) {
        if (Objects.nonNull(status)) {
            log.info("Calling the {} service to get all messages, with status: {}",
                    messageService.getClass().getSimpleName(), status);
            return ResponseEntity.ok(messageService.findAllMessagesWithStatus(status));
        }
        log.info("Calling the {} service to get all messages", messageService.getClass().getSimpleName());
        return ResponseEntity.ok(messageService.findAll());
    }
}
