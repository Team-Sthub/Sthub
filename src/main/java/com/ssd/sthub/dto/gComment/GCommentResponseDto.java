package com.ssd.sthub.dto.gComment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GCommentResponseDto {
    private List<GCommentResponseDto.GCommentDto> gCommentListDto;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GCommentDto {
        private Long id;
        private String content;
        private String nickname;
    }
}