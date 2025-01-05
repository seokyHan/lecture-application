package io.study.lecture_application.domain.lecture.dto;

import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import io.study.lecture_application.domain.users.model.Users;

import java.time.LocalDateTime;

public record LectureResult() {

    public record ContainsLecturer(Long id,
                                   String title,
                                   String description,
                                   Users lecturer,
                                   LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {

        public static ContainsLecturer of(Lecture lecture, Users lecturer){
            return new ContainsLecturer(lecture.id(), lecture.title(), lecture.description(), lecturer, lecture.createdAt(), lecture.updatedAt());
        }
    }
}
