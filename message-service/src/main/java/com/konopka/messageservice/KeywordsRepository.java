package com.konopka.messageservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordsRepository extends JpaRepository<KeyPattern, Long> {

    List<KeyPattern> findByKeyword(String keyword);
}
