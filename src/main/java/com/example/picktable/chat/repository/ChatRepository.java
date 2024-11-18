package com.example.picktable.chat.repository;

import com.example.picktable.chat.domain.entity.Chat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    Chat findOneByRoomId(Long roomId);
}
