package com.konopka.messageservice;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public List<MessageDTO> findAllMessagesWithNotSentStatus() {
        return messageRepository.findByStatus("NOT_SENT").stream().map(messageMapper::messageToMessageDTO)
                .collect(Collectors.toList());
    }

    public Optional<MessageDTO> findByEmailUuid(String emailUuid) {
        return messageRepository.findByEmailUuid(emailUuid).map(messageMapper::messageToMessageDTO);
    }

    public Optional<MessageDTO> save(MessageDTO messageDTO) {
        messageRepository.save(messageMapper.messageDtoToMessage(messageDTO));
        return Optional.of(messageDTO);
    }

    public List<MessageDTO> findAll() {
        return messageRepository.findAll().stream().map(messageMapper::messageToMessageDTO).collect(Collectors.toList());
    }
}
