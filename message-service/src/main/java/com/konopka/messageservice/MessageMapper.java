package com.konopka.messageservice;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class MessageMapper {

    public MessageDTO messageToMessageDTO(Message message) {
        return new MessageDTO.MessageDTOBuilder()
                .body(message.getBody())
                .emailUuid(message.getEmailUuid())
                .sendDate(message.getSendDate())
                .status(message.getStatus())
                .build();
    }

    public Message messageDtoToMessage(MessageDTO messageDTO) {
        return new Message.MessageBuilder()
                .body(messageDTO.getBody())
                .emailUuid(messageDTO.getEmailUuid())
                .status(messageDTO.getStatus())
                .sendDate(messageDTO.getSendDate())
                .build();
    }

    public LocalDate convertStringToDate(String date) {
        DateTimeFormatter sf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date,sf);
    }
}
