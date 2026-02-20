package com.practice.mainservice.category.service;

import com.practice.mainservice.category.dto.CategoryDto;
import com.practice.mainservice.category.dto.CategoryMapper;
import com.practice.mainservice.category.dto.NewCategoryDto;
import com.practice.mainservice.category.entity.Category;
import com.practice.mainservice.category.repository.CategoryRepository;
import com.practice.mainservice.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toEntity(newCategoryDto);

        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto update(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));

        category.setName(categoryDto.getName());

        category = categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> findAll(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));

        return categoryMapper.toDto(category);
    }
}
