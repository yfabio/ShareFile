package com.tech.developer.infrastructure.web.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tech.developer.domain.Profile;
import com.tech.developer.domain.Staff;

public class Credential implements UserDetails {
	
	private Staff staff;
	
	private Profile role;
	
	private Collection<? extends GrantedAuthority> roles;
	
	public Credential(Staff staff) {
		this.staff = staff;	
		this.role = staff.getProfile();
		roles = List.of(new SimpleGrantedAuthority("ROLE_"+role));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {		
		return staff.getPassword();
	}

	@Override
	public String getUsername() {
		return staff.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {		
		return true;
	}

	@Override
	public boolean isEnabled() {		
		return true;
	}
	
	public Staff getStaff() {
		return staff;
	}

	public Profile getRole() {
		return role;
	}
}
