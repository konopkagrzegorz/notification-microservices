package com.konopka.emailrestclient;

import nl.altindag.log.LogCaptor;
import org.apache.commons.lang.NotImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailRepository emailRepository;
    @Mock
    private MailServiceReceiver mailServiceReceiver;

    private EmailMapper emailMapper = new EmailMapper();
    @Mock
    private EmailFilteringServiceClient emailFilteringServiceClient;
    @Mock
    private MessageService messageService;

    @InjectMocks
    private EmailService emailService;


    @BeforeEach
    void setUp() {
        emailService = new EmailService(emailRepository, mailServiceReceiver, emailMapper, emailFilteringServiceClient, messageService);
    }

    @Test
    void getNewMessages_shouldLogAndError() throws IOException {
        Mockito.doThrow(IOException.class).when(mailServiceReceiver).handleReceiveEmail();
        LogCaptor captor = LogCaptor.forClass(EmailService.class);
        emailService.getNewMessages();
        assertThat(captor.getErrorLogs()).containsExactly("Cannot fetch emails from GMAIL API");
    }

    @Test
    void getEmails_shouldReturnListOfEmails() {
        Mockito.when(emailRepository.findAll()).thenReturn(List.of(Email.builder().id(1L).body("Example").build()));
        List<EmailDTO> expected = List.of(EmailDTO.builder().body("Example").build());
        assertThat(emailService.getEmails().size()).isEqualTo(1);
        assertThat(emailService.getEmails()).isEqualTo(expected);
    }

    @Nested
    class EmailServiceTestStub {

        private EmailRepository emailRepository = new EmailRepository() {

            private List<Email> emails = new ArrayList<>(List.of(new Email(1L, "example@example.com", "Subject", "Body", "22-02-2022", "100")));

            @Override
            public Optional<Email> findEmailByMessageId(String messageId) {
                return emails.stream().filter(email -> email.getMessageId().equals(messageId)).findFirst();
            }

            @Override
            public List<Email> findAll() {
                return emails;
            }

            @Override
            public List<Email> findAll(Sort sort) {
                throw new NotImplementedException();
            }

            @Override
            public List<Email> findAllById(Iterable<Long> longs) {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> List<S> saveAll(Iterable<S> entities) {
                throw new NotImplementedException();
            }

            @Override
            public void flush() {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> S saveAndFlush(S entity) {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> List<S> saveAllAndFlush(Iterable<S> entities) {
                throw new NotImplementedException();
            }

            @Override
            public void deleteAllInBatch(Iterable<Email> entities) {
                throw new NotImplementedException();
            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {
                throw new NotImplementedException();
            }

            @Override
            public void deleteAllInBatch() {
                throw new NotImplementedException();
            }

            @Override
            public Email getOne(Long aLong) {
                throw new NotImplementedException();
            }

            @Override
            public Email getById(Long aLong) {
                return emails.stream().filter(email -> email.getId().equals(aLong)).findAny().orElse(null);
            }

            @Override
            public Email getReferenceById(Long aLong) {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> List<S> findAll(Example<S> example) {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> List<S> findAll(Example<S> example, Sort sort) {
                throw new NotImplementedException();
            }

            @Override
            public Page<Email> findAll(Pageable pageable) {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> S save(S entity) {
                emails.add(entity);
                return entity;
            }

            @Override
            public Optional<Email> findById(Long aLong) {
                return emails.stream().filter(email -> email.getId().equals(aLong)).findAny();
            }

            @Override
            public boolean existsById(Long aLong) {
                throw new NotImplementedException();
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {
                throw new NotImplementedException();
            }

            @Override
            public void delete(Email entity) {
                throw new NotImplementedException();
            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {
                throw new NotImplementedException();
            }

            @Override
            public void deleteAll(Iterable<? extends Email> entities) {
                throw new NotImplementedException();
            }

            @Override
            public void deleteAll() {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> Optional<S> findOne(Example<S> example) {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> Page<S> findAll(Example<S> example, Pageable pageable) {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> long count(Example<S> example) {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email> boolean exists(Example<S> example) {
                throw new NotImplementedException();
            }

            @Override
            public <S extends Email, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                throw new NotImplementedException();
            }
        };

        @Mock
        private MailServiceReceiver mailServiceReceiver;

        private EmailMapper emailMapper = new EmailMapper();
        @Mock
        private EmailFilteringServiceClient emailFilteringServiceClient;
        @Mock
        private MessageService messageService;

        @InjectMocks
        private EmailService emailService;

        @BeforeEach
        void setUp() {
            this.emailService = new EmailService(emailRepository, mailServiceReceiver, emailMapper, emailFilteringServiceClient, messageService);
        }

        @Test
        void getNewMessages_shouldReturnUpdatedListOfMessages() throws IOException {
            Mockito.when(emailFilteringServiceClient.isMailInFilteringService(any(EmailDTO.class))).thenReturn(Boolean.TRUE);
            Mockito.when(mailServiceReceiver.handleReceiveEmail()).thenReturn(List.of(
                    EmailDTO.builder()
                            .from("example@example.com")
                            .subject("Subject")
                            .body("Body")
                            .date("22-02-2022")
                            .messageId("100").build(),
                    EmailDTO.builder()
                            .from("example@example.com")
                            .subject("Subject")
                            .body("Body")
                            .date("23-02-2022")
                            .messageId("200").build()));


            List<EmailDTO> expected = List.of(EmailDTO.builder()
                            .from("example@example.com")
                            .subject("Subject")
                            .body("Body")
                            .date("22-02-2022")
                            .messageId("100").build(),
                    EmailDTO.builder()
                            .from("example@example.com")
                            .subject("Subject")
                            .body("Body")
                            .date("23-02-2022")
                            .messageId("200").build());

            List<EmailDTO> actual = emailService.getNewMessages();
            assertThat(actual).hasSameElementsAs(expected);
        }
    }
}