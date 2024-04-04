package com.viridi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.viridi.dto.ApiResponse;
import com.viridi.dto.CouponDto;

public interface CouponService {

	CouponDto createCoupon(CouponDto couponDto);
	
	List<CouponDto> getAllCoupon();
	
	CouponDto getCouponById(Long couponId);
	
	ResponseEntity<?> getCouponByCode(String code);
	
	CouponDto updateCoupon(CouponDto couponDto, Long couponId);
	
	ApiResponse deleteCoupon(Long couponId);
	
	
}
