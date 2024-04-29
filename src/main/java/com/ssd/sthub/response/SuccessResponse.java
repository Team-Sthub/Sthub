package com.ssd.sthub.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<T> {
    private Boolean isSuccess;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /* 요청이 성공한 경우 1 */
    public static <T> SuccessResponse<T> create(T data) {
        return new SuccessResponse<>(true, "요청에 성공하였습니다.", data);
    }

    /* 요청이 성공한 경우 1 */
    public static <T> SuccessResponse<T> create(String message, T data) {
        return new SuccessResponse<>(true, message, data);
    }
}
