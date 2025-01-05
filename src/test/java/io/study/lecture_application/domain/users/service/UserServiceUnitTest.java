package io.study.lecture_application.domain.users.service;

import io.study.lecture_application.domain.users.dto.UserInfo;
import io.study.lecture_application.domain.users.model.Users;
import io.study.lecture_application.domain.users.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UsersRepository usersRepository;

    @DisplayName("userId를 통해 유저를 조회한다.")
    @Test
    void getUserUnitTest() {
        // given
        Long userId = 1L;
        Users user = new Users(userId, "test", LocalDateTime.now(), LocalDateTime.now());
        UserInfo.GetUserById userInfo = new UserInfo.GetUserById(userId);
        when(usersRepository.getUserById(userId)).thenReturn(user);

        // when
        Users result = userService.getUser(userInfo);

        // then
        assertEquals(result.id(), user.id());
        assertEquals(result.name(), user.name());
        assertEquals(result.createdAt(), user.createdAt());
        assertEquals(result.updatedAt(), user.updatedAt());
    }

    @DisplayName("usersIds를 통해 유저를 조회한다.")
    @Test
    void getUsersUnitTest() {
        // given
        Long userId = 1L;
        Users user = new Users(userId, "test", LocalDateTime.now(), LocalDateTime.now());
        UserInfo.GetUserByIds userInfo = new UserInfo.GetUserByIds(List.of(userId));
        when(usersRepository.getUsersByIds(userInfo.userIds())).thenReturn(List.of(user));

        // when
        List<Users> result = userService.getUsers(userInfo);

        // then
        assertThat(result).hasSize(1)
                .extracting("id", "name", "createdAt", "updatedAt")
                .containsExactlyInAnyOrder(
                        tuple(result.get(0).id(),
                                result.get(0).name(),
                                result.get(0).createdAt(),
                                result.get(0).updatedAt()
                        )
                );

    }

}