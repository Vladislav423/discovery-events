package com.practice.mainservice.user.service;

import com.practice.mainservice.user.dto.NewUserRequest;
import com.practice.mainservice.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll(List<Long> ids, Integer from, Integer size);

    UserDto create(NewUserRequest newUserRequest);

    void deleteById(Long userId);
}
