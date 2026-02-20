package com.practice.mainservice.event.service;

import com.practice.mainservice.category.entity.Category;
import com.practice.mainservice.category.repository.CategoryRepository;
import com.practice.mainservice.event.dto.AdminStateAction;
import com.practice.mainservice.event.dto.EventFullDto;
import com.practice.mainservice.event.dto.EventMapper;
import com.practice.mainservice.event.entity.Event;
import com.practice.mainservice.event.entity.EventState;
import com.practice.mainservice.event.entity.Location;
import com.practice.mainservice.event.repository.EventRepository;
import com.practice.mainservice.event.repository.LocationRepository;
import com.practice.mainservice.exception.ConflictException;
import com.practice.mainservice.exception.EventNotFoundException;
import com.practice.mainservice.exception.ValidationException;
import com.practice.mainservice.event.dto.UpdateEventAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<String> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Дата начала не может быть позже даты окончания");
        }

        if (users != null && users.isEmpty()) users = null;
        if (categories != null && categories.isEmpty()) categories = null;

        List<EventState> stateEnums = null;
        if (states != null && !states.isEmpty()) {
            stateEnums = states.stream()
                    .map(EventState::valueOf)
                    .toList();
        }

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Event> page = eventRepository.findAdminEvents(users, stateEnums, categories, rangeStart, rangeEnd, pageable);

        return page.stream()
                .map(eventMapper::toFullDto)
                .toList();
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие на найдено"));

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new EventNotFoundException("Категория на найдена"));
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации.");
            }
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            Location  location = updateEventAdminRequest.getLocation();
            locationRepository.save(location);
            event.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction() == AdminStateAction.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING) {
                    throw new ConflictException("Событие можно публиковать, только если оно в ожидании публикации");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventAdminRequest.getStateAction() == AdminStateAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Событие уже опубликовано, его нельзя отклонить");
                }
                event.setState(EventState.CANCELED);
            }
        }

        return eventMapper.toFullDto(eventRepository.save(event));
    }
}
