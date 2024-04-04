package com.viridi.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viridi.dto.OrdersDto;
import com.viridi.entity.Orders;
import com.viridi.entity.User;
import com.viridi.enums.OrderStatus;
import com.viridi.exception.ResourceNotFoundException;
import com.viridi.repo.OrdersRepo;
import com.viridi.repo.UserRepo;
import com.viridi.service.OrdersService;

@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private OrdersRepo ordersRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	
	@Override
	public List<OrdersDto> getAllPladedOrders() {
		List<Orders> ordersList = ordersRepo.findAllByStatusIn(List.of(OrderStatus.PLACED, OrderStatus.SHIPPED, OrderStatus.DELIVERED));
		
		return ordersList.stream().map(order -> modelMapper.map(order, OrdersDto.class)).collect(Collectors.toList());
	}


	@Override
	public OrdersDto changeStatus(Long orderId, String status) {
		
		Orders orders = ordersRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

		if (Objects.equals(status, "SHIPPED")) {			
			orders.setStatus(OrderStatus.SHIPPED);
		}else if (Objects.equals(status, "DELIVERED")) {
			orders.setStatus(OrderStatus.DELIVERED);
		}else if (Objects.equals(status, "CANCELLED")) {
			orders.setStatus(OrderStatus.CANCELLED);
		}
				
		return modelMapper.map(ordersRepo.save(orders), OrdersDto.class);
	}


	@Override
	public List<OrdersDto> getPlacedOrdersByUserId(Long userId) {
		
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		
		return ordersRepo.findByUserIdAndStatusIn(
				user.getId(), List.of(OrderStatus.PLACED, OrderStatus.SHIPPED, OrderStatus.DELIVERED))
				.stream().map(order -> modelMapper.map(order, OrdersDto.class))
				.collect(Collectors.toList());
	}

	 
}
