package com.konopka.messageservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByStatus(String status);
    Optional<Message> findByEmailUuid(String emailUuid);
}
