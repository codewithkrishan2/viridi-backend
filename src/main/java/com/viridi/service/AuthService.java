package com.viridi.service;

import com.viridi.dto.ApiResponse;
import com.viridi.dto.AuthenticationResponse;
import com.viridi.dto.UserDto;

public interface AuthService {

	public AuthenticationResponse registerNewUser(UserDto request);
	public AuthenticationResponse authenticate(UserDto request);
	
	public ApiResponse initRoleAndUser();
	
	public AuthenticationResponse enableUserReqest(Long id);
}
