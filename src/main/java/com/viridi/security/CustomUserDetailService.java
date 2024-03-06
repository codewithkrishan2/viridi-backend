package com.viridi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viridi.entity.User;
import com.viridi.exception.ResourceNotFoundException;
import com.viridi.repo.UserRepo;



@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//Loading user from database
		User user = this.userRepo.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User ", " email : " +username, 0));
		
		return user;
	}

	
}
