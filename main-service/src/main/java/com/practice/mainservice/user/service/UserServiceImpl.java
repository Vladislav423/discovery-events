package com.practice.mainservice.user.service;

import com.practice.mainservice.user.dto.NewUserRequest;
import com.practice.mainservice.user.dto.UserDto;
import com.practice.mainservice.user.dto.UserMapper;
import com.practice.mainservice.user.entity.User;
import com.practice.mainservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable).stream()
                    .map(userMapper::toDto)
                    .toList();
        } else {
            return userRepository.findByIdIn(ids,pageable).stream()
                    .map(userMapper::toDto)
                    .toList();
        }
    }

    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        User user = userMapper.toEntity(newUserRequest);

        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
