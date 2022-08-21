package com.konopka.emailfilteringservice;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FilterService {

    private final FilterRepository filterRepository;
    private final FilterMapper filterMapper;

    public FilterService(FilterRepository filterRepository, FilterMapper filterMapper) {
        this.filterRepository = filterRepository;
        this.filterMapper = filterMapper;
    }

    Optional<FilterDTO> getKeyByFromAndValue(EmailDTO emailDTO) {
        List<String> from = convertEmailDtoToFromList(emailDTO);
        List<String> value = convertEmailDtoToBodyList(emailDTO);

        Optional<FilterDTO> filterDTO = Optional.empty();
        for (String key : from) {
            if (filterRepository.findByMajor(key).isPresent()) {
                for (String val : value) {
                    if (filterRepository.findByMajorAndVal(key,val).isPresent()) {
                        filterDTO = Optional.of(filterMapper.mapKeyToKeyDTO(filterRepository.findByMajorAndVal(key,val).get()));
                        break;
                    }

                }
            }
        }
        return filterDTO;
    }

    private List<String> convertEmailDtoToFromList(EmailDTO emailDTO) {
        String from = emailDTO.getFrom()
                .replace("\r", "")
                .replace("\n","")
                .replace("<","")
                .replace(">","");
        List<String> fromList = List.of(from.split(" "));
        return fromList;
    }

    private List<String> convertEmailDtoToBodyList(EmailDTO emailDTO) {
        if (emailDTO.getBody() == null)
            return Collections.EMPTY_LIST;
        String body = emailDTO.getBody()
                .replace("\r", "")
                .replace("\n","");
        return List.of(body.split(" "));
    }
}
