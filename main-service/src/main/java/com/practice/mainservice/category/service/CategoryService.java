package com.practice.mainservice.category.service;

import com.practice.mainservice.category.dto.CategoryDto;
import com.practice.mainservice.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    void deleteById(Long catId);

    CategoryDto update(Long catId, CategoryDto categoryDto);

    List<CategoryDto> findAll(Integer from, Integer size);

    CategoryDto findById(Long catId);
}
