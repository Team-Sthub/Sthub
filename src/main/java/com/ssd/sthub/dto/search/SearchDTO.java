package com.ssd.sthub.dto.search;

import jakarta.xml.soap.Detail;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchDTO {
    @Getter
    @AllArgsConstructor
    public static class DetailResponse {
        private Long id; // secondhandId / groupBuyingId 둘 중 하나
        private LocalDateTime createdAt;
        private String imgUrl;
        private String title;
        private String nickname;
        private Long price;
        private String type;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class ListResponse {
        private List<SearchDTO.DetailResponse> searchs;
        private String searchValue;
        private int totalPage;
        private int currentPage;
    }
}
