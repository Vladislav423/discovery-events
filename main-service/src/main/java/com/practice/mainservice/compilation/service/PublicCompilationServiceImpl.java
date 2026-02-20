package com.practice.mainservice.compilation.service;

import com.practice.mainservice.compilation.dto.CompilationDto;
import com.practice.mainservice.compilation.dto.CompilationMapper;
import com.practice.mainservice.compilation.entity.Compilation;
import com.practice.mainservice.compilation.repository.CompilationRepository;
import com.practice.mainservice.event.entity.Event;
import com.practice.mainservice.exception.CompilationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        Page<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable);
        }

        return compilations.stream()
                .map(compilationMapper::toDto)
                .toList();
    }

    @Override
    public CompilationDto findById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Подборка не найдена"));

        return compilationMapper.toDto(compilation);
    }
}
