package com.practice.mainservice.subscription.repository;

import com.practice.mainservice.subscription.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<Subscription> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Page<Subscription> findAllByFollowerId(Long followerId, Pageable pageable);

    Page<Subscription> findAllByFollowedId(Long followedId, Pageable pageable);
}
