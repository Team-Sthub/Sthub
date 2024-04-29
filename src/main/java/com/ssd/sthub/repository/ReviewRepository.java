package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review save(Review review);
    //농도 계산

}
