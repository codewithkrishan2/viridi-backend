package com.viridi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viridi.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
