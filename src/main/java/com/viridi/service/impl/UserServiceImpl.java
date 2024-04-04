package com.viridi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viridi.dto.UserDto;
import com.viridi.entity.User;
import com.viridi.exception.ResourceNotFoundException;
import com.viridi.repo.UserRepo;
import com.viridi.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UserDto getUserById(Long id) {
		User user = this.userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException( "User", "Id", id));
		
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User ", "Email", 0));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateOneUser(Long id, UserDto userDto) {
		
		User user = this.userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException( "User", "Id", id));
		
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		User savedUser = userRepo.save(user);
		
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = userRepo.findAll();
		
		List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
		
		return userDtos	;
	}

	@Override
	public void deleteOneUser(Long id) {
		
		User user = this.userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException( "User", "Id", id));
		
		this.userRepo.delete(user);
	}

}
