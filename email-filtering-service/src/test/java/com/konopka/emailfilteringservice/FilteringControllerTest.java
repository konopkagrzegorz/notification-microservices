package com.konopka.emailfilteringservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class FilteringControllerTest {

    @Mock
    FilterService filterService;

    @InjectMocks
    FilteringController filteringController;

    @Test
    void mailContainsKey_shouldReturnTrue() {
        EmailDTO emailDTO = new EmailDTO.EmailDTOBuilder().build();
        Mockito.when(filterService.getKeyByFromAndValue(emailDTO))
                .thenReturn(Optional.of(new FilterDTO()));

        ResponseEntity<Boolean> actual = filteringController.mailContainsKey(emailDTO);
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual.getBody()).isEqualTo(Boolean.TRUE);
    }

    @Test
    void mailContainsKey_shouldReturnFalse() {
        EmailDTO emailDTO = new EmailDTO.EmailDTOBuilder().build();
        Mockito.when(filterService.getKeyByFromAndValue(emailDTO))
                .thenReturn(Optional.empty());

        ResponseEntity<Boolean> actual = filteringController.mailContainsKey(emailDTO);
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual.getBody()).isEqualTo(Boolean.FALSE);
    }
}
