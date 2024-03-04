package com.viridi.service;

import org.springframework.http.ResponseEntity;

import com.viridi.dto.AddProductsInCartDto;
import com.viridi.dto.OrdersDto;

public interface CartItemsService {

	ResponseEntity<?> addProductsInCart(AddProductsInCartDto addProductsInCartDto);
	
	OrdersDto getCartByUserId(Long userId);
}
