package com.practice.mainservice.subscription.service;

import com.practice.mainservice.event.dto.EventMapper;
import com.practice.mainservice.event.dto.EventShortDto;
import com.practice.mainservice.event.entity.Event;
import com.practice.mainservice.event.entity.EventState;
import com.practice.mainservice.event.repository.EventRepository;
import com.practice.mainservice.exception.ConflictException;
import com.practice.mainservice.exception.SubscriptionNotFoundException;
import com.practice.mainservice.exception.UserNotFoundException;
import com.practice.mainservice.exception.ValidationException;
import com.practice.mainservice.subscription.dto.SubscriptionDto;
import com.practice.mainservice.subscription.dto.SubscriptionMapper;
import com.practice.mainservice.subscription.entity.Subscription;
import com.practice.mainservice.subscription.repository.SubscriptionRepository;
import com.practice.mainservice.user.dto.UserMapper;
import com.practice.mainservice.user.dto.UserShortDto;
import com.practice.mainservice.user.entity.User;
import com.practice.mainservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    @Override
    public SubscriptionDto createSubscription(Long userId, Long followedId) {
        if (userId.equals(followedId)) {
            throw new ValidationException("Нельзя подписаться на самого себя");
        }
        if (subscriptionRepository.existsByFollowerIdAndFollowedId(userId, followedId)) {
            throw new ConflictException("Подписка уже существует");
        }
        User follower = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));

        User followed = userRepository.findById(followedId).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + followedId + " не найден"));

        Subscription subscription = new Subscription();
        subscription.setFollower(follower);
        subscription.setFollowed(followed);
        subscription.setCreatedOn(LocalDateTime.now());
        subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(subscription);
    }

    @Override
    public void deleteSubscription(Long userId, Long followedId) {
        Subscription subscription = subscriptionRepository.findByFollowerIdAndFollowedId(userId, followedId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Подписка не найдена"));

        subscriptionRepository.delete(subscription);
    }

    @Override
    public List<UserShortDto> getFollowedUsers(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Subscription> subscriptions = subscriptionRepository.findAllByFollowerId(userId, pageable);

        return subscriptions.stream()
                .map(Subscription::getFollowed)
                .map(userMapper::toUserShortDto)
                .toList();
    }

    @Override
    public List<UserShortDto> getFollowersUsers(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Subscription> subscriptions = subscriptionRepository.findAllByFollowedId(userId, pageable);

        return subscriptions.stream()
                .map(Subscription::getFollower)
                .map(userMapper::toUserShortDto)
                .toList();
    }

    @Override
    public List<EventShortDto> getEventsFromFollowed(Long userId, Integer from, Integer size, String sort) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));

        Sort sorting = Sort.unsorted();
        if ("EVENT_DATE".equalsIgnoreCase(sort)) {
            sorting = Sort.by(Sort.Direction.ASC, "eventDate");
        } else if ("VIEWS".equalsIgnoreCase(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "views");
        }
        Pageable pageable = PageRequest.of(from / size, size, sorting);

        Page<Event> events = eventRepository.findEventsFromSubscriptions(userId, EventState.PUBLISHED, pageable);

        return events.stream()
                .map(eventMapper::toShortDto)
                .toList();
    }
}
