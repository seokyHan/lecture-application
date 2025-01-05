package io.study.lecture_application.domain.lecture.repository.model;


import java.time.LocalDateTime;

public record LectureSchedule(Long id,
                              Long lectureId,
                              int capacity,
                              int enrolledCount,
                              LocalDateTime startAt,
                              LocalDateTime endAt,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt

) {

}
