package io.study.lecture_application.domain.users.dto;

import java.util.List;

public class UserInfo {

    public record GetUserById(Long userId) {}

    public record GetUserByIds(List<Long> userIds) {}



}
