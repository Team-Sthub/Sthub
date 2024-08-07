package com.ssd.sthub.domain;

import com.ssd.sthub.dto.gComment.GCommentRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "gComment")
public class GComment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gCommentId")
    private Long id;

    @Column(nullable = false)
    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "groupBuyingId")
    private GroupBuying groupBuying;

    @Builder
    public GComment(Member member,GroupBuying groupBuying, GCommentRequestDto.request request) {
        this.member = member;
        this.groupBuying = groupBuying;
        this.content = request.getContent();
    }
}