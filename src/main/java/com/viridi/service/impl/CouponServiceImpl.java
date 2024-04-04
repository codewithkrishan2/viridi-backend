package com.viridi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.viridi.dto.ApiResponse;
import com.viridi.dto.CouponDto;
import com.viridi.entity.Coupon;
import com.viridi.exception.CustomValidationException;
import com.viridi.exception.ResourceNotFoundException;
import com.viridi.repo.CouponRepo;
import com.viridi.service.CouponService;


@Service
public class CouponServiceImpl implements CouponService {
	
	@Autowired
	private CouponRepo couponRepo;
	
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CouponDto createCoupon(CouponDto couponDto) {
		
		if (couponRepo.existsByCode(couponDto.getCode())) {
			throw new CustomValidationException("Coupon Code already exists");
		}
		
		Coupon coupon = modelMapper.map(couponDto, Coupon.class);
		Coupon savedCoupon = couponRepo.save(coupon);
		
		return modelMapper.map(savedCoupon, CouponDto.class);
	}


	@Override
	public List<CouponDto> getAllCoupon() {
		List<Coupon> coupons = couponRepo.findAll();
		return coupons.stream().map(coupon -> modelMapper.map(coupon, CouponDto.class)).collect(Collectors.toList());
	}


	@Override
	public CouponDto getCouponById(Long couponId) {
		Coupon coupon = couponRepo.findById(couponId).orElseThrow(()-> new ResourceNotFoundException("Coupon", "Coupon Id", couponId));
		return modelMapper.map(coupon, CouponDto.class);
	}


	@Override
	public CouponDto updateCoupon(CouponDto couponDto, Long couponId) {
		Coupon coupon = couponRepo.findById(couponId).orElseThrow(()-> new ResourceNotFoundException("Coupon", "Coupon Id", couponId));
		
		if (couponRepo.existsByCode(couponDto.getCode())) {
			throw new CustomValidationException("Coupon Code already exists");
		}
		
		coupon.setCode(couponDto.getCode());
		coupon.setDiscount(couponDto.getDiscount());
		coupon.setExpirationDate(couponDto.getExpirationDate());
		coupon.setName(couponDto.getName());
		
		Coupon updatedCoupon = couponRepo.save(coupon);
		
		return modelMapper.map(updatedCoupon, CouponDto.class);
		
	}


	@Override
	public ApiResponse deleteCoupon(Long couponId) {
		
		Coupon coupon = couponRepo.findById(couponId).orElseThrow(()-> new ResourceNotFoundException("Coupon ", "Coupon Id", couponId));
		couponRepo.delete(coupon);
		
		return new ApiResponse("Coupon deleted successfully", true);
	}


	@Override
	public ResponseEntity<?> getCouponByCode(String code) {
		
		Optional<Coupon> coupon = couponRepo.findByCode(code);
		
		ResponseEntity<?> response = null;
		
		if (coupon.isPresent()) {
			response =  new ResponseEntity<CouponDto>(modelMapper.map(coupon.get(), CouponDto.class), HttpStatus.OK);
		}else {
			response =  new ResponseEntity<ApiResponse>(new ApiResponse("Coupon not found", false), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}

}
