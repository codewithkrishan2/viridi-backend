package com.viridi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viridi.entity.Role;

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
