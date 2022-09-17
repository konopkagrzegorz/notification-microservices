package com.konopka.messageservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@TestPropertySource("classpath:application.yml")
@Sql("classpath:data-test.sql")
class TemplateRepositoryIT {

    @Autowired
    TemplateRepository templateRepository;

    @ParameterizedTest
    @MethodSource("dataShouldReturnTemplate")
    void findByAddress_shouldReturnTemplate(String given, Template expected) {
        Optional<Template> actual = templateRepository.findByAddress(given);
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void findByAddress_shouldReturnEmptyOptional() {
        Optional<Template> actual = templateRepository.findByAddress("empty");
        Assertions.assertThat(actual).isEmpty();
    }

    private static Stream<Arguments> dataShouldReturnTemplate() {
        return Stream.of(
                arguments("example@mailgo.com",
                        new Template.TemplateBuilder()
                                .id(10L)
                                .address("example@mailgo.com")
                                .body("Invoice invoice payment date")
                                .build()),
                arguments("example@mailgo.org",
                        new Template.TemplateBuilder()
                                .id(20L)
                                .address("example@mailgo.org")
                                .body("Payment invoice payment date")
                                .build())
        );
    }
}