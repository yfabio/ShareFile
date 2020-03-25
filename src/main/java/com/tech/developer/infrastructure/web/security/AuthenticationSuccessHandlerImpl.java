package com.tech.developer.infrastructure.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.sun.xml.txw2.IllegalSignatureException;
import com.tech.developer.domain.Profile;
import com.tech.developer.util.SecurityUtil;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		Profile profile = SecurityUtil.loggedUser().getRole();
		
		switch (profile) {
			case ADMIN: {
				//response.sendRedirect("staff/admin/home");
				response.sendRedirect("staff/user/home");
				break;
			}
			case USER:{				
				response.sendRedirect("staff/user/home");
				break;
			} 			
			default:
				throw new IllegalSignatureException("Authentication fail!");
		}
	}

}
