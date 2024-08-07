package com.ssd.sthub.domain;

import com.ssd.sthub.dto.participation.ParticipationRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "participation")
public class Participation extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participationId")
    private Long id;

    private String content; // 추가사항

    @Column(nullable = false)
    @ColumnDefault("0")
    private int accept; // 수락 여부 0(대기) 1(수락) 2(거절)

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "groupBuyingId")
    private GroupBuying groupBuying;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "memberId")
    private Member member;

    @Builder
    public Participation(Member member, GroupBuying groupBuying, ParticipationRequestDTO.Request request) {
        this.member = member;
        this.groupBuying = groupBuying;
        this.content = request.getContent();
        this.accept = 0;
    }

    public Participation update(ParticipationRequestDTO.PatchRequest request) {
        this.content = request.getContent();
        return this;
    }

    public void accept(ParticipationRequestDTO.AcceptRequest request) {
        this.accept = request.getAccept();
    }
}
