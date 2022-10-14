package com.konopka.emailfilteringservice;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/filtering/api")
public class FilteringController {

    private final FilterService filterService;

    @Autowired
    public FilteringController(FilterService filterService) {
        this.filterService = filterService;
    }

    @PutMapping("/filter")
    @ApiOperation(
            value = "Check if mail contains specific keys",
            produces = "application/json",
            consumes = "application/json")
    @ApiResponses({@ApiResponse(message = "OK", code = 200, response = Boolean.class),
                   @ApiResponse(message = "Bad request",code = 400),
                   @ApiResponse(message = "Server error",code = 500)})
    public ResponseEntity<Boolean> mailContainsKey(@RequestBody EmailDTO emailDTO) {
        log.info("Calling the {} to check if mail contains searched key\n" +
                "EmailUuid: {}", filterService.getClass().getSimpleName(), emailDTO.getMessageId());
        if (filterService.getKeyByFromAndValue(emailDTO).isPresent())
            return ResponseEntity.ok(Boolean.TRUE);
        return ResponseEntity.ok(Boolean.FALSE);
    }
}
