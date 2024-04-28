package com.ssd.sthub.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "secondhand")
public class SecondHand extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "secondhandId")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private Long price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transaction type;

    private String place;

    @Column(nullable = false)
    private String content;

    private String trackingNum;

    @ColumnDefault("'거래가능'")
    @Column(nullable = false)
    private String status; // 거래가능, 예약중, 거래완료

    @Lob
    private String imageUrl;
}
