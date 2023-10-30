package com.konopka.emailrestclient;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GmailMessageToEmailMapperTest {

    GmailMessageToEmailMapper gmailMessageToEmailMapper = new GmailMessageToEmailMapper();

    @Test
    void messageToEmailDTO_shouldConvertMessageToEmailDTO() {

        Message given = new Message()
                .setPayload(new MessagePart()
                        .setHeaders(List.of(new MessagePartHeader()
                                        .setName("From")
                                        .setValue("Example Example <example@example.org>"),
                                new MessagePartHeader()
                                        .setName("Subject")
                                        .setValue("New Invoice"),
                                new MessagePartHeader()
                                        .setName("Date")
                                        .setValue("21-02-2022")))
                        .setParts(List.of(
                                new MessagePart()
                                        .setBody(new MessagePartBody().encodeData("Message body".getBytes())))))
                .setId("UUID-1");

        EmailDTO actual = gmailMessageToEmailMapper.messageToEmailDTO(given);
        EmailDTO expected = new EmailDTO.EmailDTOBuilder()
                .from("example@example.org")
                .subject("New Invoice")
                .messageId("UUID-1")
                .body("Message body")
                .date("21-02-2022")
                .build();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void messageToEmailDTO_shouldConvertMessageToEmailDTOWithoutChangedFromHeader() {
        Message given = new Message()
                .setPayload(new MessagePart()
                        .setHeaders(List.of(new MessagePartHeader()
                                        .setName("From who:")
                                        .setValue("Example Example example@example.org"),
                                new MessagePartHeader()
                                        .setName("Subject of message:")
                                        .setValue("New Invoice"),
                                new MessagePartHeader()
                                        .setName("Sent date:")
                                        .setValue("21-02-2022")))
                        .setParts(List.of(
                                new MessagePart()
                                        .setBody(new MessagePartBody().encodeData("Message body".getBytes())))))
                .setId("UUID-1");

        EmailDTO actual = gmailMessageToEmailMapper.messageToEmailDTO(given);
        EmailDTO expected = new EmailDTO.EmailDTOBuilder()
                .from("null")
                .subject("null")
                .messageId("UUID-1")
                .body("Message body")
                .date("null")
                .build();

        assertThat(actual).isEqualTo(expected);
    }

}
