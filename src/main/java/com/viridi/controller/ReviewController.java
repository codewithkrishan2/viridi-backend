package com.viridi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridi.dto.OrderedProductsResponseDto;
import com.viridi.dto.ReviewDto;
import com.viridi.service.ReviewService;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	
	@GetMapping("/ordered-products/{orderId}")
	public ResponseEntity<OrderedProductsResponseDto> getOrderedProductDto(@PathVariable Long orderId) {
	
		return ResponseEntity.ok(reviewService.getOrderedProductsByOrderId(orderId));
		
	}
	
	@PostMapping("/ordered-products/{orderId}")
	public ResponseEntity<ReviewDto> addOneReview(@RequestBody ReviewDto reviewDto) {
		return new ResponseEntity<ReviewDto>(reviewService.addOneReview(reviewDto), HttpStatus.CREATED);
		
	}
}
