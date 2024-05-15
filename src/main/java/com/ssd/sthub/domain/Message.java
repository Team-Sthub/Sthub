package com.ssd.sthub.domain;

import com.ssd.sthub.dto.message.MessageDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "message")
public class Message extends BaseTime {

    // 쪽지 식별값
    @Id
    @Column(name="messageId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 내용
    @Column(nullable = false, columnDefinition = "CLOB")
    private String content;

    // 보낸 사람
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "senderId")
    private Member sender;

    // 받는 사람
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "receiverId")
    private Member receiver;

    public Message(MessageDTO.Request request, Member sender, Member receiver) {
        this.content = request.getContent();
        this.sender = sender;
        this.receiver = receiver;
    }
}
