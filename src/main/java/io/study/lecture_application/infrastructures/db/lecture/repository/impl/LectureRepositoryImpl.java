package io.study.lecture_application.infrastructures.db.lecture.repository.impl;


import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import io.study.lecture_application.domain.lecture.repository.LectureRepository;
import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEnrollmentEntity;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEntity;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureScheduleEntity;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureEnrollmentJpaRepository;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureJpaRepository;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.study.lecture_application.common.code.LectureErrorCode.*;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;
    private final LectureScheduleJpaRepository lectureScheduleJpaRepository;
    private final LectureEnrollmentJpaRepository lectureEnrollmentJpaRepository;


    @Override
    public Lecture getLectureById(Long lectureId) {
        return lectureJpaRepository.findById(lectureId)
                .map(LectureEntity::toLectureDomain)
                .orElseThrow(() -> new CustomException(LECTURE_NOT_FOUND));
    }

    @Override
    public LectureSchedule getLectureScheduleById(Long lectureScheduleId, boolean isConcurrency) {
        Optional<LectureScheduleEntity> lectureScheduleEntity = isConcurrency
                ? lectureScheduleJpaRepository.findWithLockById(lectureScheduleId)
                : lectureScheduleJpaRepository.findById(lectureScheduleId);

        return lectureScheduleEntity
                .map(LectureScheduleEntity::toLectureScheduleDomain)
                .orElseThrow(() -> new CustomException(LECTURE_SCHEDULE_NOT_FOUND));
    }

    @Override
    public List<LectureSchedule> findAvailableLectureSchedulesByDateTime(LocalDate date) {
        return lectureScheduleJpaRepository.findAvailableByStartAtBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay())
            .stream()
            .map(LectureScheduleEntity::toLectureScheduleDomain)
            .toList();
    }

    @Override
    public LectureEnrollment getLectureEnrollmentById(Long enrollmentId) {
        return lectureEnrollmentJpaRepository.findById(enrollmentId)
                .map(LectureEnrollmentEntity::toLectureEnrollmentDomain)
                .orElseThrow(() -> new CustomException(LECTURE_ENROLLMENT_NOT_FOUND));
    }

    @Override
    public List<LectureEnrollment> findLectureEnrollmentsByUserId(Long userId) {
        return lectureEnrollmentJpaRepository.findAllByUserId(userId)
                .stream()
                .map(LectureEnrollmentEntity::toLectureEnrollmentDomain)
                .toList();
    }

    @Override
    public List<Lecture> findLecturesByIds(List<Long> lectureIds) {
        return lectureJpaRepository.findAllById(lectureIds)
                .stream()
                .map(LectureEntity::toLectureDomain)
                .toList();
    }

    @Override
    public Long createLectureEnrollment(Long lectureId, Long lectureScheduleId, Long userId) {
        return lectureEnrollmentJpaRepository.save(new LectureEnrollmentEntity(lectureId, lectureScheduleId, userId)).getId();
    }

    @Override
    public void increaseEnrollmentCountByLectureScheduleId(Long lectureScheduleId) {
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository
                .findById(lectureScheduleId)
                .orElseThrow(() -> new CustomException(LECTURE_SCHEDULE_NOT_FOUND));

        lectureScheduleEntity.increaseEnrollmentCount();
        lectureScheduleJpaRepository.save(lectureScheduleEntity);
    }
}
