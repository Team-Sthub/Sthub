package com.ssd.sthub.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "purchase")
public class Purchase extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchaseId")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="secondhandId")
    private Secondhand secondhand;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "memberId")
    private Member member;

    @Builder
    public Purchase(Secondhand secondhand, Member member) {
        this.secondhand = secondhand;
        this.member = member;
    }
}
