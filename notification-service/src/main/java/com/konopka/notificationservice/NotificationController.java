package com.konopka.notificationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notification/api")
public class NotificationController {

    private final MessageClientService messageClientService;
    private final SmsServiceClient smsServiceClient;

    @Autowired
    public NotificationController(MessageClientService messageClientService, SmsServiceClient smsServiceClient) {
        this.messageClientService = messageClientService;
        this.smsServiceClient = smsServiceClient;
    }

    @GetMapping("/notify")
    @Scheduled(cron = "0 30 17 * * *")
    public ResponseEntity<Void> notification() {
        List<MessageDTO> messages = messageClientService.getNotSentMessages();
        LocalDate now = LocalDate.now();
        List<MessageDTO> filtered = messages.stream().
                filter(messageDTO -> ChronoUnit.DAYS.between(messageDTO.getSendDate(), now) >= 2L).toList();
        filtered.forEach(smsServiceClient::sendSMS);
        filtered.forEach(messageClientService::updateMessage);
        return ResponseEntity.ok().build();
    }
}
