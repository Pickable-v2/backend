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

    //리뷰 좋아요
    //리뷰 좋아요 등록
    @Transactional
    public void save(Long reviewId, LikesRequestDTO likesRequestDTO) {
        String loginId = SecurityUtil.getLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        // 좋아요 상태 확인
        boolean isLiked = likesRepository.existsByReviewIdAndMemberIdAndState(reviewId, member.getId(), true);

        if (isLiked) {
            // 이미 좋아요를 눌렀으면 좋아요 취소
            delete(reviewId);
        } else {
            // 좋아요를 누르지 않았다면 좋아요 등록
            Likes likes = likesRequestDTO.toSaveEntity(member, review, true);
            likesRepository.save(likes);
            review.setTotalLikes(review.getTotalLikes() + 1); // 리뷰 좋아요 개수 증가
        }
    }

    // 리뷰 좋아요 취소
    @Transactional
    public void delete(Long reviewId) {
        String loginId = SecurityUtil.getLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        Long memberId = member.getId();
        Likes likes = likesRepository.findByReviewIdAndMemberIdAndState(reviewId, memberId, true).orElseThrow(() -> new RuntimeException("좋아요를 찾을 수 없습니다."));
        likesRepository.delete(likes);
        review.setTotalLikes(review.getTotalLikes() - 1); // 리뷰 좋아요 개수 감소
    }
    @Transactional
    public void toggleLike(Long reviewId) {
        String loginId = SecurityUtil.getLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        // 좋아요 상태 확인
        boolean isLiked = likesRepository.existsByReviewIdAndMemberIdAndState(reviewId, member.getId(), true);
        if (isLiked) {
            // 이미 좋아요를 눌렀으면 좋아요 취소
            Likes likes = likesRepository.findByReviewIdAndMemberIdAndState(reviewId, member.getId(), true)
                    .orElseThrow(() -> new RuntimeException("좋아요를 찾을 수 없습니다."));
            likesRepository.delete(likes);
            review.setTotalLikes(review.getTotalLikes() - 1); // 리뷰 좋아요 개수 감소
        } else {
            // 아직 좋아요를 누르지 않았으면 좋아요 등록
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
            review.setTotalLikes(review.getTotalLikes() + 1); // 리뷰 좋아요 개수 증가
        }
    }
}
