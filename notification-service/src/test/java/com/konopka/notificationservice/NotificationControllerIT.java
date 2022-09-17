package com.konopka.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8083)
@TestPropertySource("classpath:application.yml")
@ContextConfiguration(classes = NotificationServiceTestConfig.class)
class NotificationControllerIT {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SmsServiceClient smsServiceClient;


    @Test
    void notification_shouldReturnOkStatus() throws Exception {
        List<MessageDTO> expectedResponse = List.of(
                new MessageDTO.MessageDTOBuilder()
                        .body("Message 1")
                        .sendDate(LocalDate.now())
                        .emailUuid(UUID.randomUUID().toString())
                        .status(Status.NOT_SENT).sendDate(LocalDate.now())
                        .build(),
                new MessageDTO.MessageDTOBuilder()
                        .body("Message 2")
                        .sendDate(LocalDate.now())
                        .emailUuid(UUID.randomUUID().toString())
                        .status(Status.NOT_SENT).sendDate(LocalDate.now())
                        .build());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String expectedResponseAsString = objectMapper.writeValueAsString(expectedResponse);

        stubFor(WireMock.get(WireMock.urlEqualTo("/messages?status=NOT_SENT"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedResponseAsString)
                        .withStatus(HttpStatus.OK.value())));

        MvcResult result = mockMvc.perform(get("/notification/api/notify"))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void notification_shouldReturnNoContentStatus() throws Exception {
        List<MessageDTO> expectedResponse = Collections.emptyList();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String expectedResponseAsString = objectMapper.writeValueAsString(expectedResponse);

        stubFor(WireMock.get(WireMock.urlEqualTo("/messages?status=NOT_SENT"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedResponseAsString)
                        .withStatus(HttpStatus.OK.value())));

        MvcResult result = mockMvc.perform(get("/notification/api/notify"))
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
