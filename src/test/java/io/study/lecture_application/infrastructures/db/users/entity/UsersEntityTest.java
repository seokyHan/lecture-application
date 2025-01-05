package io.study.lecture_application.infrastructures.db.users.entity;

import io.study.lecture_application.domain.users.model.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UsersEntityTest {

    @DisplayName("UsersEntity 객체를 Users Model로 반환한다.")
    @Test
    void UsersEntityConvertToUsersModelTest() {
        //given
        UsersEntity usersEntity = createUserEntityBuilder(1L, "홍길동", LocalDateTime.now(), null);

        //when
        Users users = usersEntity.toUsersDomain();

        //then
        assertEquals(usersEntity.getId(), users.id());
        assertEquals(usersEntity.getName(), users.name());
        assertEquals(usersEntity.getCreatedAt(), users.createdAt());
        assertEquals(usersEntity.getUpdatedAt(), users.updatedAt());
    }


    private UsersEntity createUserEntityBuilder(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return UsersEntity.builder()
                .id(id)
                .name(name)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }



}