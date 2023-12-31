package com.konopka.messageservice;

import io.dropwizard.testing.FixtureHelpers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailDTODeserializerTest {

    private EmailDTODeserializer classUnderTest = new EmailDTODeserializer();

    @Test
    void deserializeShouldReturnEmailDTO() {
        String serialized = FixtureHelpers.fixture("json/email-dto.json");

        EmailDTO expected = EmailDTO.builder()
                .from("example@example.com")
                .subject("Example subject")
                .body("Body")
                .messageId("18332c00ef8c07fb")
                .date("Mon, 12 Sep 2022 19:29:39 +0200")
                .build();

        EmailDTO actual = classUnderTest.deserialize("raw", serialized.getBytes());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deserializeShouldReturnNull() {
        EmailDTO actual = classUnderTest.deserialize("raw", null);
        Assertions.assertThat(actual).isEqualTo(null);
    }
}