package io.study.lecture_application.domain.users.service;

import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.domain.users.dto.UserInfo;
import io.study.lecture_application.domain.users.model.Users;
import io.study.lecture_application.infrastructures.db.users.entity.UsersEntity;
import io.study.lecture_application.infrastructures.db.users.repository.UsersJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.study.lecture_application.common.code.LectureErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UsersJpaRepository usersJpaRepository;

    @AfterEach
    void tearDown() {
        usersJpaRepository.deleteAllInBatch();
    }

    @DisplayName("userId로 조회되지 않는다면 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        //given
        UserInfo.GetUserById userId = new UserInfo.GetUserById(1L);

        //when & then
        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOf(CustomException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }


    @DisplayName("userId를 통해 유저를 조회한다.")
    @Test
    void getUserIntegrationTest() {
        //given
        UsersEntity usersEntity = usersJpaRepository.save(new UsersEntity(null, "test1", null, null));
        UserInfo.GetUserById userInfo = new UserInfo.GetUserById(usersEntity.getId());

        //when
        Users user = userService.getUser(userInfo);

        //then
        assertEquals(usersEntity.getId(), user.id());
        assertEquals(usersEntity.getName(), user.name());
        assertNotNull(user.createdAt());
    }


    @DisplayName("userIds를 통해 유저를 조회한다.")
    @Test
    void getUserIdsIntegrationTest() {
        // given
        List<UsersEntity> userEntities = usersJpaRepository.saveAll(List.of(
                new UsersEntity(null, "test2", null, null),
                new UsersEntity(null, "test3", null, null)
        ));
        List<Long> userIds = userEntities.stream()
                .map(UsersEntity::getId)
                .toList();
        UserInfo.GetUserByIds users = new UserInfo.GetUserByIds(userIds);

        // when
        List<Users> result = userService.getUsers(users);

        // then
        assertThat(result).hasSize(2)
                .extracting("id", "name")
                .containsExactlyInAnyOrder(
                        tuple(result.get(0).id(), "test2"),
                        tuple(result.get(1).id(), "test3")
                );
    }


}