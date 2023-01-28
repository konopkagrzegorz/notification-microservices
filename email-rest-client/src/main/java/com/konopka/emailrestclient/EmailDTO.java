package com.konopka.emailrestclient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDTO {

    @Schema(description = "From who is an email", name = "from", required = true, example = "example@example.com")
    private String from;
    @Schema(description = "Subject", name = "subject", required = true, example = "Example subject")
    private String subject;
    @Schema(description = "Body", name = "body", required = true, example = "Body")
    private String body;
    @Schema(description = "Sent date", name = "date", required = true, example = "Mon, 12 Sep 2022 19:29:39 +0200")
    private String date;
    @Schema(description = "UUID of received email", name = "messageId", required = true, example = "18332c00ef8c07fb")
    private String messageId;


}
