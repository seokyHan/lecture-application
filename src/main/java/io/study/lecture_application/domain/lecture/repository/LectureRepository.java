package io.study.lecture_application.domain.lecture.repository;

import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;

import java.time.LocalDate;
import java.util.List;

public interface LectureRepository {
    Lecture getLectureById(Long lectureId);

    LectureSchedule getLectureScheduleById(Long lectureScheduleId, boolean isConcurrency);

    List<LectureSchedule> findAvailableLectureSchedulesByDateTime(LocalDate date);

    LectureEnrollment getLectureEnrollmentById(Long enrollmentId);

    List<LectureEnrollment> findLectureEnrollmentsByUserId(Long userId);

    List<Lecture> findLecturesByIds(List<Long> lectureIds);

    Long createLectureEnrollment(Long lectureId, Long lectureScheduleId, Long userId);

    void increaseEnrollmentCountByLectureScheduleId(Long lectureScheduleId);
}
