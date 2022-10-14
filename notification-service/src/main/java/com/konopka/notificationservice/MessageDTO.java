package com.konopka.notificationservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "Message's body", required = true, example = "Body")
    private String body;
    @ApiModelProperty(value = "Email's UUID", required = true, example = "18332c00ef8c07fb")
    private String emailUuid;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @ApiModelProperty(value = "Deadline for message", required = true, example = "22-02-2022")
    private LocalDate sendDate;
    @ApiModelProperty(value = "Message's status", required = true, example = "NOT_SENT")
    private Status status;

}
