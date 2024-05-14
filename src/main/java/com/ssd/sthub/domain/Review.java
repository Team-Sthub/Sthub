package com.ssd.sthub.domain;

import com.ssd.sthub.dto.review.ReviewDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

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
    @ElementCollection
    @CollectionTable(name = "review_tags", joinColumns = @JoinColumn(name = "reviewId"))
    @Column(name = "tag")
    private List<Boolean> tags;

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // 중고거래 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "secondhandId")
    private Secondhand secondhand;

    public Review(ReviewDTO request, Secondhand secondhand) {
        this.tags = List.of(
                request.isTag1(),
                request.isTag2(),
                request.isTag3(),
                request.isTag4(),
                request.isTag5(),
                request.isTag6()
        );
        this.secondhand = secondhand;
    }
}
