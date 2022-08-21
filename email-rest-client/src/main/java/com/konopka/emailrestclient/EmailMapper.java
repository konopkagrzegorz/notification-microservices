package com.konopka.emailrestclient;

import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public Email emailDtoToEmail(EmailDTO emailDTO) {
        return new Email.EmailBuilder()
                .from(emailDTO.getFrom())
                .subject(emailDTO.getSubject())
                .body(emailDTO.getBody())
                .date(emailDTO.getDate())
                .messageId(emailDTO.getMessageId())
                .build();
    }

    public EmailDTO emailToEmailDto(Email email) {
        return new EmailDTO.EmailDTOBuilder()
                .from(email.getFrom())
                .subject(email.getSubject())
                .body(email.getBody())
                .date(email.getDate())
                .messageId(email.getMessageId())
                .build();
    }
}
