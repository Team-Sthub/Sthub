package com.ssd.sthub.service;

import com.ssd.sthub.domain.Review;
import com.ssd.sthub.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    // 후기 작성
    public Review createReview(Review review) throws NullPointerException {
        if (review == null) {
            throw new NullPointerException("리뷰가 존재하지 않습니다.");
        }
        return reviewRepository.save(review);
    }

    // 매너 농도 계산


}
