package com.example.picktable.likes.service;

import com.example.picktable.likes.domain.dto.LikesRequestDTO;
import com.example.picktable.notice.domain.type.NoticeType;
import com.example.picktable.likes.domain.entity.Likes;
import com.example.picktable.likes.repository.LikesRepository;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.member.repository.MemberRepository;
import com.example.picktable.notice.domain.entity.Notice;
import com.example.picktable.notice.repository.NoticeRepository;
import com.example.picktable.review.repository.ReviewRepository;
import com.example.picktable.member.security.util.SecurityUtil;
import com.example.picktable.review.domain.entity.Review;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final ReviewRepository reviewRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void toggleLike(Long reviewId) {
        String loginId = SecurityUtil.getLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        boolean isLiked = likesRepository.existsByReviewIdAndMemberIdAndState(reviewId, member.getId(), true);
        if (isLiked) {
            Likes likes = likesRepository.findByReviewIdAndMemberIdAndState(reviewId, member.getId(), true)
                    .orElseThrow(() -> new RuntimeException("좋아요를 찾을 수 없습니다."));
            likesRepository.delete(likes);
            review.setTotalLikes(review.getTotalLikes() - 1);
        } else {
            Likes likes = new Likes();
            likes.setMember(member);
            likes.setReview(review);
            likes.setState(true);
            Member reviewWriter = review.getMember();
            String content = member.getNickname() + "님이 " + likes.getReview().getRestaurant().getName() + "의 리뷰에 좋아요를 등록했습니다.";
            Notice notice = new Notice(reviewWriter, content, NoticeType.LIKES);
            likesRepository.save(likes);
            if (reviewWriter != null) {
                noticeRepository.save(notice);
            }
            review.setTotalLikes(review.getTotalLikes() + 1);
        }
    }
}
