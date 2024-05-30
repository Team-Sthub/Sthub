package com.ssd.sthub.dto.groupBuying;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.domain.GImage;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.enumerate.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupBuyingDetailDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PatchRequest {
        private Long groupBuyingId;
        private String title; // 제목
        private Category category; // 카테고리
        private String product; // 상품
        private Long price; // 가격
        private LocalDate deadline; // 마감 기한
        private String chatLink; // 오픈 채팅 링크
        private String meetingPlace; // 직거래 장소
        private String content; // 내용
        private String status; // 상태 : 모집중, 모집완료
        private List<String> deleteImages; // 삭제하고 싶은 이미지들
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PatchResponse {
        private GroupBuying groupBuying;
        private List<GImage> gImages;
        private List<GComment> gComments;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Response {
        private GroupBuying groupBuying;
        private List<GImage> gImages;
        private List<GComment> gComments;
    }
}
