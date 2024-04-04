package com.viridi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsDto {

	private Long id;
	
	private Double price;
	
	private Integer quantity;
	
	private Long orderId;

	private Long productId;	
	
	private String productName;
	
	private String productImages;
	
	private Long userId;
	
}
