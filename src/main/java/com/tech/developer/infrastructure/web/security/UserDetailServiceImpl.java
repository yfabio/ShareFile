package com.tech.developer.infrastructure.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tech.developer.domain.Staff;
import com.tech.developer.domain.repositories.StaffRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private StaffRepository staffRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Staff staff = staffRepository.findByEmail(username);
		
		if(staff == null) {
			throw new UsernameNotFoundException(username);
		}
			
		return new Credential(staff);
	}

}
