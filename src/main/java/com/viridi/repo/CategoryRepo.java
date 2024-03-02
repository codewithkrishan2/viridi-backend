package com.viridi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viridi.entity.Category;
import java.util.List;


public interface CategoryRepo extends JpaRepository<Category, Long> {

	List<Category> findByName(String name);
}
