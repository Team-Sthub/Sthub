package com.ssd.sthub.domain;

import com.ssd.sthub.dto.review.ReviewRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
public class Review extends BaseTime {

    // 거래후기 식별값
    @Id
    @Column(name="reviewId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 별점
    @Column(nullable = false)
    private Double rating;

    // 조건 (각 항목은 노션에 정리되어 있음) 0,1로 구분 0-> 선택 안된 tag, 1-> 선택된 tag
    @NotEmpty
    @Convert(converter = StringListConverter.class)
    @Column(name = "tag")
    private List<Integer> tags;

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // 중고거래 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "secondhandId")
    private Secondhand secondhand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiverId")
    private Member receiver;

    public Review(ReviewRequestDTO.Request request, Secondhand secondhand) {
        this.rating = request.getRating();
        this.tags = List.of(
                request.getTag1(),
                request.getTag2(),
                request.getTag3(),
                request.getTag4(),
                request.getTag5(),
                request.getTag6()
        );
        this.secondhand = secondhand;
        this.receiver = secondhand.getMember();
    }
}