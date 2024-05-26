package com.ssd.sthub.dto.secondhand;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.SComment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SCommentDTO {
    @Getter
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "중고거래 게시글 식별값이 필요합니다.")
        private Long secondhandId;
        @NotEmpty(message = "댓글 내용을 입력해주세요.")
        private String content;
    }

    @Getter
    public static class Response {
        private Long memberId;
        private String nickname;
        private String content;
        private LocalDateTime updatedAt;

        public Response(SComment comment) {
            this.memberId = comment.getMember().getId();
            this.nickname = comment.getMember().getNickname();
            this.content = comment.getContent();
            this.updatedAt = comment.getUpdatedAt();
        }
    }
}
