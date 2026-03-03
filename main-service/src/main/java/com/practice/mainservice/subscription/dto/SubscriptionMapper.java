package com.practice.mainservice.subscription.dto;

import com.practice.mainservice.subscription.entity.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    public SubscriptionDto toDto(Subscription subscription) {
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(subscription.getId());
        subscriptionDto.setFollower(subscription.getFollower().getId());
        subscriptionDto.setFollowed(subscription.getFollowed().getId());
        subscriptionDto.setCreatedOn(subscription.getCreatedOn());
        return subscriptionDto;
    }
}
