package com.viridi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viridi.dto.OrderedProductsResponseDto;
import com.viridi.dto.ProductDto;
import com.viridi.dto.ReviewDto;
import com.viridi.entity.CartItems;
import com.viridi.entity.Orders;
import com.viridi.entity.Product;
import com.viridi.entity.Review;
import com.viridi.entity.User;
import com.viridi.repo.OrdersRepo;
import com.viridi.repo.ProductRepo;
import com.viridi.repo.ReviewRepo;
import com.viridi.repo.UserRepo;
import com.viridi.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private OrdersRepo ordersRepo;
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ReviewRepo reviewRepo;

	@Override
	public OrderedProductsResponseDto getOrderedProductsByOrderId(Long orderId) {
		Optional<Orders> optionalOrder = ordersRepo.findById(orderId);
		
		OrderedProductsResponseDto orderedProductsResponseDto = new OrderedProductsResponseDto();
		
		if (optionalOrder.isPresent()) {
			
			orderedProductsResponseDto.setTotalPrice(optionalOrder.get().getAmount());
			
			List<ProductDto> productDtoList = new ArrayList<>();
			
			for(CartItems cartItems : optionalOrder.get().getCartItems()) {
				
				ProductDto productDto = new ProductDto();
				
				productDto.setId(cartItems.getProduct().getId());
				productDto.setName(cartItems.getProduct().getName());
				productDto.setPrice(cartItems.getProduct().getPrice());
				productDto.setQuantity(cartItems.getQuantity());
				productDto.setImages(cartItems.getProduct().getImages());
				
				productDtoList.add(productDto);
				
			}
			
			orderedProductsResponseDto.setProductDtoList(productDtoList);
		}
			
		return orderedProductsResponseDto;
	}

	@Override
	public ReviewDto addOneReview(ReviewDto reviewDto) {
		Optional<Product> optionalProduct = productRepo.findById(reviewDto.getProductId());
		Optional<User> optionalUser = userRepo.findById(reviewDto.getUserId());
		
		if (optionalProduct.isPresent() && optionalUser.isPresent()) {
			Review review = new Review();
			review.setDescription(reviewDto.getDescription());
			review.setRating(reviewDto.getRating());
			review.setProduct(optionalProduct.get());
			review.setUser(optionalUser.get());
			review.setImages(reviewDto.getImages());
			
			Review savedReview = reviewRepo.save(review);
			
			return modelMapper.map(savedReview, ReviewDto.class);
		}
		
		return null;
	}
}
