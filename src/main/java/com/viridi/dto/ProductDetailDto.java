package com.viridi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {
	
	private ProductDto product;
	
	private List<ReviewDto> reviews;
	
	//private List<FaqDto> faqs; // for future
}
