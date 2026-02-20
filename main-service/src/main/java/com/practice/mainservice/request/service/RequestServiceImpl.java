package com.practice.mainservice.request.service;

import com.practice.mainservice.event.entity.Event;
import com.practice.mainservice.event.entity.EventState;
import com.practice.mainservice.event.repository.EventRepository;
import com.practice.mainservice.exception.ConflictException;
import com.practice.mainservice.exception.EventNotFoundException;
import com.practice.mainservice.exception.UserNotFoundException;
import com.practice.mainservice.request.dto.ParticipationRequestDto;
import com.practice.mainservice.request.dto.RequestMapper;
import com.practice.mainservice.request.entity.ParticipationRequest;
import com.practice.mainservice.request.repository.ParticipationRequestRepository;
import com.practice.mainservice.request.entity.RequestStatus;
import com.practice.mainservice.user.entity.User;
import com.practice.mainservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));

        if (requestRepository.findByRequesterIdAndEventId(userId,eventId).isPresent()){
            throw new ConflictException("Нельзя повторно отправить заявку");
        }

        if (event.getInitiator().getId().equals(userId)){
            throw new ConflictException("Инициатор не может быть участником своего события");
        }

        if (event.getState() != EventState.PUBLISHED){
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()){
            throw new ConflictException("Лимит участников исчерпан");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setRequester(user);
        request.setEvent(event);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0){
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EventNotFoundException("Заявка не найдена"));

        if (!request.getRequester().getId().equals(userId)) {
            throw new ConflictException("Можно отменить только свою заявку");
        }

        request.setStatus(RequestStatus.CANCELED);

        return requestMapper.toDto(requestRepository.save(request));
    }
}
