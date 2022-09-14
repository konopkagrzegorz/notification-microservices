package com.konopka.emailfilteringservice;

import org.junit.jupiter.api.Assertions;
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
class FilterRepositoryIT {

    @Autowired
    private FilterRepository filterRepository;

    @ParameterizedTest
    @MethodSource("dataShouldReturnFilterByMajor")
    void findByMajor_shouldReturnFilter(String search, Filter expected) {
        Filter actual = filterRepository.findByMajor(search).orElse(null);
        Assertions.assertEquals(expected,actual);
    }

    @ParameterizedTest
    @MethodSource("dataShouldReturnEmptyOptional")
    void findByMajor_shouldReturnEmptyOptional(String search) {
        Optional<Filter> actual = filterRepository.findByMajor(search);
        Assertions.assertTrue(actual.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("dataShouldReturnFilterByMajorAndVal")
    void findByMajorAndVal_shouldReturnFilter(String major, String value, Filter expected) {
        Filter actual = filterRepository.findByMajorAndVal(major,value).orElse(null);
        Assertions.assertEquals(expected,actual);
    }

    @ParameterizedTest
    @MethodSource("dataShouldReturnFilterByMajorAndVal")
    void findByMajorAndVal_shouldReturnEmptyOptional(String major, String value) {
        Optional<Filter> actual = filterRepository.findByMajorAndVal(value,major);
        Assertions.assertTrue(actual.isEmpty());
    }


    private static Stream<Arguments> dataShouldReturnFilterByMajor() {
        return Stream.of(
                arguments("example.com",
                        new Filter.FilterBuilder().id(10L).major("example@example.com").val("Invoice").build()),
                arguments("example.org",
                        new Filter.FilterBuilder().id(11L).major("example@example.org").val("Payment").build()));
    }

    private static Stream<Arguments> dataShouldReturnFilterByMajorAndVal() {
        return Stream.of(
                arguments("example.com", "Inv",
                        new Filter.FilterBuilder().id(10L).major("example@example.com").val("Invoice").build()),
                arguments("example.org", "ment",
                        new Filter.FilterBuilder().id(11L).major("example@example.org").val("Payment").build()));
    }

    private static Stream<Arguments> dataShouldReturnEmptyOptional() {
        return Stream.of(
                arguments("not-present"),
                arguments("test"));
    }
}
