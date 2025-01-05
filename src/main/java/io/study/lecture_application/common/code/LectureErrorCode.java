package io.study.lecture_application.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LectureErrorCode implements ErrorCode{
    INVALID_LECTURE_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 강의 ID 입니다."),
    INVALID_LECTURE_SCHEDULE_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 강의 스케줄 ID 입니다."),
    INVALID_LECTURE_ENROLLMENT_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 강의 등록 ID 입니다."),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 ID 입니다."),
    INVALID_USER_IDS(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 ID 목록입니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "유효하지 않은 날짜 입니다."),
    INVALID_LECTURE_IDS(HttpStatus.BAD_REQUEST, "유효하지 않은 강의 목록 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 강의입니다."),
    LECTURE_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "강의 스케줄을 찾을 수 없습니다."),
    LECTURE_ENROLLMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "강의 등록 목록을 찾을 수 없습니다."),
    ENROLLMENT_EXCEED_CAPACITY(HttpStatus.BAD_REQUEST, "정원을 초과했습니다."),
    DUPLICATE_ENROLLMENT(HttpStatus.BAD_REQUEST, "이미 등록된 강의입니다."),
    ;

    private final HttpStatus status;
    private final String message;


    @Override
    public String getCode() {
        return name();
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
