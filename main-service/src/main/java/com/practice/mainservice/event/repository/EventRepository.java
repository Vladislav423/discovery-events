package com.practice.mainservice.event.repository;

import com.practice.mainservice.event.entity.Event;
import com.practice.mainservice.event.entity.EventState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("select e from Event e " +
            "where (:users is null or e.initiator.id in :users)" +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or e.category.id in :categories)" +
            "and (cast(:rangeStart as timestamp) is null or e.eventDate >= :rangeStart)" +
            "and (cast(:rangeEnd as timestamp) is null or e.eventDate <= :rangeEnd)")
    Page<Event> findAdminEvents(@Param("users") List<Long> users,
                                @Param("states") List<EventState> states,
                                @Param("categories") List<Long> categories,
                                @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                Pageable pageable);

    @Query("select e from Event e " +
            "where (e.state = 'PUBLISHED') " +
            "and ((:text is null or lower(e.annotation) like lower(concat('%', cast(:text as string), '%'))) or lower(e.description) like lower(concat('%', cast(:text as string), '%'))) " +
            "and (:categories IS NULL OR e.category.id IN :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (cast(:rangeStart as timestamp ) is null or e.eventDate >= :rangeStart) " +
            "and (cast(:rangeEnd as timestamp ) is null or e.eventDate <= :rangeEnd)")
    Page<Event> findPublicEvents(@Param("text") String text,
                                 @Param("categories") List<Long> categories,
                                 @Param("paid") Boolean paid,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                 Pageable pageable);
}
