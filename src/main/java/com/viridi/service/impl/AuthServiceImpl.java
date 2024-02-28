package com.viridi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viridi.dto.AuthenticationResponse;
import com.viridi.entity.Role;
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

		// check if user already exist. if exist than authenticate the user
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			return new AuthenticationResponse(null, "User already exist");
		}

		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEnabled(false);
		// user.setRole("ROLE_USER");
		user.setRole(Role.USER);

		user = userRepo.save(user);

		String token = jwtService.generateToken(user);


		return new AuthenticationResponse(token, "User registration was successful");
	}

	@Override
	public AuthenticationResponse authenticate(User request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		System.out.println("Received from crontroller to service: " + request.getEmail());
		System.out.println("Received from crontroller to service: " + request.getPassword());

		User user = userRepo.findByEmail(request.getUsername()).get();
		String jwt = jwtService.generateToken(user);


		return new AuthenticationResponse(jwt, "User login was successful");
	}


	@Override
	public AuthenticationResponse initRoleAndUser() {

		// check if user already exist. if exist than authenticate the user
		if (userRepo.findByEmail("admin@viridi.com").isPresent()) {
			return new AuthenticationResponse(null, "User already exist");
		}
		else {
			User user = new User();
			user.setFirstName("Admin");
			user.setLastName("admin");
			user.setEmail("admin@viridi.com");
			user.setPassword(passwordEncoder.encode("admin"));
			user.setEnabled(true);
			user.setRole(Role.ADMIN);

			user = userRepo.save(user);
			String token = jwtService.generateToken(user);

			return new AuthenticationResponse(token, "User registration was successful");
		
		}
		

	}

	@Override
	public AuthenticationResponse enableUserReqest(Long id) {
		User user = userRepo.findById(id).get();
		user.setEnabled(true);
		userRepo.save(user);

		String token = jwtService.generateToken(user);


		return new AuthenticationResponse(token, "User enabled was successful");
	}

}
