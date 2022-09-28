package com.konopka.emailfilteringservice;

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
    public ResponseEntity<Boolean> mailContainsKey(@RequestBody EmailDTO emailDTO) {
        log.info("Calling the {} to check if mail contains searched key\n" +
                "EmailUuid: {}" , filterService.getClass().getSimpleName(), emailDTO.getMessageId());
        if (filterService.getKeyByFromAndValue(emailDTO).isPresent())
            return ResponseEntity.ok(Boolean.TRUE);
        return ResponseEntity.ok(Boolean.FALSE);
    }
}
