package com.ssd.sthub.dto.gComment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GCommentRequestDto {

    @Getter
    public static class request {
        private Long groupBuyingId;
        private String content;
    }
}
