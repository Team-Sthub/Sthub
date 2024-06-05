package com.ssd.sthub.dto.review;

import com.ssd.sthub.domain.Review;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewResponseDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ReviewDto {
        private Long id;
        private double rating;
        private int tag1;
        private int tag2;
        private int tag3;
        private int tag4;
        private int tag5;
        private int tag6;

        public ReviewDto(ReviewDto reviewDto) {
            this.id = reviewDto.id;
            this.rating = reviewDto.getRating();
            this.tag1 = reviewDto.getTag1();
            this.tag2 = reviewDto.getTag2();
            this.tag3 = reviewDto.getTag3();
            this.tag4 = reviewDto.getTag4();
            this.tag5 = reviewDto.getTag5();
            this.tag6 = reviewDto.getTag6();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ReviewList {
        private Review review;
    }
}
