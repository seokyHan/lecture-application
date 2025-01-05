package io.study.lecture_application.domain.users.repository;

import io.study.lecture_application.domain.users.model.Users;

import java.util.List;

public interface UsersRepository {
    Users getUserById(Long userId);

    List<Users> getUsersByIds(List<Long> userIds);
}
