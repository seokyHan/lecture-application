package io.study.lecture_application.infrastructures.db.lecture.entity;


import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import io.study.lecture_application.infrastructures.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecture")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long lecturerId;

    @Builder
    public LectureEntity(Long id, String title, String description, Long lecturerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.title = title;
        this.description = description;
        this.lecturerId = lecturerId;
    }

    public Lecture toLectureDomain() {
        return new Lecture(id, title, description, lecturerId, getCreatedAt(), getUpdatedAt());
    }



}
