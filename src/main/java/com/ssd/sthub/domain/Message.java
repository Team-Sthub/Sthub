package com.ssd.sthub.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Message extends BaseTime {

    // 쪽지 식별값
    @Id
    @Column(name="messageId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    // 내용
    @Column(nullable = false, columnDefinition = "text")
    private String content;

    // 보낸 사람
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "senderId")
//    private Member senderId;
}
