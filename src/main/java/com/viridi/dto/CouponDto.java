package com.viridi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {

	private Long id;
	
	private String name;
	
	private String code;
	
	private Double discount;
	
	private LocalDateTime expirationDate;
}
