package com.viridi.service;

import java.util.List;
import java.util.UUID;

import com.viridi.dto.OrdersDto;

public interface OrdersService {

	List<OrdersDto> getAllPladedOrders();
	
	OrdersDto changeStatus(Long orderId, String status);
	
	List<OrdersDto> getPlacedOrdersByUserId(Long userId);
	
	public OrdersDto searchOrdersByTrackingId(UUID trackingId);

}
