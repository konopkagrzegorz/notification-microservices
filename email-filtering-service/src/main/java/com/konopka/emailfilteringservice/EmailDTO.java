package com.konopka.emailfilteringservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EmailDTO {

    private String from;
    private String subject;
    private String body;
    private String date;
    private String messageId;

}
