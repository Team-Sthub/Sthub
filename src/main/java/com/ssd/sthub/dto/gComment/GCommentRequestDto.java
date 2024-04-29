package com.ssd.sthub.dto.gComment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GCommentRequestDto {

    @Getter
    public static class request {
        private String content;
    }
}
