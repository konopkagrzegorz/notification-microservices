package com.konopka.emailrestclient;

import java.io.IOException;
import java.util.List;

public interface MailService {
    List<EmailDTO> handleReceiveEmail() throws IOException;
}
