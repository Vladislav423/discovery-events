package com.practice.mainservice.compilation.service;

import com.practice.mainservice.compilation.dto.CompilationDto;
import com.practice.mainservice.compilation.dto.CompilationMapper;
import com.practice.mainservice.compilation.dto.NewCompilationDto;
import com.practice.mainservice.compilation.dto.UpdateCompilationRequest;
import com.practice.mainservice.compilation.entity.Compilation;
import com.practice.mainservice.compilation.repository.CompilationRepository;
import com.practice.mainservice.event.entity.Event;
import com.practice.mainservice.event.repository.EventRepository;
import com.practice.mainservice.exception.CompilationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();

        compilation.setTitle(newCompilationDto.getTitle());

        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        } else {
            compilation.setPinned(false);
        }

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            compilation.setEvents(events);
        } else {
            compilation.setEvents(new ArrayList<>());
        }

        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteById(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new CompilationNotFoundException("Подборка не найдена");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateById(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Подборка не найдена"));

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }

        return compilationMapper.toDto(compilationRepository.save(compilation));
    }
}
