package com.viridi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.viridi.dto.AddProductsInCartDto;
import com.viridi.dto.CartItemsDto;
import com.viridi.dto.OrdersDto;
import com.viridi.entity.CartItems;
import com.viridi.entity.Orders;
import com.viridi.entity.Product;
import com.viridi.entity.User;
import com.viridi.enums.OrderStatus;
import com.viridi.exception.ResourceNotFoundException;
import com.viridi.repo.CartItemsRepo;
import com.viridi.repo.OrdersRepo;
import com.viridi.repo.ProductRepo;
import com.viridi.repo.UserRepo;
import com.viridi.service.CartItemsService;

@Service
public class CartItemsServiceImpl implements CartItemsService {

	@Autowired
	private CartItemsRepo cartItemsRepo;
	
	@Autowired
	private OrdersRepo ordersRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	
	@Autowired
	private ProductRepo productRepo;
	
	
	@Autowired
	private ModelMapper modelMapper;


	@Override
	public ResponseEntity<?> addProductsInCart(AddProductsInCartDto addProductsInCartDto) {
		Orders activeOrder = ordersRepo.findByUserIdAndStatus(addProductsInCartDto.getUserId(), OrderStatus.PENDING);
		
		System.out.println(addProductsInCartDto.getUserId());
		
		Optional<CartItems> optionalCartItems = cartItemsRepo.findByProductIdAndOrdersIdAndUserId(addProductsInCartDto.getProductId(), activeOrder.getId(), addProductsInCartDto.getUserId());
		
		if(optionalCartItems.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}else {
			Optional<Product> optionalProduct = productRepo.findById(addProductsInCartDto.getProductId());
			Optional<User> optionalUser = userRepo.findById(addProductsInCartDto.getUserId());
			
			if(optionalProduct.isPresent() && optionalUser.isPresent()) {
				
				CartItems cartItems = new CartItems();
				cartItems.setProduct(optionalProduct.get());
				cartItems.setPrice(optionalProduct.get().getPrice());
				cartItems.setQuantity(1);
				cartItems.setOrders(activeOrder);
				cartItems.setUser(optionalUser.get());
				
				CartItems updatedCart = cartItemsRepo.save(cartItems);
				
				activeOrder.setAmount(activeOrder.getAmount() + cartItems.getPrice());
				activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cartItems.getPrice());
				activeOrder.setDiscountedAmount(activeOrder.getDiscountedAmount() + cartItems.getPrice());
				activeOrder.getCartItems().add(cartItems);
				
				ordersRepo.save(activeOrder);
				
				return ResponseEntity.status(HttpStatus.CREATED).body(cartItems);
				
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product or User not found");
			}
			
		}
	}
	
	
	public OrdersDto getCartByUserId(Long userId) {
		
		User user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with Id ", " userId", userId));
				
		Orders activeOrder = ordersRepo.findByUserIdAndStatus(user.getId(), OrderStatus.PENDING);
				
		List<CartItems> items = activeOrder.getCartItems();
		
		List<CartItemsDto> cartItems = items.stream().map((item)-> this.modelMapper.map(item, CartItemsDto.class)).collect(Collectors.toList());
		
		
		
		OrdersDto orderDto = modelMapper.map(activeOrder, OrdersDto.class);
		
		orderDto.setId(activeOrder.getId());
		orderDto.setStatus(activeOrder.getStatus());
		orderDto.setAmount(activeOrder.getAmount());
		orderDto.setDiscountedAmount(activeOrder.getDiscountedAmount());
		orderDto.setTotalAmount(activeOrder.getTotalAmount());
		orderDto.setCartItems(cartItems);
		
		
		return orderDto;
	}
}
