package com.viridi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridi.dto.ApiResponse;
import com.viridi.dto.CouponDto;
import com.viridi.exception.CustomValidationException;
import com.viridi.exception.ResourceNotFoundException;
import com.viridi.service.CouponService;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController {

	@Autowired
	private CouponService couponService;
	
	//Method to create coupon
	@PostMapping("/create")
	public ResponseEntity<?> createCoupon(@RequestBody CouponDto couponDto) {
		try {
			CouponDto createdCoupon = couponService.createCoupon(couponDto);
			return ResponseEntity.ok(createdCoupon);
		} catch (CustomValidationException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	//GetAll coupons list
	@GetMapping("/all")
	public ResponseEntity<List<CouponDto>> getAllCouponsList() {
		return new ResponseEntity<List<CouponDto>>(couponService.getAllCoupon(),HttpStatus.OK);
	}
	
	//Get One Coupon By Id
	@GetMapping("/{couponId}")
	public ResponseEntity<?> getCouponById(@PathVariable Long couponId) {
		
		ResponseEntity<?> response = null;
		
		try {
			CouponDto couponById = couponService.getCouponById(couponId);
			response = ResponseEntity.ok(couponById);
			
		} catch (Exception e) {
			
			ResourceNotFoundException exception = new ResourceNotFoundException("Coupon with ", "Coupon Id", couponId);
			response = new ResponseEntity<ResourceNotFoundException>(exception, HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	
	//Delete Coupon
	@DeleteMapping("/{couponId}")
	public ResponseEntity<?> deleteCoupon(@PathVariable Long couponId) {
		
		ApiResponse deleteCoupon = couponService.deleteCoupon(couponId);

		return new ResponseEntity<ApiResponse>(deleteCoupon,HttpStatus.OK);
	}
	
	//Get Coupon By Code
	@GetMapping("/code/{code}")
	public ResponseEntity<?> getCouponByCode(@PathVariable String code) {
		return ResponseEntity.ok(couponService.getCouponByCode(code));
	}
	
	//Update Coupon
	@PutMapping("/{couponId}")
	public ResponseEntity<?> updateCoupon(@RequestBody CouponDto couponDto, @PathVariable Long couponId) {
		
		try {
			CouponDto updated = couponService.updateCoupon(couponDto, couponId);
			return ResponseEntity.ok(updated);
		} catch (CustomValidationException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
}
