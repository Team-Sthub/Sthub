package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.domain.Review;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.review.ReviewRequestDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.PurchaseRepository;
import com.ssd.sthub.repository.ReviewRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final SecondhandRepository secondhandRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;

    // 후기 작성
    public Review createReview(Long purchaseId, ReviewRequestDTO.Request request) throws NullPointerException {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException("해당 구매 내역을 찾을 수 없습니다."));

        Secondhand secondhand = secondhandRepository.findById(purchase.getSecondhand().getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다."));

        Review review = new Review(request, secondhand);

        return reviewRepository.save(review);
    }

    // 매너 농도 계산


    // 후기 조회
    public List<Integer> getTags(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        List<Integer> trueIndexes = reviewRepository.findReviewRepoDTOByMemberId(memberId);
        return trueIndexes;
    }
    // 매너 농도 조회

}
