package io.study.lecture_application.domain.lecture.repository.model;


import io.study.lecture_application.domain.users.model.Users;

import java.time.LocalDateTime;

public record Lecture(Long id,
                      String title,
                      String description,
                      Long lecturerId,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt
) {

}
