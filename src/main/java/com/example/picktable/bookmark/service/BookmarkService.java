package com.example.picktable.bookmark.service;

import com.example.picktable.bookmark.domain.dto.BookmarkRequestDTO;
import com.example.picktable.bookmark.domain.dto.BookmarkResponseDTO;
import com.example.picktable.global.domain.dto.MsgResponseDTO;
import com.example.picktable.bookmark.domain.entity.Bookmark;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.restaurant.domain.entity.Restaurant;
import com.example.picktable.bookmark.repository.BookmarkRepository;
import com.example.picktable.member.repository.MemberRepository;
import com.example.picktable.restaurant.repository.RestaurantRepository;
import com.example.picktable.member.security.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void addBookmark(Long restaurantId, BookmarkRequestDTO bookmarkRequestDTO) {
        String loginId = SecurityUtil.getLoginId();
        Member member = memberRepository.findByLoginId(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 음식점입니다."));

        if (bookmarkRepository.existsByMemberAndRestaurant(member, restaurant)) {
            throw new IllegalArgumentException("이미 즐겨찾기한 음식점입니다.");
        }

        Bookmark bookmark = bookmarkRequestDTO.toSaveEntity(member, restaurant);
        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public MsgResponseDTO cancelBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
        return new MsgResponseDTO("즐겨찾기 취소", 200);
    }

    @Transactional
    public Page<BookmarkResponseDTO> findAllBookmarks(Pageable pageable) {
        Member member = getCurrentMember();

        Page<Bookmark> bookmarkPage = bookmarkRepository.findAllByMemberId(member.getId(), pageable);
        return bookmarkPage.map(BookmarkResponseDTO::new);
    }

    private Member getCurrentMember() {
        String loginId = SecurityUtil.getLoginId();
        return memberRepository.findByLoginId(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
    }
}
