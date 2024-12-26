package io.study.lecture_application.domain.users.service;

import io.study.lecture_application.domain.users.model.Users;
import io.study.lecture_application.domain.users.dto.UserInfo;
import io.study.lecture_application.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;

    public Users getUser(UserInfo.GetUserById usersInfo) {
        return usersRepository.getUserById(usersInfo.userId());
    }

    public List<Users> getUsers(UserInfo.GetUserByIds usersInfo) {
        return usersRepository.getUsersByIds(usersInfo.userIds());
    }
}
