package com.viridi.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

	private Long id;
	
	//@NotBlank(message = "Category name is required")
	@Size(min = 2, message = "Category name should be atleast 2 characters")
	private String name;
	
	//private String description;
}
