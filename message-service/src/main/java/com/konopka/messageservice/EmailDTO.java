package com.konopka.messageservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDTO {

    private String from;
    private String subject;
    private String body;
    private String date;
    private String messageId;

}
