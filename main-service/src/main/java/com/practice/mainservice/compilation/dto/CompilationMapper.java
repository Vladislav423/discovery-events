package com.practice.mainservice.compilation.dto;

import com.practice.mainservice.compilation.entity.Compilation;
import com.practice.mainservice.event.dto.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public CompilationDto toDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        if (compilation.getEvents() != null) {
            compilationDto.setEvents(compilation.getEvents().stream()
                    .map(eventMapper::toShortDto)
                    .toList());
        } else {
            compilationDto.setEvents(new ArrayList<>());
        }

        return compilationDto;
    }
}
