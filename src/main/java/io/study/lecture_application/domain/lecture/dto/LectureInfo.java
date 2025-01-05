package io.study.lecture_application.domain.lecture.dto;

public class LectureInfo {

    public record CreateEnrollmentLecture(
            Long lectureId,
            Long lectureScheduleId,
            Long userId
    ) { }

}
