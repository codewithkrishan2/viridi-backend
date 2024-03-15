package com.viridi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductsInCartDto {

	private Long userId;
	
	private Long productId;
		
}
