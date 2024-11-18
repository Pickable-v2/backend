package com.example.picktable.meet.repository;

import com.example.picktable.meet.domain.entity.Meet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetRepository extends JpaRepository<Meet, Long> {
    Optional<Meet> findByIdAndMeetMenu(Long chatRoomId, String meetMenu);
}
