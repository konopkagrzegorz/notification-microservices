package com.konopka.emailrestclient;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MailServiceReceiver implements MailService {
    private static final String user = "me";
    private ObjectFactory<Gmail> gmailObjectFactory;
    private final GmailMessageToEmailMapper mapper;

    @Autowired
    public MailServiceReceiver(ObjectFactory<Gmail> gmailObjectFactory, GmailMessageToEmailMapper mapper) {
        this.gmailObjectFactory = gmailObjectFactory;
        this.mapper = mapper;
    }

    @Override
    public List<EmailDTO> handleReceiveEmail() throws IOException {
        Gmail gmailService = gmailObjectFactory.getObject();
        Gmail.Users.Messages.List request = gmailService.users().messages().list(user);
        ListMessagesResponse messagesResponse = request.execute();
        request.setPageToken(messagesResponse.getNextPageToken());
        List<Message> messages = gmailService.users().messages().list(user).execute().getMessages();
        List<EmailDTO> emails = new ArrayList<>();
        for (Message message : messages) {
            Message msg = gmailService.users().messages().get(user,message.getId()).execute();
            emails.add(mapper.messageToEmailDTO(msg));
        }
        return emails;
    }
}
