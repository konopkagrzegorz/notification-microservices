package com.konopka.notificationservice;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
public class SmsServiceClient {

    private String ACCOUNT_SID ="";

    private String AUTH_TOKEN = "";

    private String PHONE_NUMBER = "";

    private String PHONE_NUMBER_TO = "";

    private Properties properties;


   @PostConstruct
    public void setUp() {
        ACCOUNT_SID = properties.getProperty(NotificationServiceConfig.ACCOUNT_SID_KEY);
        AUTH_TOKEN = properties.getProperty(NotificationServiceConfig.AUTH_TOKEN_KEY);
        PHONE_NUMBER = properties.getProperty(NotificationServiceConfig.PHONE_NUMBER_KEY);
        PHONE_NUMBER_TO = properties.getProperty(NotificationServiceConfig.PHONE_NUMBER_TO_KEY);

    }

    @Autowired
    public SmsServiceClient(Properties properties) {
        this.properties = properties;
    }

    public void sendSMS(MessageDTO messageDTO) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new com.twilio.type.PhoneNumber(PHONE_NUMBER_TO),
                        new com.twilio.type.PhoneNumber(PHONE_NUMBER), messageDTO.getBody())
                .create();

    }
}
