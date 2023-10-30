package com.konopka.emailfilteringservice;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FilteringController {

    private final FilterService filterService;

    @Autowired
    public FilteringController(FilterService filterService) {
        this.filterService = filterService;
    }

    @Timed(value = "filter.email",
            description = "Filter - Total execution time of filtering an email",
            percentiles = {0.5, 0.7, 0.9, 0.95})
    @Operation(description = "Checks if given email match the regex pattern")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Boolean.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Bad request"))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Not found"))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "", value = "Server error")))
    })
    @PutMapping("/filter")
    public ResponseEntity<Boolean> mailContainsKey(
            @Parameter( schema = @Schema(implementation = EmailDTO.class)) @RequestBody EmailDTO emailDTO) {
        log.info("Calling the {} to check if mail contains searched key\n" +
                "EmailUuid: {}" , filterService.getClass().getSimpleName(), emailDTO.getMessageId());
        if (filterService.getKeyByFromAndValue(emailDTO).isPresent())
            return ResponseEntity.ok(Boolean.TRUE);
        return ResponseEntity.ok(Boolean.FALSE);
    }
}
