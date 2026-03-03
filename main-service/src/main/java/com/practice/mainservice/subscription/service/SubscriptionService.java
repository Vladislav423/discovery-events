package com.practice.mainservice.subscription.service;

import com.practice.mainservice.event.dto.EventShortDto;
import com.practice.mainservice.subscription.dto.SubscriptionDto;
import com.practice.mainservice.user.dto.UserShortDto;

import java.util.List;

public interface SubscriptionService {
     SubscriptionDto createSubscription(Long userId, Long followedId);

    void deleteSubscription(Long userId, Long followedId);

    List<UserShortDto> getFollowedUsers(Long userId, Integer from, Integer size);

    List<UserShortDto> getFollowersUsers(Long userId, Integer from, Integer size);

    List<EventShortDto> getEventsFromFollowed(Long userId, Integer from, Integer size, String sort);
}
