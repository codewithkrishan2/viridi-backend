package com.viridi.service;

import java.util.List;

import com.viridi.dto.UserDto;

public interface UserService {

	UserDto getUserById(Long id);
	
	UserDto getUserByEmail(String email);
	
	UserDto updateOneUser(Long id, UserDto userDto);	
	
	List<UserDto> getAllUsers();
	
	void deleteOneUser(Long id);

	
}
