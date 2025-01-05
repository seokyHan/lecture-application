package io.study.lecture_application.infrastructures.db.lecture.entity;

import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.infrastructures.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "lecture_enrollment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lectureId", "userId"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureEnrollmentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lectureId;

    @Column(nullable = false)
    private Long lectureScheduleId;

    @Column(nullable = false)
    private Long userId;

    @Builder
    public LectureEnrollmentEntity(Long id, Long lectureId, Long lectureScheduleId, Long userId,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.lectureId = lectureId;
        this.lectureScheduleId = lectureScheduleId;
        this.userId = userId;
    }

    public LectureEnrollmentEntity(Long lectureId, Long lectureScheduleId, Long userId) {
        this(null, lectureId, lectureScheduleId, userId, null, null);
    }

    public LectureEnrollment toLectureEnrollmentDomain() {
        return new LectureEnrollment(
                id,
                lectureId,
                lectureScheduleId,
                userId,
                getCreatedAt(),
                getUpdatedAt());
    }
}
