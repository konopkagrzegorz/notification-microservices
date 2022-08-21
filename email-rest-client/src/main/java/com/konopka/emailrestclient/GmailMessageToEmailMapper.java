package com.konopka.emailrestclient;

import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GmailMessageToEmailMapper {

    public EmailDTO messageToEmailDTO(Message message) {
        return new EmailDTO.EmailDTOBuilder()
                .from(getHeaderValue(message.getPayload().getHeaders(),"From"))
                .subject(getHeaderValue(message.getPayload().getHeaders(), "Subject"))
                .body(StringUtils
                        .newStringUtf8(Base64.decodeBase64(message.getPayload().getParts().get(0).getBody().getData())))
                .date(getHeaderValue(message.getPayload().getHeaders(), "Date"))
                .messageId(message.getId())
                .build();
    }

    private String getHeaderValue(List<MessagePartHeader> headers, String search) {
        Optional<MessagePartHeader> header = headers
                .stream().filter(messagePartHeader -> messagePartHeader.getName().equals(search)).findFirst();
        if (header.isEmpty())
            return "null";
        if (header.get().getName().equals("From")) {
            int leftIndex = header.get().getValue().indexOf("<") + 1;
            int rightIndex = header.get().getValue().indexOf(">");
            String from = header.get().getValue().substring(leftIndex,rightIndex);
            return from;
        }
        return header.get().getValue();
    }
}
