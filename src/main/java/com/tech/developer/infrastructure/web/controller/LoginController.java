package com.tech.developer.infrastructure.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tech.developer.domain.Staff;
import com.tech.developer.util.SecurityUtil;

@Controller
public class LoginController {

	@GetMapping(path = {"/login","/"})
	public String login() {	
		if(SecurityUtil.loggedUser() != null) {
			return "redirect:/staff/user/home";
		}
		return "login";
	}
		
	@GetMapping(path = "/login-error")
	public String loginError(Model model) {
		model.addAttribute("msg","Credential wasn't valid!");
		return "login";
	}
	
}
