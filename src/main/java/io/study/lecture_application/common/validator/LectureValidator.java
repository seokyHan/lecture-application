package io.study.lecture_application.common.validator;

import io.study.lecture_application.common.exception.CustomException;
import java.time.LocalDate;
import java.util.List;

import static io.study.lecture_application.common.code.LectureErrorCode.*;


public class LectureValidator {

    public static void createLectureEnrollmentValidate (Long lectureId, Long lectureScheduleId, Long userId) {
        if (lectureId == null || lectureId <= 0) {
            throw new CustomException(INVALID_LECTURE_ID);
        }

        if (lectureScheduleId == null || lectureScheduleId <= 0) {
            throw new CustomException(INVALID_LECTURE_SCHEDULE_ID);
        }

        if (userId == null || userId <= 0) {
            throw new CustomException(INVALID_USER_ID);
        }

    }

    public static void getLectureByIdValidate(Long lectureId) {
        if (lectureId == null || lectureId <= 0) {
            throw new CustomException(INVALID_LECTURE_ID);
        }
    }

    public static void getLectureScheduleByIdValidate(Long lectureScheduleId) {
        if (lectureScheduleId == null || lectureScheduleId <= 0) {
            throw new CustomException(INVALID_LECTURE_ID);
        }
    }

    public static void availableLectureSchedulesValidate(LocalDate date) {
        if (date == null || date.isBefore(LocalDate.now())) {
            throw new CustomException(INVALID_DATE);
        }
    }

    public static void getLectureEnrollmentByIdValidate(Long lectureEnrollmentId) {
        if (lectureEnrollmentId == null || lectureEnrollmentId <= 0) {
            throw new CustomException(INVALID_LECTURE_ENROLLMENT_ID);
        }
    }

    public static void getLectureEnrollmentsByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new CustomException(INVALID_USER_ID);
        }
    }

    public static void getLecturesByIds(List<Long> lectureIds) {
        if (lectureIds == null || lectureIds.isEmpty() || lectureIds.stream()
                .anyMatch(id -> id == null || id <= 0)) {
            throw new CustomException(INVALID_LECTURE_IDS);
        }
    }

}
