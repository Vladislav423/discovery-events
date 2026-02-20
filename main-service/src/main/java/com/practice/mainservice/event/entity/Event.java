package com.practice.mainservice.event.entity;

import com.practice.mainservice.category.entity.Category;
import com.practice.mainservice.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String annotation;

    @Column(length = 7000, nullable = false)
    private String description;

    @Column(name = "event_date",nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "created_on",nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "published_on",nullable = false)
    private LocalDateTime publishedOn;

    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit",nullable = false)
    private Integer participantLimit;

    @Column(name = "request_moderation",nullable = false)
    private Boolean requestModeration;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    private Long views;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}
