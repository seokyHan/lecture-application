package io.study.lecture_application.domain.users.repository;

import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.domain.users.model.Users;
import io.study.lecture_application.infrastructures.db.users.entity.UsersEntity;
import io.study.lecture_application.infrastructures.db.users.repository.UsersJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static io.study.lecture_application.common.code.LectureErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class UsersRepositoryTest {

    @Autowired
    private UsersJpaRepository usersJpaRepository;
    @Autowired
    private UsersRepository usersRepository;

    @AfterEach
    void tearDown() {
        usersJpaRepository.deleteAllInBatch();
    }

    @DisplayName("userId로 조회되지 않는다면 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        //given
        usersJpaRepository.save(new UsersEntity(1L, "test", null, null));

        //when & then
        assertThatThrownBy(() -> usersRepository.getUserById(2L))
                .isInstanceOf(CustomException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());

    }

    @DisplayName("UserEntity에서 userId를 통해 조회한다.")
    @Test
    void getUserByIdTest() {
        //given
        UsersEntity usersEntity = usersJpaRepository.save(new UsersEntity(null,"test", null, null));

        //when
        Users users = usersRepository.getUserById(usersEntity.getId());

        //then
        assertEquals(usersEntity.getId(), users.id());
        assertEquals(usersEntity.getName(), users.name());
        assertNotNull(users.createdAt());
    }

    @DisplayName("findUsersByIds 테스트 성공")
    @Test
    void shouldSuccessfullyFindUsersByIds() {
        // given
        List<UsersEntity> userEntities = usersJpaRepository.saveAll(List.of(
                new UsersEntity(null, "test1", null, null),
                new UsersEntity(null, "test2", null, null)
        ));
        List<Long> userIds = userEntities.stream()
                .map(UsersEntity::getId)
                .toList();

        // when
        List<Users> result = usersRepository.getUsersByIds(userIds);

        // then
        assertThat(result).hasSize(2)
                .extracting("id", "name")
                .containsExactlyInAnyOrder(
                        tuple(result.get(0).id(), "test1"),
                        tuple(result.get(1).id(), "test2")
                );

    }



}