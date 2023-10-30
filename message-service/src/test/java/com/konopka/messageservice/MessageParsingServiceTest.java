package com.konopka.messageservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class MessageParsingServiceTest {

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private KeywordsRepository keywordsRepository;

    @Mock
    private TemplateRepository templateRepository;

    @InjectMocks
    private MessageParsingService messageParsingService;


    @Test
    void convertEmailDTOtoMessageDTO() {
        Mockito.when(keywordsRepository.findByKeyword("example@example.com"))
                .thenReturn(List.of(
                        new KeyPattern.KeyPatternBuilder()
                                .id(1L)
                                .keyword("example@example.com")
                                .type("invoice")
                                .pattern("INVOICE")
                                .build(),
                        new KeyPattern.KeyPatternBuilder()
                                .id(1L)
                                .keyword("example@example.com")
                                .type("payment")
                                .pattern("PAYMENT")
                                .build(),
                        new KeyPattern.KeyPatternBuilder()
                                .id(1L)
                                .keyword("example@example.com")
                                .type("date")
                                .pattern("[0-9]{2}-[0-9]{2}-[0-9]{4}")
                                .build()));

        Mockito.when(templateRepository.findByAddress("example@example.com"))
                .thenReturn(Optional.of(
                        new Template.TemplateBuilder()
                                .id(1L)
                                .address("example@example.com")
                                .body("Invoice: invoice, Payment: payment, Date: date")
                                .build()));

        Mockito.when(messageMapper.convertStringToDate(any())).thenReturn(LocalDate.of(2022,1,21));

        EmailDTO emailDTO = new EmailDTO.EmailDTOBuilder()
                .messageId("e976f0d4-22d1-48be-a724-ef6c3879f429")
                .from("example@example.com")
                .date("21-01-2022")
                .subject("Example")
                .body("New INVOICE, PAYMENT, 17-01-2022 21-01-2022")
                .build();

        MessageDTO expected = new MessageDTO.MessageDTOBuilder()
                .emailUuid("e976f0d4-22d1-48be-a724-ef6c3879f429")
                .status(Status.NOT_SENT)
                .body("Invoice: INVOICE, Payment: PAYMENT, Date: 21-01-2022")
                .sendDate(LocalDate.of(2022,1,19))
                .build();

        Optional<MessageDTO> actual = messageParsingService.convertEmailDTOtoMessageDTO(emailDTO);

        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void convertEmailDTOtoEmptyOptional() {
        Mockito.when(keywordsRepository.findByKeyword(anyString())).thenReturn(Collections.emptyList());
        Mockito.when(templateRepository.findByAddress(anyString())).thenReturn(Optional.empty());
        EmailDTO emailDTO = new EmailDTO.EmailDTOBuilder()
                .messageId("e976f0d4-22d1-48be-a724-ef6c3879f429")
                .from("example@example.com")
                .date("21-01-2022")
                .subject("Example")
                .body("New INVOICE, PAYMENT, 17-01-2022 21-01-2022")
                .build();

        Optional<MessageDTO> actual = messageParsingService.convertEmailDTOtoMessageDTO(emailDTO);
        Assertions.assertThat(actual).isEmpty();
    }
}
