package com.practice.mainservice.event.service;

import com.practice.mainservice.event.dto.EventFullDto;
import com.practice.mainservice.event.dto.EventMapper;
import com.practice.mainservice.event.dto.EventShortDto;
import com.practice.mainservice.event.entity.Event;
import com.practice.mainservice.event.entity.EventState;
import com.practice.mainservice.event.repository.EventRepository;
import com.practice.mainservice.exception.EventNotFoundException;
import com.practice.mainservice.exception.ValidationException;
import com.practice.statsclient.client.StatsClient;
import com.practice.statsdto.dto.EndpointHit;
import com.practice.statsdto.dto.ViewStats;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         Boolean onlyAvailable, String sort,
                                         Integer from, Integer size,
                                         HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Дата начала не может быть позже даты окончания");
        }

        LocalDateTime start = rangeStart != null ? rangeStart : LocalDateTime.now();

        Sort sortObj = Sort.unsorted();
        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                sortObj = Sort.by(Sort.Direction.ASC, "eventDate");
            } else if (sort.equals("VIEWS")) {
                sortObj = Sort.by(Sort.Direction.DESC, "views");
            } else {
                throw new ValidationException("Неизвестный тип сортировки");
            }
        }

        Pageable pageable = PageRequest.of(from / size, size, sortObj);

        Page<Event> page = eventRepository.findPublicEvents(text, categories, paid, start, rangeEnd, pageable);
        List<Event> events = page.getContent();

        if (onlyAvailable != null && onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getParticipantLimit() == 0 || e.getConfirmedRequests() < e.getParticipantLimit())
                    .toList();
        }
        EndpointHit hit = EndpointHit.builder().app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.addHit(hit);

        return events.stream()
                .map(eventMapper::toShortDto)
                .toList();
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));

        if (event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException("Событие не найдено или еще не опубликовано");
        }

        try {
            EndpointHit hit = EndpointHit.builder()
                    .app("main-service")
                    .uri(request.getRequestURI())
                    .ip(request.getRemoteAddr())
                    .timestamp(LocalDateTime.now())
                    .build();
            statsClient.addHit(hit);
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении статистики: " + e.getMessage());
        }

        EventFullDto eventFullDto = eventMapper.toFullDto(event);

        try {
            List<ViewStats> stats = statsClient.getStats(
                    event.getPublishedOn() != null ? event.getPublishedOn() : LocalDateTime.now().minusYears(1),
                    LocalDateTime.now().plusYears(1),
                    List.of(request.getRequestURI()),
                    true
            );
            if (stats != null && !stats.isEmpty()) {
                eventFullDto.setViews(stats.get(0).getHits());
            } else {
                eventFullDto.setViews(0L);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при получении статистики: " + e.getMessage());
            eventFullDto.setViews(0L);
        }

        return eventFullDto;
    }
}
