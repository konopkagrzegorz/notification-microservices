package com.konopka.messageservice;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MessageParsingService {

    private final KeywordsRepository keywordsRepository;
    private final TemplateRepository templateRepository;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageParsingService(KeywordsRepository keywordsRepository, TemplateRepository templateRepository, MessageMapper messageMapper) {
        this.keywordsRepository = keywordsRepository;
        this.templateRepository = templateRepository;
        this.messageMapper = messageMapper;
    }


    public Optional<MessageDTO> convertEmailDTOtoMessageDTO(@NonNull EmailDTO emailDTO) {
        List<KeyPattern> keywords = keywordsRepository.findByKeyword(emailDTO.getFrom());
        Optional<Template> template = templateRepository.findByAddress(emailDTO.getFrom());
        if (template.isPresent()) {
            String body = template.get().getBody();
            String date = null;
            for (KeyPattern keyword : keywords) {
                Pattern pattern = Pattern.compile(keyword.getPattern());
                Matcher matcher = pattern.matcher(emailDTO.getBody());
                if (matcher.find() && !keyword.getType().equals("date")) {
                    body = body.replace(keyword.getType(),matcher.group());
                }

                if (keyword.getType().equals("date")) {
                    if (matcher.find()) {
                        date = matcher.group();
                        body = body.replace(keyword.getType(),date);
                    } else {
                        date = matcher.group();
                        body = body.replace(keyword.getType(), date);
                    }
                }
            }
            log.debug("Successfully converted Email with UUID: {} to a message", emailDTO.getMessageId());
            return Optional.of(new MessageDTO.MessageDTOBuilder()
                    .body(body)
                    .emailUuid(emailDTO.getMessageId())
                    .sendDate(messageMapper.convertStringToDate(date).minus(2, ChronoUnit.DAYS))
                    .status(Status.NOT_SENT)
                    .build());
        }
        log.debug("Did not convert Email with UUID: {} to a message (not fulfilled requirements)", emailDTO.getMessageId());
        return Optional.empty();
    }
}
