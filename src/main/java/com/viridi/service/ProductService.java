package com.viridi.service;

import java.util.List;

import com.viridi.dto.ProductDto;

public interface ProductService {

	ProductDto addOneProduct(ProductDto productDto, Long categoryId);
	
	ProductDto updateOneProduct(ProductDto productDto, Long id);
	
	void deleteOneProduct(Long productId);
	
	ProductDto getOneProductById(Long id);
	
	List<ProductDto> getAllProduct();
	
	
}
