package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review save(Review review);
    //농도 계산

    // 거래 후기 내역 조회(키워드 조회)
    List<Integer> findReviewRepoDTOByMemberId(Long memberId);
}
