package com.viridi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viridi.dto.AuthenticationResponse;
import com.viridi.entity.Token;
import com.viridi.entity.User;
import com.viridi.jwt.JwtService;
import com.viridi.repo.TokenRepository;
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

	@Autowired
	private TokenRepository tokenRepository;

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
		user.setEnabled(true);
		// user.setRole("ROLE_USER");
		user.setRole(request.getRole());

		user = userRepo.save(user);

		String token = jwtService.generateToken(user);

		//Save the genrated token
		saveUserToken(token, user);

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

		revokeAllTokenByUser(user);
		saveUserToken(jwt, user);

		return new AuthenticationResponse(jwt, "User login was successful");
	}

	@Override
	public AuthenticationResponse loginUserservice(User request) {
		// TODO Auto-generated method stub
		return null;
	}

	private void revokeAllTokenByUser(User user) {
		List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
		if (validTokens.isEmpty()) {
			return;
		}

		validTokens.forEach(t -> {
			t.setLoggedOut(true);
		});

		tokenRepository.saveAll(validTokens);
	}

	private void saveUserToken(String jwt, User user) {
		Token token = new Token();
		token.setToken(jwt);
		token.setLoggedOut(false);
		token.setUser(user);
		tokenRepository.save(token);
	}

}
