package com.viridi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viridi.entity.Coupon;


public interface CouponRepo extends JpaRepository<Coupon, Long> {

	boolean existsByCode(String code);
	
	Optional<Coupon> findByCode(String code);
}
