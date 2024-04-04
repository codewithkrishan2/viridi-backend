package com.viridi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viridi.entity.Review;

public interface ReviewRepo extends JpaRepository<Review, Long> {

	List<Review> findByProductId(Long productId);

}
