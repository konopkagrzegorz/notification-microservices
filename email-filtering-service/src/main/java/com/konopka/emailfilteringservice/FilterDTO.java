package com.konopka.emailfilteringservice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterDTO {

    @Schema(description = "Key for a filter", name = "key",
                required = true, example = "example@example.com")
    private String major;
    @Schema(description = "Value for a filter", name = "value",
                required = true, example = "Invoice")
    private String val;
    }
