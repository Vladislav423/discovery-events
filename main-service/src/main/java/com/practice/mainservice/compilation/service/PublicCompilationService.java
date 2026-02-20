package com.practice.mainservice.compilation.service;

import com.practice.mainservice.compilation.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> findAll(Boolean pinned,Integer from, Integer size);

    CompilationDto findById(Long compId);
}
