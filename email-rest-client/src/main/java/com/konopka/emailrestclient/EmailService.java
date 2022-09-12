package com.konopka.emailrestclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final MailServiceReceiver mailServiceReceiver;
    private final EmailMapper emailMapper;
    private final EmailFilteringServiceClient emailFilteringServiceClient;
    private final MessageService messageService;

    @Autowired
    public EmailService(EmailRepository emailRepository,
                        MailServiceReceiver mailServiceReceiver,
                        EmailMapper emailMapper,
                        EmailFilteringServiceClient emailFilteringServiceClient,
                        MessageService messageService) {
        this.emailRepository = emailRepository;
        this.mailServiceReceiver = mailServiceReceiver;
        this.emailMapper = emailMapper;
        this.emailFilteringServiceClient = emailFilteringServiceClient;
        this.messageService = messageService;
    }

    public List<EmailDTO> getNewMessages() {
        try {
            List<EmailDTO> emails = mailServiceReceiver.handleReceiveEmail();
            for (EmailDTO emailDTO : emails) {
                emailRepository.findEmailByMessageId(emailDTO.getMessageId())
                        .ifPresentOrElse((dto -> log.debug("{} already exists in repository",
                                emailDTO.getMessageId())), () -> {
                            if (Boolean.TRUE.equals(emailFilteringServiceClient.isMailInFilteringService(emailDTO))) {
                                Email email = emailMapper.emailDtoToEmail(emailDTO);
                                emailRepository.save(email);
                                messageService.saveMessage(emailDTO);
                            }
                });
            }
        } catch (IOException e) {
            log.error("Cannot fetch emails from GMAIL API");
        }
        List<Email> saved = emailRepository.findAll();
        return saved.stream().map(emailMapper::emailToEmailDto).collect(Collectors.toList());
    }
}
