package com.konopka.messageservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@TestPropertySource("classpath:application.yml")
@Sql("classpath:data-test.sql")
class KeywordsRepositoryIT {

    @Autowired
    KeywordsRepository keywordsRepository;

    @ParameterizedTest
    @MethodSource("data")
    void findByKeyword_shouldReturnListOfKeyPattern(String given, List<KeyPattern> expected) {
        List<KeyPattern> actual = keywordsRepository.findByKeyword(given);
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).hasSameElementsAs(expected);
    }

    private static Stream<Arguments> data() {
        return Stream.of(
                arguments("example@example.com", List.of(
                        new KeyPattern.KeyPatternBuilder()
                                .id(10L)
                                .keyword("example@example.com")
                                .type("invoice")
                                .pattern("invoice")
                                .build(),
                        new KeyPattern.KeyPatternBuilder()
                                .id(20L)
                                .keyword("example@example.com")
                                .type("payment")
                                .pattern("payment")
                                .build(),
                        new KeyPattern.KeyPatternBuilder()
                                .id(30L)
                                .keyword("example@example.com")
                                .type("date")
                                .pattern("date")
                                .build())),
                arguments("not-found", Collections.emptyList()));
    }
}
