package com.viridi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDto {

	private Long userId;
	
	private String address;
	
	private Long contactNumber;
	
	private String orderDescription;
	
}
