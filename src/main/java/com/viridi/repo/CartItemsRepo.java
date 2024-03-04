package com.viridi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viridi.entity.CartItems;


public interface CartItemsRepo extends JpaRepository<CartItems, Long> {

	Optional<CartItems> findByProductIdAndOrdersIdAndUserId(Long productId, Long orderId, Long userId);

	
}
