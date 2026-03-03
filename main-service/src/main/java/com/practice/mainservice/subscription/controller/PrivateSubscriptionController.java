package com.practice.mainservice.subscription.controller;

import com.practice.mainservice.event.dto.EventShortDto;
import com.practice.mainservice.subscription.dto.SubscriptionDto;
import com.practice.mainservice.subscription.service.SubscriptionService;
import com.practice.mainservice.user.dto.UserShortDto;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class PrivateSubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscriptions/{followedId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto createSubscription(@PathVariable Long userId,
                                              @PathVariable Long followedId) {
        return subscriptionService.createSubscription(userId, followedId);
    }

    @DeleteMapping("/subscriptions/{followedId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable Long userId,
                                   @PathVariable Long followedId) {
        subscriptionService.deleteSubscription(userId, followedId);
    }

    @GetMapping("/subscriptions")
    public List<UserShortDto> getFollowedUsers(@PathVariable Long userId,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size) {
        return subscriptionService.getFollowedUsers(userId, from, size);
    }

    @GetMapping("/followers")
    public List<UserShortDto> getFollowersUsers(@PathVariable Long userId,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return subscriptionService.getFollowersUsers(userId, from, size);
    }

    @GetMapping("/feed")
    public List<EventShortDto> getEventsFromFollowed(@PathVariable Long userId,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(defaultValue = "10") Integer size,
                                                     @RequestParam(required = false) String sort) {
        return subscriptionService.getEventsFromFollowed(userId, from, size, sort);
    }

}
