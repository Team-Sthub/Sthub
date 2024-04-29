package com.ssd.sthub.dto.review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewDTO {
    private double rating;
    private boolean tag1;
    private boolean tag2;
    private boolean tag3;
    private boolean tag4;
    private boolean tag5;
    private boolean tag6;
}
