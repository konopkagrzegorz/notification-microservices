package com.konopka.emailfilteringservice;

import org.springframework.stereotype.Component;

@Component
public class FilterMapper {

    public FilterDTO mapFilterToFilterDTO(Filter filter) {
        return new FilterDTO.FilterDTOBuilder()
                .major(filter.getMajor())
                .val(filter.getVal())
                .build();
    }

    public Filter mapFilterDTOToFilter(FilterDTO filterDTO) {
        return new Filter.FilterBuilder()
                .major(filterDTO.getMajor())
                .val(filterDTO.getVal())
                .build();
    }
}
