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
	
	private String details;
	
	private Double price;
	
	private Double discountedPrice;
	
    private List<String> images;
	
	private CategoryDto category;

	private int quantity;
	
}
