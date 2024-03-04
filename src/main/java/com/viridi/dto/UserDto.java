package com.viridi.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viridi.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private LocalDateTime createdAt;
	
	private Role role;

	private boolean isEnabled;
	
	@JsonIgnore
    public String getPassword() {
    	return this.password;
    }
    
    @JsonProperty
    public void setPassword(String password) {
	    this.password = password;
    }
}
