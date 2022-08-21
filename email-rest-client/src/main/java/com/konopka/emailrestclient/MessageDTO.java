package com.konopka.emailrestclient;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    private String body;
    private String emailUuid;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate sendDate;
    private Status status;

}
