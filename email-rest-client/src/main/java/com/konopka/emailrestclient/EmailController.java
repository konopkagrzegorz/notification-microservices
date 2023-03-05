package com.konopka.emailrestclient;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Timed(value = "fetch.email",
            description = "Total execution time for fetching emails from Gmail API",
            percentiles = {0.5, 0.7, 0.9, 0.95})
    @Operation(description = "Fetch only new emails from Gmail API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmailDTO.class)))
                    }),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(examples = @ExampleObject(name = "", value = "Bad request")))
    })
    @GetMapping("/emails")
    @Scheduled(cron = "0 20 17 * * *")
    public ResponseEntity<List<EmailDTO>> getEmails() throws IOException {
        log.info("Fetching emails from {}", emailService.getClass().getSimpleName());
        return ResponseEntity.ok(emailService.getNewMessages());
    }
}
