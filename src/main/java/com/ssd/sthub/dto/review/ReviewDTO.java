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
    private int tag1;
    private int tag2;
    private int tag3;
    private int tag4;
    private int tag5;
    private int tag6;
}
