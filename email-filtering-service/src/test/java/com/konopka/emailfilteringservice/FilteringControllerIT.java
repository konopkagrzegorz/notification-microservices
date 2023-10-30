package com.konopka.emailfilteringservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilteringControllerIT {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FilterRepository filterRepository;


    @Test
    void mailContainsKey_shouldReturnTrue() throws Exception {
        //given
        EmailDTO given = new EmailDTO.EmailDTOBuilder()
                .from("Example Example <example@example.com>")
                .body("Payment")
                .date(LocalDate.now().toString())
                .messageId(UUID.randomUUID().toString())
                .subject("Example")
                .build();

        ObjectMapper mapper = new ObjectMapper();

        //when
        Mockito.when(filterRepository.findByMajor("Example")).thenReturn(Optional.empty(), Optional.empty());
        Mockito.when(filterRepository.findByMajor("example@example.com")).thenReturn(Optional.of(
                new Filter.FilterBuilder().id(1L).major("example@example.com").val("Payment").build()));
        Mockito.when(filterRepository.findByMajorAndVal("example@example.com", "Payment")).thenReturn(
                Optional.of(new Filter.FilterBuilder().id(1L).major("example@example.com").val("Payment").build()));

        // then
        MvcResult result = mockMvc.perform(put("/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(given)))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("true");
    }

    @Test
    void mailContainsKey_shouldReturnFalse() throws Exception {
        //given
        EmailDTO given = new EmailDTO.EmailDTOBuilder()
                .from("Example Example <example@example.com>")
                .body("Invoice")
                .date(LocalDate.now().toString())
                .messageId(UUID.randomUUID().toString())
                .subject("Example")
                .build();

        ObjectMapper mapper = new ObjectMapper();

        //when
        Mockito.when(filterRepository.findByMajor("Example")).thenReturn(Optional.empty(), Optional.empty());
        Mockito.when(filterRepository.findByMajor("example@example.com")).thenReturn(Optional.of(
                new Filter.FilterBuilder().id(1L).major("example@example.com").val("Invoice").build()));
        Mockito.when(filterRepository.findByMajorAndVal("example@example.com", "Invoice")).thenReturn(
                Optional.empty());

        // then
        MvcResult result = mockMvc.perform(put("/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(given)))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("false");
    }

    @Test
    void mailContainsKey_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(put("/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((byte[]) null))
                .andExpect(status().isBadRequest());
    }
}
