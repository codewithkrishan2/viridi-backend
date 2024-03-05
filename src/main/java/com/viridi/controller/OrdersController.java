package com.viridi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridi.dto.ApiResponse;
import com.viridi.dto.OrdersDto;
import com.viridi.service.OrdersService;


@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	
	@GetMapping("/all")
	public ResponseEntity<List<OrdersDto>> gettingAllPladedOrders() {
		
		return new ResponseEntity<List<OrdersDto>>(ordersService.getAllPladedOrders(),HttpStatus.OK);
	}
	
	@PutMapping("/update/{orderId}/{status}")
	public ResponseEntity<?> chaangeOrderStatus(@PathVariable Long orderId, @PathVariable String status) {
		
		OrdersDto changeStatus = ordersService.changeStatus(orderId, status);
		
		if (changeStatus == null) {
			return new ResponseEntity<ApiResponse>(new ApiResponse("Something went wrong", false), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<OrdersDto>(changeStatus, HttpStatus.OK);
	}
	
	@GetMapping("/myorders/{userId}")
	public ResponseEntity<List<OrdersDto>> getMethodName(@PathVariable Long userId) {
		return new ResponseEntity<List<OrdersDto>>(ordersService.getPlacedOrdersByUserId(userId),HttpStatus.OK);
	}
	
	
	
}
