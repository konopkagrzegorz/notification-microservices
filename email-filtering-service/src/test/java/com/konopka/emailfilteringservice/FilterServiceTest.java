package com.konopka.emailfilteringservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class FilterServiceTest {

    @Mock
    FilterRepository filterRepository;

    @Mock
    FilterMapper filterMapper;

    @InjectMocks
    FilterService filterService;

    @Test
    void getKeyByFromAndValue_shouldReturnFilterDTO() {
        EmailDTO given = new EmailDTO.EmailDTOBuilder()
                .from("Example Example <example@example.com>")
                .body("Payment")
                .date(LocalDate.now().toString())
                .messageId(UUID.randomUUID().toString())
                .subject("Example")
                .build();

        Mockito.when(filterMapper.mapFilterToFilterDTO(
                new Filter.FilterBuilder().id(1L).major("example@example.com").val("Payment").build()))
                .thenReturn(new FilterDTO.FilterDTOBuilder().major("example@example.com").val("Payment").build());
        Mockito.when(filterRepository.findByMajor("Example")).thenReturn(Optional.empty(), Optional.empty());
        Mockito.when(filterRepository.findByMajor("example@example.com")).thenReturn(Optional.of(
                        new Filter.FilterBuilder().id(1L).major("example@example.com").val("Payment").build()));
        Mockito.when(filterRepository.findByMajorAndVal("example@example.com", "Payment")).thenReturn(
                Optional.of(new Filter.FilterBuilder().id(1L).major("example@example.com").val("Payment").build()));

        Optional<FilterDTO> expected = Optional.of(
                new FilterDTO.FilterDTOBuilder().major("example@example.com").val("Payment").build());

        Optional<FilterDTO> actual = filterService.getKeyByFromAndValue(given);
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
