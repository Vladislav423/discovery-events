package com.practice.mainservice.request.repository;

import com.practice.mainservice.request.entity.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    Optional<ParticipationRequest> findByRequesterIdAndEventId(Long requesterId,Long eventId);

    List<ParticipationRequest> findAllByEventId(Long eventId);
}
