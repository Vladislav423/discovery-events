package com.practice.mainservice.event.service;

import com.practice.mainservice.category.entity.Category;
import com.practice.mainservice.category.repository.CategoryRepository;
import com.practice.mainservice.event.dto.*;
import com.practice.mainservice.event.entity.Event;
import com.practice.mainservice.event.entity.EventState;
import com.practice.mainservice.event.entity.Location;
import com.practice.mainservice.event.repository.EventRepository;
import com.practice.mainservice.event.repository.LocationRepository;
import com.practice.mainservice.exception.*;
import com.practice.mainservice.request.entity.ParticipationRequest;
import com.practice.mainservice.request.repository.ParticipationRequestRepository;
import com.practice.mainservice.request.entity.RequestStatus;
import com.practice.mainservice.request.dto.ParticipationRequestDto;
import com.practice.mainservice.request.dto.RequestMapper;
import com.practice.mainservice.user.entity.User;
import com.practice.mainservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final LocationRepository locationRepository;

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        Pageable pageable = PageRequest.of(from / size, size);

        return eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(eventMapper::toShortDto)
                .toList();
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Дата события не может быть раньше, чем через 2 часа от текущего момента");
        }

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));

        Event event = eventMapper.toEntity(newEventDto);

        event.setCategory(category);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0L);
        event.setViews(0L);

        if (event.getPaid() == null){
            event.setPaid(false);
        }
        if (event.getParticipantLimit() == null){
            event.setParticipantLimit(0);
        }
        if (event.getRequestModeration() == null){
            event.setRequestModeration(true);
        }

        if (event.getLocation() != null){
            Location savedLocation = locationRepository.save(event.getLocation());
            event.setLocation(savedLocation);
        }

        Event savedEvent = eventRepository.save(event);

        return eventMapper.toFullDto(savedEvent);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Событие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new EventNotFoundException("Событие не найдено");
        }

        return eventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Пользователь не является владельцем события");
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Нельзя редактировать опубликованное событие");
        }

        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }

        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
            event.setCategory(category);
        }

        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }

        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Новая дата события слишком близко");
            }
            event.setEventDate(updateEvent.getEventDate());
        }

        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }

        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }

        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }

        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }

        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction() == StateAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            } else if (updateEvent.getStateAction() == StateAction.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            }
        }

        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }

        return eventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Только организатор может просматривать список заявок");
        }

        return participationRequestRepository.findAllByEventId(eventId).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие на найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Только организатор может просматривать список заявок");
        }

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Лимит участников исчерпан");
        }

        List<ParticipationRequest> requests = participationRequestRepository.findAllById(updateRequest.getRequestIds());

        System.out.println("Найдено заявок в БД: " + requests.size());
        System.out.println("ID события из URL: " + eventId);
        if (!requests.isEmpty()) {
            System.out.println("ID события в первой заявке: " + requests.get(0).getEvent().getId());
            System.out.println("Статус первой заявки: " + requests.get(0).getStatus());
        }
        System.out.println("Желаемый статус: " + updateRequest.getStatus());

        List<ParticipationRequestDto> confirmedList = new ArrayList<>();

        List<ParticipationRequestDto> rejectedList = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (!request.getEvent().getId().equals(eventId)) {
                continue;
            }

            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Статус можно менять только у заявок в ожидании");
            }
            if (updateRequest.getStatus() == RequestStatus.CONFIRMED) {
                if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedList.add(requestMapper.toDto(request));
                } else {
                    request.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    confirmedList.add(requestMapper.toDto(request));
                }

            } else if (updateRequest.getStatus() == RequestStatus.REJECTED) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedList.add(requestMapper.toDto(request));
            }
        }
        participationRequestRepository.saveAll(requests);
        eventRepository.save(event);

        return new EventRequestStatusUpdateResult(confirmedList, rejectedList);
    }


}
