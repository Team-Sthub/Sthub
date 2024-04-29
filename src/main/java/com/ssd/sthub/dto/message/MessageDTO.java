package com.ssd.sthub.dto.message;

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
}
