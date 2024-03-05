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

import com.viridi.dto.AddProductsInCartDto;
import com.viridi.dto.OrdersDto;
import com.viridi.exception.CustomValidationException;
import com.viridi.service.CartItemsService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
	
	
	@Autowired
	private CartItemsService cartItemsService;
	
	
	// add products to cart
	@PostMapping("/add")
	public ResponseEntity<?> addProductsToCart(@RequestBody AddProductsInCartDto addProductsInCartDto) {
		return cartItemsService.addProductsInCart(addProductsInCartDto);
	}
	
	
	
	// get cart by user id
	@GetMapping("/{userId}")
	public ResponseEntity<?> getCartItemByUserId(@PathVariable Long userId) {
		
		ResponseEntity<?> response = null;
		
		try {
			OrdersDto cartByUserId = cartItemsService.getCartByUserId(userId);
			
			response = new ResponseEntity<OrdersDto>(cartByUserId, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response; 
	}
	
	@PostMapping("/applyCoupon/{userId}/{code}")
	public ResponseEntity<?> applyCoupon( @PathVariable Long userId ,@PathVariable String code) {
		
		try {
			OrdersDto applyCoupon = cartItemsService.applyCoupon(userId, code);
			return ResponseEntity.ok(applyCoupon);
		} catch (CustomValidationException ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
				
	}
	
}
