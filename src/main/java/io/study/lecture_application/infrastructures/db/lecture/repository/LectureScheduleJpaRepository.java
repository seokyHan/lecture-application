package io.study.lecture_application.infrastructures.db.lecture.repository;

import io.study.lecture_application.infrastructures.db.lecture.entity.LectureScheduleEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureScheduleJpaRepository extends JpaRepository<LectureScheduleEntity, Long> {

    @Query("select ls from LectureScheduleEntity ls " +
            "where ls.startAt >= :startDateTime " +
            "and ls.startAt < :endDateTime " +
            "and ls.enrolledCount < ls.capacity ")
    List<LectureScheduleEntity> findAvailableByStartAtBetween(
           LocalDateTime startDateTime,
           LocalDateTime endDateTime
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LectureScheduleEntity> findWithLockById(Long lectureScheduleId);
}
