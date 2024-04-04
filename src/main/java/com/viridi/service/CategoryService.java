package com.viridi.service;

import java.util.List;

import com.viridi.dto.CategoryDto;

public interface CategoryService {

	CategoryDto createCategory(CategoryDto categoryDto);
	
	CategoryDto updateCategory(CategoryDto CategoryDto, Long id);
	
	void deleteCategory(Long categoryId);
	
	CategoryDto getCategoryById(Long id);
	
	List<CategoryDto> getAllCategory();
	
	//CategoryDto getCategoryByName(String name);
	
	//void deleteCategoryById(Long id);
	
	//void deleteCategoryByName(String name);
	
	
}
