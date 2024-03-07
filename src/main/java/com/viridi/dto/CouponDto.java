package com.viridi.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {

	private Long id;
	
	@NotBlank(message = "Coupon name is required")
	private String name;
	
	@NotBlank(message = "Coupon code is required")
	private String code;
	
	private Double discount;
	
	private LocalDateTime expirationDate;
}
