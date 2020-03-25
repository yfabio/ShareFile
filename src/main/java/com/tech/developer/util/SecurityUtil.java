package com.tech.developer.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tech.developer.domain.Staff;
import com.tech.developer.infrastructure.web.security.Credential;

public class SecurityUtil {

	public static Credential loggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth instanceof AnonymousAuthenticationToken) {
			return null;
		}
		
		return ((Credential) auth.getPrincipal());
				
	}
	
	
	public static Staff loggedStaff() {
		Credential credential = loggedUser();
		if(credential == null) {
			throw new IllegalArgumentException("User has not been logged!");
		}
		return credential.getStaff();
	}
	
}
