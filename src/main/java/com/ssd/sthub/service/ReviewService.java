package com.ssd.sthub.service;

import com.ssd.sthub.domain.Review;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.review.ReviewDTO;
import com.ssd.sthub.repository.ReviewRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final SecondhandRepository secondhandRepository;

    // 후기 작성
    public Review createReview(Long secondhandId, ReviewDTO request) throws NullPointerException {
        Secondhand secondhand = secondhandRepository.findById(secondhandId).orElseThrow(() -> new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다."));
        Review review = new Review(request, secondhand);
        return reviewRepository.save(review);
    }

    // 매너 농도 계산


}
