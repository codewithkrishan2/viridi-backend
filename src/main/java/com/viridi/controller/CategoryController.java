package com.viridi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridi.dto.ApiResponse;
import com.viridi.dto.CategoryDto;
import com.viridi.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	
	//Create Category
    @PostMapping("/create")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
    	
		CategoryDto createdCategory = this.categoryService.createCategory(
				categoryDto);
		return new ResponseEntity<CategoryDto>(
				createdCategory, HttpStatus.CREATED);
	}
    
    //Update Category
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid
    		@RequestBody CategoryDto categoryDto,
    		@PathVariable Long categoryId){
    	CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, categoryId);
    	return new ResponseEntity<CategoryDto>(
    			updatedCategory, HttpStatus.OK);
    }
    
    //Delete Category
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId){
    	this.categoryService.deleteCategory(categoryId);
    	return new ResponseEntity<ApiResponse> ( 
    			 new ApiResponse("Successfully deleted", true),
    			 HttpStatus.OK);    	
    }
    
    //Get Category
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId){
    	CategoryDto categoryById = this.categoryService.getCategoryById(categoryId);
    	return new ResponseEntity<CategoryDto>(categoryById, HttpStatus.OK);
    }
    
    //Get All Categories
    @GetMapping("/all-categories")
    public ResponseEntity<?> getAllCategories(){
    	List<CategoryDto> allCategory = this.categoryService.getAllCategory();
    	return new ResponseEntity<List<CategoryDto>>(allCategory, HttpStatus.OK);
    }
}