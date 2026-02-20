package com.practice.mainservice.compilation.controller;

import com.practice.mainservice.compilation.dto.CompilationDto;
import com.practice.mainservice.compilation.dto.NewCompilationDto;
import com.practice.mainservice.compilation.dto.UpdateCompilationRequest;
import com.practice.mainservice.compilation.service.AdminCompilationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return adminCompilationService.create(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long compId) {
        adminCompilationService.deleteById(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateById(@PathVariable Long compId,
                                @Valid  @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return adminCompilationService.updateById(compId,updateCompilationRequest);
    }
}
