package com.viridi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viridi.dto.AuthenticationResponse;
import com.viridi.entity.User;
import com.viridi.jwt.JwtService;
import com.viridi.repo.UserRepo;
import com.viridi.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public AuthenticationResponse registerNewUser(User request) {
	
		User user = new User();
		
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEnabled(false);
		
		//user.setRole("ROLE_USER");
		user.setRole(user.getRole());
		
		User savedUser = userRepo.save(user);
		
		String token = jwtService.generateToken(savedUser);
		
		return new AuthenticationResponse(token);
	}

	
	@Override
	public AuthenticationResponse login(User request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken( 
						request.getEmail(), 
						request.getPassword() 
						)
				);
		User userByEmail = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		String token = jwtService.generateToken(userByEmail);
		
		return new AuthenticationResponse(token);
	}

}
