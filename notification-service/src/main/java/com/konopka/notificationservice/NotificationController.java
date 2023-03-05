package com.konopka.notificationservice;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
public class NotificationController {

    private final MessageClientService messageClientService;
    private final SmsServiceClient smsServiceClient;

    @Autowired
    public NotificationController(MessageClientService messageClientService, SmsServiceClient smsServiceClient) {
        this.messageClientService = messageClientService;
        this.smsServiceClient = smsServiceClient;
    }

    @Timed(value = "notification.notify",
            description = "Total execution time for sending SMS notification for NOT_SENT emails",
            percentiles = {0.5, 0.7, 0.9, 0.95})
    @Operation(description = "Send SMS notification for messages which meets the deadline and are NOT SENT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(examples = @ExampleObject(name = "", value = "Bad request"))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(examples = @ExampleObject(name = "", value = "Bad request")))
    })
    @GetMapping("/notify")
    @Scheduled(cron = "0 30 17 * * *")
    public ResponseEntity<Void> notification() {
        List<MessageDTO> messages = messageClientService.getNotSentMessages();
        if (messages.isEmpty())
            return ResponseEntity.noContent().build();

        LocalDate now = LocalDate.now();
        List<MessageDTO> filtered = messages.stream().
                filter(messageDTO -> ChronoUnit.DAYS.between(messageDTO.getSendDate(), now) >= 2L).toList();
        filtered.forEach(smsServiceClient::sendSMS);
        filtered.forEach(messageClientService::updateMessage);
        return ResponseEntity.ok().build();
    }
}
