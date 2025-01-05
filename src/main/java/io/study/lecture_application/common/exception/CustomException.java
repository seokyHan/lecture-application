package io.study.lecture_application.common.exception;

import io.study.lecture_application.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private String[] values;

    public CustomException(ErrorCode errorCode, String... values) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.values = values;
    }
}
