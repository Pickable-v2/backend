package com.example.picktable.notice.repository;

import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByMember(Member member);
}
