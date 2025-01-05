package io.study.lecture_application.infrastructures.db.users.repository;

import io.study.lecture_application.infrastructures.db.users.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersJpaRepository extends JpaRepository<UsersEntity,Long> {
}
