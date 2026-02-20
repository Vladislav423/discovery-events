package com.practice.mainservice.event.dto;

import com.practice.mainservice.category.entity.Category;
import com.practice.mainservice.category.dto.CategoryDto;
import com.practice.mainservice.event.entity.Event;
import com.practice.mainservice.user.entity.User;
import com.practice.mainservice.user.dto.UserShortDto;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(NewEventDto newEventDto) {
        Event event = new Event();
        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setPaid(newEventDto.getPaid() != null ? newEventDto.getPaid() : false);
        event.setParticipantLimit(newEventDto.getParticipantLimit() != null ? newEventDto.getParticipantLimit() : 0);
        event.setRequestModeration(newEventDto.getRequestModeration() != null ? newEventDto.getRequestModeration() : true);
        event.setLocation(newEventDto.getLocation());
        return event;
    }

    public EventFullDto toFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setLocation(event.getLocation());

        eventFullDto.setCategory(toCategoryDto(event.getCategory()));

        eventFullDto.setInitiator(toUserShortDto(event.getInitiator()));

        eventFullDto.setViews(event.getViews() != null ? event.getViews() : 0L);

        eventFullDto.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0);

        eventFullDto.setParticipantLimit(event.getParticipantLimit() != null ? event.getParticipantLimit() : 0);

        return eventFullDto;
    }

    public EventShortDto toShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setPaid(event.getPaid());

        eventShortDto.setViews(event.getViews() != null ? event.getViews() : 0L);
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0L);

        eventShortDto.setCategory(toCategoryDto(event.getCategory()));
        eventShortDto.setInitiator(toUserShortDto(event.getInitiator()));

        return eventShortDto;
    }

    private CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    private UserShortDto toUserShortDto(User user) {
        if (user == null) {
            return null;
        }
        UserShortDto dto = new UserShortDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }
}
