package com.ssd.sthub.domain;

import com.ssd.sthub.dto.review.ReviewDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "review")
public class Review {

    // 거래후기 식별값
    @Id
    @Column(name="reviewId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 별점
    @Column(nullable = false)
    private Double rating;

    // 조건 (각 항목은 노션에 정리되어 있음)
    @Column(nullable = false)
    private Boolean tag1;

    @Column(nullable = false)
    private Boolean tag2;

    @Column(nullable = false)
    private Boolean tag3;

    @Column(nullable = false)
    private Boolean tag4;

    @Column(nullable = false)
    private Boolean tag5;

    @Column(nullable = false)
    private Boolean tag6;

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // 중고거래 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "secondhandId")
    private Secondhand secondhand;

    public Review(ReviewDTO request, Secondhand secondhand) {
        this.rating = request.getRating();
        this.tag1 = request.isTag1();
        this.tag2 = request.isTag2();
        this.tag3 = request.isTag3();
        this.tag4 = request.isTag4();
        this.tag5 = request.isTag5();
        this.tag6 = request.isTag6();
        this.secondhand = secondhand;
    }
}
