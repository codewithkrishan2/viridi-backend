package com.viridi.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderedProductsResponseDto {

	private List<ProductDto> productDtoList;
	
	private Double totalPrice;
	
	private List<String> images;
	
}
