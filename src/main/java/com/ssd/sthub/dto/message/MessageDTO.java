package com.ssd.sthub.dto.message;

import com.ssd.sthub.domain.Message;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private Long receiverId;
        private String content;

        public Response(Message message) {
            this.senderId = message.getSender().getId();
            this.receiverId = message.getReceiver().getId();
            this.content = message.getContent();
        }
    }
}
