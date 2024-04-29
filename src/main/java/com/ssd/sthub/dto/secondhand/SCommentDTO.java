package com.ssd.sthub.dto.secondhand;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.SComment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SCommentDTO {
    @Getter
    public static class Request {
        private Long secondhandId;
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
            this.content = comment.getContent();;
            this.updatedAt = comment.getUpdatedAt();
        }
    }
}
