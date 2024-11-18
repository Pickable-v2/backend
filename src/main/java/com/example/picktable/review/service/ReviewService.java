package com.example.picktable.review.service;

import com.example.picktable.common.domain.dto.MsgResponseDTO;
import com.example.picktable.restaurant.domain.dto.RestaurantResponseDTO;
import com.example.picktable.review.domain.dto.ReviewRequestDTO;
import com.example.picktable.review.domain.dto.ReviewResponseDTO;
import com.example.picktable.foodType.domain.entity.FoodType;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.restaurant.domain.entity.Restaurant;
import com.example.picktable.review.domain.entity.Review;
import com.example.picktable.review.domain.type.ReviewType;
import com.example.picktable.foodType.repository.FoodTypeRepository;
import com.example.picktable.member.repository.MemberRepository;
import com.example.picktable.restaurant.repository.RestaurantRepository;
import com.example.picktable.review.repository.ReviewRepository;
import com.example.picktable.member.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodTypeRepository foodTypeRepository;

    @Transactional
    public void addReview(Long id, ReviewRequestDTO requestDTO) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        FoodType foodType = foodTypeRepository.findById(restaurant.getFoodType().getId()).orElseThrow(IllegalArgumentException::new);
        restaurant.setTotalReviews(restaurant.getTotalReviews() + 1);
        restaurant.setCount(restaurant.getCount() + 1);
        foodType.setCount(foodType.getCount()+1);
        Review review = new Review(requestDTO);
        Member member = memberRepository.findByLoginId(SecurityUtil.getLoginId()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
        review.setWriter(member.getNickname());
        review.confirmMember(member);
        review.setRestaurant(restaurant);
        review.setStars(requestDTO.getStars());
        reviewRepository.save(review);
        updateRestaurantScores(restaurant, review, true);
        restaurant.calculateDegree(review.getStars());
    }

   public void updateRestaurantScores(Restaurant restaurant, Review review, boolean isAdding) {
        int delta = isAdding ? 1 : -1;
        restaurant.calculateDegree(review.getStars());
        if (review.getMood() == 1) {
            restaurant.setTotalMood(restaurant.getTotalMood() + delta);
        }
        if (review.getKind() == 1) {
            restaurant.setTotalKind(restaurant.getTotalKind() + delta);
        }
        if (review.getCost() == 1) {
            restaurant.setTotalCost(restaurant.getTotalCost() + delta);
        }
        if (review.getPark() == 1) {
            restaurant.setTotalPark(restaurant.getTotalPark() + delta);
        }
        if (review.getTaste() == 1) {
            restaurant.setTotalTaste(restaurant.getTotalTaste() + delta);
        }
    }

    @Transactional
    public Page<RestaurantResponseDTO> findAll(String address, Pageable pageable) {
        Page<Restaurant> pages = (address == null || address.trim().isEmpty())
                ? restaurantRepository.findAll(pageable)
                : restaurantRepository.findByAddress(address, pageable);

        return pages.map(restaurant -> {
            Page<ReviewResponseDTO> reviewList = this.showReviewsByRestaurant(restaurant.getId(), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate")));
            return new RestaurantResponseDTO(restaurant, reviewList);
        });
    }

    @Transactional
    public ReviewResponseDTO findReviewDetails(Long restaurantId, Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return new ReviewResponseDTO(review);
    }


    @Transactional
    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO requestDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        Restaurant restaurant = review.getRestaurant();
        updateRestaurantScoresForReviewUpdate(restaurant, review, requestDTO);
        review.updateReview(requestDTO.getCost(), requestDTO.getPark(), requestDTO.getMood(), requestDTO.getKind(), requestDTO.getTaste(), requestDTO.getStars());
        restaurant.calculateDegree(review.getStars());
        return new ReviewResponseDTO(review);
    }

    @Transactional
    public void updateDateFormat() {
        List<Review> reviewList = reviewRepository.findAll();

        for (Review review : reviewList) {
            review.updateCreatedDateFormat();
        }
    }

    private void updateRestaurantScoresForReviewUpdate(Restaurant restaurant, Review oldReview, ReviewRequestDTO newReview) {
        if (oldReview.getTaste() != newReview.getTaste()) {
            restaurant.setTotalTaste(restaurant.getTotalTaste() + (newReview.getTaste() - oldReview.getTaste()));
        }
        if (oldReview.getCost() != newReview.getCost()) {
            restaurant.setTotalCost(restaurant.getTotalCost() + (newReview.getCost() - oldReview.getCost()));
        }
        if (oldReview.getMood() != newReview.getMood()) {
            restaurant.setTotalMood(restaurant.getTotalMood() + (newReview.getMood() - oldReview.getMood()));
        }
        if (oldReview.getKind() != newReview.getKind()) {
            restaurant.setTotalKind(restaurant.getTotalKind() + (newReview.getKind() - oldReview.getKind()));
        }
        if (oldReview.getPark() != newReview.getPark()) {
            restaurant.setTotalPark(restaurant.getTotalPark() + (newReview.getPark() - oldReview.getPark()));
        }
    }

    @Transactional
    public MsgResponseDTO deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        Long restaurantId = review.getRestaurant().getId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(RuntimeException::new);
        restaurant.setTotalReviews(restaurant.getTotalReviews() - 1);
        restaurant.calculateDegree(-review.getStars());
        updateRestaurantScores(restaurant, review, false);
        reviewRepository.delete(review);
        return new MsgResponseDTO("리뷰 삭제 완료", 200);
    }


    public Page<RestaurantResponseDTO> findAllByAddress(String address, Pageable pageable) {
        List<Restaurant> restaurants = restaurantRepository.findByAddressLatestReview(address);

        List<RestaurantResponseDTO> responseDTOs = restaurants.stream().map(restaurant -> {
                    Page<ReviewResponseDTO> reviewList = this.showReviewsByRestaurant(restaurant.getId(), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate")));
                    return new RestaurantResponseDTO(restaurant, reviewList);
                }).sorted(Comparator.comparing(dto -> dto.getReviewList().getContent().isEmpty() ? null : dto.getReviewList().getContent().get(0).getCreatedDate(), Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        if (pageable == null) {
            System.out.println("Pageable 객체가 없음");
            pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseDTOs.size());

        return new PageImpl<>(responseDTOs.subList(start, end), pageable, responseDTOs.size());
    }

    @Transactional
    public Page<ReviewResponseDTO> showReviewsByRestaurant(Long restaurantId, Pageable pageable) {
        Page<Review> page = reviewRepository.findByRestaurantforPage(restaurantId, pageable);
        return page.map(ReviewResponseDTO::new);
    }
}
