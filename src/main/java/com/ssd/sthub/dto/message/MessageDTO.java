package com.ssd.sthub.dto.message;

import com.ssd.sthub.domain.Message;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class MessageDTO {
    @Getter
    public static class Request {
        private Long receiverId;
        private String content;
    }

    @Getter
    public static class Response {
        private Long senderId;
        private String senderNickname;
        private String senderProfile;
        private Long receiverId;
        private String receiverNickname;
        private String receiverProfile;
        private String content;
        private LocalDateTime createdAt;

        public Response(Message message) {
            this.senderId = message.getSender().getId();
            this.senderNickname = message.getSender().getNickname();
            this.senderProfile = message.getSender().getProfile();
            this.receiverId = message.getReceiver().getId();
            this.receiverNickname = message.getReceiver().getNickname();
            this.receiverProfile = message.getReceiver().getProfile();
            this.content = message.getContent();
            this.createdAt = message.getCreatedAt();
        }
    }
}
