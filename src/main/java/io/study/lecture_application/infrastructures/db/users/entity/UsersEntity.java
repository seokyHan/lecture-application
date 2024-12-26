package io.study.lecture_application.infrastructures.db.users.entity;

import io.study.lecture_application.domain.users.model.Users;
import io.study.lecture_application.infrastructures.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder
    public UsersEntity(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
    }

    public Users toUsersDomain() {
        return new Users(id, name, getCreatedAt(), getUpdatedAt());
    }

}
