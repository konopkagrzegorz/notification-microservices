package com.konopka.emailfilteringservice;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EmailDTO {

    @ApiModelProperty(value = "From who is an email", required = true, example = "example@example.com")
    private String from;
    @ApiModelProperty(value = "Email's subject", required = true, example = "Example subject")
    private String subject;
    @ApiModelProperty(value = "Email's body", required = true, example = "Body")
    private String body;
    @ApiModelProperty(value = "Email's date", required = true, example = "Mon, 12 Sep 2022 19:29:39 +0200")
    private String date;
    @ApiModelProperty(value = "UUID", required = true, example = "18332c00ef8c07fb")
    private String messageId;

}
