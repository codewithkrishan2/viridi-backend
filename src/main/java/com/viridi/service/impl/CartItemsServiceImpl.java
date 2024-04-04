package com.viridi.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.viridi.dto.AddProductsInCartDto;
import com.viridi.dto.CartItemsDto;
import com.viridi.dto.OrdersDto;
import com.viridi.dto.PlaceOrderDto;
import com.viridi.entity.CartItems;
import com.viridi.entity.Coupon;
import com.viridi.entity.Orders;
import com.viridi.entity.Product;
import com.viridi.entity.User;
import com.viridi.enums.OrderStatus;
import com.viridi.exception.CustomValidationException;
import com.viridi.exception.ResourceNotFoundException;
import com.viridi.repo.CartItemsRepo;
import com.viridi.repo.CouponRepo;
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
	
	@Autowired
	private CouponRepo couponRepo;


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
		
		List<CartItemsDto> cartItemsDtos= items.stream().map((item)-> this.modelMapper.map(item, CartItemsDto.class)).collect(Collectors.toList());
		
		
		
		OrdersDto orderDto = modelMapper.map(activeOrder, OrdersDto.class);
		
		orderDto.setId(activeOrder.getId());
		orderDto.setStatus(activeOrder.getStatus());
		orderDto.setAmount(activeOrder.getAmount());
		orderDto.setDiscountedAmount(activeOrder.getDiscountedAmount());
		orderDto.setTotalAmount(activeOrder.getTotalAmount());
		orderDto.setCartItems(cartItemsDtos);
		if (activeOrder.getCoupon() != null) {
			orderDto.setCouponName(activeOrder.getCoupon().getCode());
			
		}
		
		
		
		return orderDto;
	}


	@Override
	public OrdersDto applyCoupon(Long userId, String code) {
		
		//User user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with Id ", " userId", userId));
		
		Orders activeOrder = ordersRepo.findByUserIdAndStatus(userId, OrderStatus.PENDING);

		Coupon coupon = couponRepo.findByCode(code).orElseThrow(()-> new CustomValidationException("Coupon Code Not Found"));
		
		if (couponIsExpired(coupon)) {
			throw new CustomValidationException("Coupon Code Has Expired");
			
		}
		
		double discountAmount = ((coupon.getDiscount() / 100.0 ) * activeOrder.getTotalAmount());
		
		double netAmount = activeOrder.getTotalAmount() - discountAmount;
		
		activeOrder.setAmount(netAmount);
		activeOrder.setDiscountedAmount(discountAmount);
		
		activeOrder.setCoupon(coupon);
		
		ordersRepo.save(activeOrder);
		
		return modelMapper.map(activeOrder, OrdersDto.class);
	}


	private boolean couponIsExpired(Coupon coupon) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expirationDate = coupon.getExpirationDate();
		return expirationDate != null && now.isAfter(expirationDate);
	}


	@Override
	public OrdersDto increaseProductsQuantity(AddProductsInCartDto addProductsInCartDto) {
		
		Orders activeOrder = ordersRepo.findByUserIdAndStatus(addProductsInCartDto.getUserId(), OrderStatus.PENDING);
		
		Optional<Product> optionalProduct = productRepo.findById(addProductsInCartDto.getProductId());
		
		Optional<CartItems> optionalCartItems = cartItemsRepo.findByProductIdAndOrdersIdAndUserId(addProductsInCartDto.getProductId(), activeOrder.getId(), addProductsInCartDto.getUserId());
		
		if( optionalProduct.isPresent()  && optionalCartItems.isPresent()) {
			CartItems cartItems = optionalCartItems.get();
			Product product = optionalProduct.get();
			
			activeOrder.setAmount(activeOrder.getAmount() + product.getPrice());
			activeOrder.setTotalAmount(activeOrder.getTotalAmount() + product.getPrice());
			activeOrder.setDiscountedAmount(activeOrder.getDiscountedAmount() + product.getPrice());
			
			cartItems.setQuantity(cartItems.getQuantity() + 1);
			
			if (activeOrder.getCoupon() != null) {
				double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0 ) * activeOrder.getTotalAmount());
				double netAmount = activeOrder.getTotalAmount() - discountAmount;
				
				activeOrder.setAmount(netAmount);
				activeOrder.setDiscountedAmount(discountAmount);
				
			}
			
			cartItemsRepo.save(cartItems);
			ordersRepo.save(activeOrder);
			
			return modelMapper.map(activeOrder, OrdersDto.class);
		}
		
		return null;
	}


	@Override
	public OrdersDto decreaseProductsQuantity(AddProductsInCartDto addProductsInCartDto) {
		Orders activeOrder = ordersRepo.findByUserIdAndStatus(addProductsInCartDto.getUserId(), OrderStatus.PENDING);
		
		Optional<Product> optionalProduct = productRepo.findById(addProductsInCartDto.getProductId());
		
		Optional<CartItems> optionalCartItems = cartItemsRepo.findByProductIdAndOrdersIdAndUserId(addProductsInCartDto.getProductId(), activeOrder.getId(), addProductsInCartDto.getUserId());
		
		if( optionalProduct.isPresent()  && optionalCartItems.isPresent()) {
			CartItems cartItems = optionalCartItems.get();
			Product product = optionalProduct.get();
			
			activeOrder.setAmount(activeOrder.getAmount() - product.getPrice());
			activeOrder.setTotalAmount(activeOrder.getTotalAmount() - product.getPrice());
			activeOrder.setDiscountedAmount(activeOrder.getDiscountedAmount() - product.getPrice());
			
			cartItems.setQuantity(cartItems.getQuantity() - 1);
			
			if (activeOrder.getCoupon() != null) {
				double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0 ) * activeOrder.getTotalAmount());
				double netAmount = activeOrder.getTotalAmount() - discountAmount;
				
				activeOrder.setAmount(netAmount);
				activeOrder.setDiscountedAmount(discountAmount);
				
			}
			
			cartItemsRepo.save(cartItems);
			ordersRepo.save(activeOrder);
			
			return modelMapper.map(activeOrder, OrdersDto.class);
		}
		
		return null;
	}


	@Override
	public OrdersDto placeOrder(PlaceOrderDto placeOrderDto) {
		
		Orders activeOrder = ordersRepo.findByUserIdAndStatus(placeOrderDto.getUserId(), OrderStatus.PENDING);
		
		Optional<User> optionalUser = userRepo.findById(placeOrderDto.getUserId());
		
		if(optionalUser.isPresent()) {
			activeOrder.setDescription(placeOrderDto.getOrderDescription());
			activeOrder.setAddress(placeOrderDto.getAddress());
			activeOrder.setContactNumber(placeOrderDto.getContactNumber());
			activeOrder.setOrderedDate(LocalDateTime.now());
			activeOrder.setStatus(OrderStatus.PLACED);
			activeOrder.setTrackingId(UUID.randomUUID());
			
			ordersRepo.save(activeOrder);
			
			Orders order = new Orders();
			order.setAddress(null);
			order.setAmount(0.0);
			order.setDiscountedAmount(0.0);
			order.setTotalAmount(0.0);
			order.setDescription(null);
			order.setUser(optionalUser.get());
			order.setStatus(OrderStatus.PENDING);
			ordersRepo.save(order);
			
			return modelMapper.map(activeOrder, OrdersDto.class);
		}
		
		return null;
	}
	
	
	
	
	
}
