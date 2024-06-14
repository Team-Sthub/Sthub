package com.ssd.sthub.dto.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssd.sthub.domain.Message;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class MessageDTO {
    @Getter @Setter
    public static class Request {
        private Long receiverId;
        private String content;
    }

    @Getter @Setter
    public static class Response {
        private Long senderId;
        private String senderNickname;
        private String senderProfile;
        private Long receiverId;
        private String receiverNickname;
        private String receiverProfile;
        private String content;
        private LocalDateTime createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

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
