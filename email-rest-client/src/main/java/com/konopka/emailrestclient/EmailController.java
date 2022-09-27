package com.konopka.emailrestclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/email/api")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/emails")
    @Scheduled(cron = "0 20 17 * * *")
    public ResponseEntity<List<EmailDTO>> getEmails() throws IOException {
        log.info("Fetching emails from {}", emailService.getClass().getSimpleName());
        return ResponseEntity.ok(emailService.getNewMessages());
    }
}
