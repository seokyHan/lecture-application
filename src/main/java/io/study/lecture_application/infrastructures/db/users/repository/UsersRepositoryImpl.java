package io.study.lecture_application.infrastructures.db.users.repository;

import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.domain.users.model.Users;
import io.study.lecture_application.domain.users.repository.UsersRepository;
import io.study.lecture_application.infrastructures.db.users.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.study.lecture_application.common.code.LectureErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {

    private final UsersJpaRepository usersJpaRepository;

    @Override
    public Users getUserById(Long userId) {
        return usersJpaRepository.findById(userId)
                .map(UsersEntity::toUsersDomain)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    @Override
    public List<Users> getUsersByIds(List<Long> userIds) {
        return usersJpaRepository.findAllById(userIds)
                .stream()
                .map(UsersEntity::toUsersDomain)
                .toList();
    }
}
