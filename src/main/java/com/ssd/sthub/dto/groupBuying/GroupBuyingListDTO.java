package com.ssd.sthub.dto.groupBuying;


import com.ssd.sthub.domain.GImage;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.enumerate.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupBuyingListDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ListResponse {
        private GroupBuying groupBuying;
        private List<GImage> gImages;
        private Category category;
        private int totalPage;
        private int currentPage;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MyListResponse {
        private GroupBuying groupBuying;
        private List<GImage> gImages;
        private int totalPage;
        private int currentPage;
    }

}
