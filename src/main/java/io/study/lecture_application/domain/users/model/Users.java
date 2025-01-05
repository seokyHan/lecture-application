package io.study.lecture_application.domain.users.model;

import java.time.LocalDateTime;

public record Users(Long id,
                    String name,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt
) {
}
