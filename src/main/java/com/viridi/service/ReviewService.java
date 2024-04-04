package com.viridi.service;

import com.viridi.dto.OrderedProductsResponseDto;
import com.viridi.dto.ReviewDto;

public interface ReviewService {

	OrderedProductsResponseDto getOrderedProductsByOrderId(Long orderId);
	
	ReviewDto addOneReview(ReviewDto reviewDto);
}
