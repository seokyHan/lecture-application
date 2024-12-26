package io.study.lecture_application.domain.lecture.service;

import io.study.lecture_application.common.validator.LectureValidator;
import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import io.study.lecture_application.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureQueryService {
    private final LectureRepository lectureRepository;

    public Lecture getLectureById(Long lectureId) {
        LectureValidator.getLectureByIdValidate(lectureId);
        return lectureRepository.getLectureById(lectureId);
    }

    public LectureSchedule getLectureScheduleById(Long lectureScheduleId, boolean isConcurrency) {
        LectureValidator.getLectureScheduleByIdValidate(lectureScheduleId);
        return lectureRepository.getLectureScheduleById(lectureScheduleId, isConcurrency);
    }

    public List<LectureSchedule> findAvailableLectureSchedules(LocalDate date) {
        LectureValidator.availableLectureSchedulesValidate(date);
        return lectureRepository.findAvailableLectureSchedulesByDateTime(date);
    }

    public LectureEnrollment getLectureEnrollmentById(Long lectureEnrollmentId) {
        LectureValidator.getLectureEnrollmentByIdValidate(lectureEnrollmentId);
        return lectureRepository.getLectureEnrollmentById(lectureEnrollmentId);
    }

    public List<LectureEnrollment> findLectureEnrollments(Long userId) {
        LectureValidator.getLectureEnrollmentsByUserId(userId);
        return lectureRepository.findLectureEnrollmentsByUserId(userId);
    }

    public List<Lecture> findLecturesByIds(List<Long> lectureIds) {
        LectureValidator.getLecturesByIds(lectureIds);
        return lectureRepository.findLecturesByIds(lectureIds);
    }
}
