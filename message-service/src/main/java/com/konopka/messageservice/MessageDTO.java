package com.konopka.messageservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Body", name = "body", required = true, example = "Body")
    private String body;
    @Schema(description = "UUID of received email", name = "emailUuid", required = true, example = "18332c00ef8c07fb")
    private String emailUuid;
    @Schema(description = "Deadline", name = "date", required = true, example = "23-02-2023")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate sendDate;
    @Schema(description = "Status", name = "date", example = "NOT_SENT")
    private Status status;

}
