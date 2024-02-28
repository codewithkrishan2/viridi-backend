package com.viridi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viridi.dto.AuthenticationResponse;
import com.viridi.entity.User;
import com.viridi.service.AuthService;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	
	@PostConstruct
	public void initRoleAndUser() {
		authService.initRoleAndUser();
	}
	
	
	
	//register New User to the Site
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registerNew(
			@RequestBody User request ){
		

		
		AuthenticationResponse registered = authService.registerNewUser(request);
		return ResponseEntity.ok(registered);
	}
	
	//Method for login
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> loginUser(
			@RequestBody User request ){
		
		return ResponseEntity.ok(authService.authenticate(request));
	}
	
	//Enable user
	@PutMapping("/enable/{id}")
	public ResponseEntity<AuthenticationResponse> enableUser( @PathVariable Long id ){
		
		return ResponseEntity.ok(authService.enableUserReqest(id));
	}
}
