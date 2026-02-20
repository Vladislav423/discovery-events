package com.practice.mainservice.event.service;

import com.practice.mainservice.event.dto.EventFullDto;
import com.practice.mainservice.event.dto.EventShortDto;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                  Boolean onlyAvailable, String sort,
                                  Integer from, Integer size,
                                  HttpServletRequest request);

    EventFullDto getEventById(Long id, HttpServletRequest request);
}
