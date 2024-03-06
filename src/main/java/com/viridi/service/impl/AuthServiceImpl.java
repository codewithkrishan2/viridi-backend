package com.viridi.service.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viridi.dto.ApiResponse;
import com.viridi.dto.AuthenticationResponse;
import com.viridi.dto.UserDto;
import com.viridi.entity.Orders;
import com.viridi.entity.User;
import com.viridi.enums.OrderStatus;
import com.viridi.enums.Role;
import com.viridi.repo.OrdersRepo;
import com.viridi.repo.UserRepo;
import com.viridi.security.JwtTokenHelper;
import com.viridi.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
//	@Autowired
//	private JwtService jwtService;

//	@Autowired
//	private AuthenticationManager authenticationManager;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private OrdersRepo ordersRepo;
	
	@Override
	public AuthenticationResponse registerNewUser(UserDto request1) {

		User request = this.modelMapper.map(request1, User.class);
		
		// check if user already exist. if exist than authenticate the user
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			return new AuthenticationResponse(null, request1,"User already exist");
		}

		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setCreatedAt(LocalDateTime.now());
		user.setEnabled(false);
		// user.setRole("ROLE_USER");
		user.setRole(Role.USER);

		
		User savedUser = userRepo.save(user);
		
		Orders order = new Orders();
		order.setAddress(null);
		order.setAmount(0.0);
		order.setDiscountedAmount(0.0);
		order.setTotalAmount(0.0);
		order.setDescription(null);
		order.setUser(savedUser);
		order.setStatus(OrderStatus.PENDING);
		ordersRepo.save(order);
		
		String token = jwtTokenHelper.generateToken(user);
		
		UserDto userDto = modelMapper.map(savedUser, UserDto.class);

		
		AuthenticationResponse response = new AuthenticationResponse();
		response.setToken(token);
		response.setUser(userDto);
		response.setMessage("User registration was successful");


		return response;
	}

	@Override
	public AuthenticationResponse authenticate(UserDto request1) {
		
		User request = this.modelMapper.map(request1, User.class);
		
		
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		System.out.println("Received from crontroller to service: " + request.getEmail());
		System.out.println("Received from crontroller to service: " + request.getPassword());

		User user = userRepo.findByEmail(request.getUsername()).get();
		String jwt = jwtTokenHelper.generateToken(user);

		UserDto userDto = modelMapper.map(user, UserDto.class);
		
		AuthenticationResponse response = new AuthenticationResponse();
		response.setToken(jwt);
		response.setUser(userDto);
		response.setMessage("Succsessfully logged in");
		
		return response;

	}


	@Override
	public ApiResponse initRoleAndUser() {

		// check if user already exist. if exist than authenticate the user
		if (userRepo.findByEmail("admin@viridi.com").isPresent()) {
			return new ApiResponse("User already exist", true);
		}
		else {
			//If user is not present then create a new user
			User user = new User();
			user.setFirstName("Admin");
			user.setLastName("admin");
			user.setEmail("admin@viridi.com");
			user.setPassword(passwordEncoder.encode("admin"));
			user.setCreatedAt(LocalDateTime.now());
			user.setEnabled(true);
			user.setRole(Role.ADMIN);//

			user = userRepo.save(user);
			
			//String token = jwtService.generateToken(user);
			
			ApiResponse response = new ApiResponse("User registration was successful", true);

			return response;
		
		}
		
	}

	@Override
	public AuthenticationResponse enableUserReqest(Long id) {
		
		User user = userRepo.findById(id).get();
		
		user.setEnabled(true);
		userRepo.save(user);
		String token = jwtTokenHelper.generateToken(user);
		
		UserDto userDto = this.modelMapper.map(user, UserDto.class);

		AuthenticationResponse response = new AuthenticationResponse();
		response.setToken(token);
		response.setUser(userDto);
		response.setMessage("Succsessfully enabled user");


		return response;
	}

}
