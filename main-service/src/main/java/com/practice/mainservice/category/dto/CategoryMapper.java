package com.practice.mainservice.category.dto;

import com.practice.mainservice.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toEntity(NewCategoryDto newCategoryDto){
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }
    public CategoryDto toDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return  categoryDto;
    }
}
