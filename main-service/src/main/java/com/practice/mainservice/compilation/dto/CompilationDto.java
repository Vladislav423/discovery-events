package com.practice.mainservice.compilation.dto;

import com.practice.mainservice.event.dto.EventShortDto;
import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    List<EventShortDto> events;
}
