package com.smart.configaration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.models.User;
import com.smart.repository.UserRepo;

public class UserDetailsServiceImpl implements UserDetailsService{
    
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//fatching user from database
		
		User user = userRepo.getUserByUserName(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("could not found user !!");
			
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		return customUserDetails;
	}

}
