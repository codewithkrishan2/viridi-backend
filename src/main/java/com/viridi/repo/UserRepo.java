package com.viridi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viridi.entity.Role;
import com.viridi.entity.User;
import java.util.List;


public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	
	List<User> findByRole(String role);
}
