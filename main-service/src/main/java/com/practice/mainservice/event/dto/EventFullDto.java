package com.practice.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.mainservice.category.dto.CategoryDto;
import com.practice.mainservice.event.entity.EventState;
import com.practice.mainservice.event.entity.Location;
import com.practice.mainservice.request.dto.ParticipationRequestDto;
import com.practice.mainservice.user.dto.UserDto;
import com.practice.mainservice.user.dto.UserShortDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventFullDto {
    private Long id;

    private String title;

    private String annotation;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private EventState state;

    private Long confirmedRequests;

    private Long views;

    private CategoryDto category;

    private UserShortDto initiator;

    private Location location;
}
