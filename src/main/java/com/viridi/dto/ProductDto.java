package com.viridi.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDto {

	private Long id;
	
	@NotBlank(message = "Product name is required")
	private String name;
	
	@NotBlank(message = "Product discription is required")
	private String description;
	
	@NotBlank(message = "Product price is required")
	private Double price;
	
	@NotBlank(message = "Discounted price is required")
	private Double discountedPrice;
	
	@NotBlank(message = "Product images link is required")
    private List<String> images;
	
	@NotBlank(message = "Category name is required")
	private CategoryDto category;

	
}
