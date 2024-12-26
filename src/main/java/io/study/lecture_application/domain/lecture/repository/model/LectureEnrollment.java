package io.study.lecture_application.domain.lecture.repository.model;

import java.time.LocalDateTime;

public record LectureEnrollment(Long id,
                                Long lectureId,
                                Long lectureScheduleId,
                                Long userId,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt
) {
}
