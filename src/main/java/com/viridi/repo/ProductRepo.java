package com.viridi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viridi.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

}
