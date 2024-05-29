package com.jellybrains.quietspace_backend_ms.chatservice.repository;

import dev.thural.quietspace.entity.Chat;
import dev.thural.quietspace.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    Page<Message> findAllByChatId(UUID chatId, Pageable pageable);
    Optional<Message> findFirstByChatOrderByCreateDateDesc(Chat chat);
}
