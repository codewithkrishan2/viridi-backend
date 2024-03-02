package com.viridi.dto;

import java.util.List;

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
	
	private String name;
	
	private String description;
	
	private Double price;
	
	private Double discountedPrice;
	
    private List<String> images;
	
	private CategoryDto category;

	
}
