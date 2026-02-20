package com.practice.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.mainservice.category.dto.CategoryDto;
import com.practice.mainservice.user.dto.UserShortDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventShortDto {
    private Long id;

    private String title;

    private String annotation;

    private Boolean paid;

    private Long confirmedRequests;

    private Long views;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private CategoryDto category;

    private UserShortDto initiator;

}
