package com.ssd.sthub.dto.gComment;

import com.ssd.sthub.domain.GComment;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GCommentResponseDto {

    public static class GCommentDto {
        private Long id;
        private String content;
        private String nickname;
    }
}
