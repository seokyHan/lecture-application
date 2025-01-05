package io.study.lecture_application.infrastructures.db.lecture.entity;

import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import io.study.lecture_application.infrastructures.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecture_schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureScheduleEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lectureId;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int enrolledCount;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Builder
    public LectureScheduleEntity(Long id, Long lectureId, int capacity, int enrolledCount, LocalDateTime startAt, LocalDateTime endAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.lectureId = lectureId;
        this.capacity = capacity;
        this.enrolledCount = enrolledCount;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public LectureSchedule toLectureScheduleDomain() {
        return new LectureSchedule(
                id,
                lectureId,
                capacity,
                enrolledCount,
                startAt,
                endAt,
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    public void increaseEnrollmentCount() {
        enrolledCount++;
    }

}
