package com.viridi.service;

import org.springframework.http.ResponseEntity;

import com.viridi.dto.AddProductsInCartDto;
import com.viridi.dto.OrdersDto;
import com.viridi.dto.PlaceOrderDto;

public interface CartItemsService {

	ResponseEntity<?> addProductsInCart(AddProductsInCartDto addProductsInCartDto);
	
	OrdersDto getCartByUserId(Long userId);
	
	OrdersDto applyCoupon(Long userId, String code);
	
	OrdersDto increaseProductsQuantity(AddProductsInCartDto addProductsInCartDto);
	
	OrdersDto decreaseProductsQuantity(AddProductsInCartDto addProductsInCartDto);
	
	OrdersDto placeOrder(PlaceOrderDto placeOrderDto);
}
