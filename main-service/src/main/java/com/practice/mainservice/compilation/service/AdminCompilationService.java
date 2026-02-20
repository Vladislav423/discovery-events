package com.practice.mainservice.compilation.service;

import com.practice.mainservice.compilation.dto.CompilationDto;
import com.practice.mainservice.compilation.dto.NewCompilationDto;
import com.practice.mainservice.compilation.dto.UpdateCompilationRequest;
import com.practice.mainservice.compilation.entity.Compilation;

import java.util.List;

public interface AdminCompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    void deleteById(Long compId);

    CompilationDto updateById(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
