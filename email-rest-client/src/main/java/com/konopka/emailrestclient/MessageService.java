package com.konopka.emailrestclient;

public interface MessageService {

    <T> T saveMessage(EmailDTO emailDTO);

}
