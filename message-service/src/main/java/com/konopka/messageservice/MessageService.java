package com.konopka.messageservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public List<MessageDTO> findAllMessagesWithStatus(Status status) {
        log.debug("Trying to find messages based on status: {}", status);
        return messageRepository.findByStatus(status).stream().map(messageMapper::messageToMessageDTO)
                .collect(Collectors.toList());
    }

    public Optional<MessageDTO> findByEmailUuid(String emailUuid) {
        log.debug("Trying to find messages based on uuid: {}", emailUuid);
        return messageRepository.findByEmailUuid(emailUuid).map(messageMapper::messageToMessageDTO);
    }

    public Optional<MessageDTO> save(MessageDTO messageDTO) {
        log.debug("Saving a message: {}", messageDTO);
        messageRepository.save(messageMapper.messageDtoToMessage(messageDTO));
        return Optional.of(messageDTO);
    }

    public Optional<MessageDTO> update(MessageDTO messageDTO) {
        Optional<Message> message = messageRepository.findByEmailUuid(messageDTO.getEmailUuid());
        message.ifPresentOrElse(msg -> {
            msg.setStatus(messageDTO.getStatus());
            log.debug("Updating a message: {}", messageDTO);
            messageRepository.save(msg);
        }, () -> log.error("Cannot find message with {} email UUID", messageDTO.getEmailUuid()));
        return Optional.of(messageDTO);
    }

    public List<MessageDTO> findAll() {
        log.debug("Fetching all emails");
        return messageRepository.findAll().stream().map(messageMapper::messageToMessageDTO).collect(Collectors.toList());
    }
}


