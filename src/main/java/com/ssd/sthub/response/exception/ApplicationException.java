package com.ssd.sthub.response.exception;

import com.ssd.sthub.response.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    protected ApplicationException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() { return errorCode;}

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
