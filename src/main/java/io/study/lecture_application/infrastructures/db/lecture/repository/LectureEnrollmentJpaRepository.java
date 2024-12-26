package io.study.lecture_application.infrastructures.db.lecture.repository;

import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureEnrollmentJpaRepository extends JpaRepository<LectureEnrollmentEntity, Long> {

    List<LectureEnrollmentEntity> findAllByUserId(Long userId);
    List<LectureEnrollmentEntity> findAllByLectureScheduleId(Long lectureScheduleId);
    List<LectureEnrollmentEntity> findAllByLectureId(Long lectureId);
}
