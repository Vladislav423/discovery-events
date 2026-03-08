package com.practice.mainservice.subscription.repository;

import com.practice.mainservice.subscription.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<Subscription> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    @Query("select s from Subscription s join fetch s.followed where s.follower.id = :followerId ")
    Page<Subscription> findAllByFollowerId(@Param("followerId") Long followerId, Pageable pageable);

    @Query("select s from Subscription s join fetch s.follower where s.followed.id = :followedId")
    Page<Subscription> findAllByFollowedId(@Param("followedId")  Long followedId, Pageable pageable);
}
