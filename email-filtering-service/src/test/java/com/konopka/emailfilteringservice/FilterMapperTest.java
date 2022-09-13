package com.konopka.emailfilteringservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;


class FilterMapperTest {

    private FilterMapper filterMapper = new FilterMapper();


    @ParameterizedTest
    @MethodSource("dataFilterToFilterDTO")
    void mapFilterToFilterDTO_shouldReturnMappedFilter(Filter given, FilterDTO expected) {
        FilterDTO actual = filterMapper.mapFilterToFilterDTO(given);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("dataFilterDTOtoFilter")
    void mapFilterDTOtoFilter_shouldReturnMappedFilterDTO(Filter expected, FilterDTO given) {
        Filter actual = filterMapper.mapFilterDTOToFilter(given);
        Assertions.assertEquals(expected, actual);
    }

    private static Stream<Arguments> dataFilterToFilterDTO() {
        return Stream.of(
                arguments(new Filter.FilterBuilder().id(1L).major("major").val("value").build(),
                        new FilterDTO.FilterDTOBuilder().major("major").val("value").build()),
                arguments(new Filter.FilterBuilder().id(2L).major("terra").val(null).build(),
                        new FilterDTO.FilterDTOBuilder().major("terra").val(null).build()),
                arguments(new Filter.FilterBuilder().id(3L).major("example").val("value").build(),
                        new FilterDTO.FilterDTOBuilder().major("example").val("value").build()));
    }

    private static Stream<Arguments> dataFilterDTOtoFilter() {
        return Stream.of(
                arguments(new Filter.FilterBuilder().id(null).major("major").val("value").build(),
                        new FilterDTO.FilterDTOBuilder().major("major").val("value").build()),
                arguments(new Filter.FilterBuilder().id(null).major("terra").val(null).build(),
                        new FilterDTO.FilterDTOBuilder().major("terra").val(null).build()),
                arguments(new Filter.FilterBuilder().id(null).major("example").val("value").build(),
                        new FilterDTO.FilterDTOBuilder().major("example").val("value").build()));
    }
}
