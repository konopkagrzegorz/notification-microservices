package com.konopka.emailfilteringservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (filterService.getKeyByFromAndValue(emailDTO).isPresent())
            return ResponseEntity.ok(Boolean.TRUE);
        return ResponseEntity.ok(Boolean.FALSE);
    }
}
