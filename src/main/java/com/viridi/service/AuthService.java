package com.viridi.service;

import com.viridi.dto.AuthenticationResponse;
import com.viridi.entity.User;

public interface AuthService {

	public AuthenticationResponse registerNewUser(User request);
	
	public AuthenticationResponse login(User request);

}
