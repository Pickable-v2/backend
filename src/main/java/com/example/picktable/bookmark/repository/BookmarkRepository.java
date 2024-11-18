package com.example.picktable.bookmark.repository;

import com.example.picktable.bookmark.domain.entity.Bookmark;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.restaurant.domain.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b WHERE b.member.id = :memberId")
    Page<Bookmark> findAllByMemberId(Long memberId, Pageable pageable);
    boolean existsByMemberAndRestaurant(Member member, Restaurant restaurant);

}
