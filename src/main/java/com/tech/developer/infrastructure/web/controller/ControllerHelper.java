package com.tech.developer.infrastructure.web.controller;

import org.springframework.ui.Model;


public class ControllerHelper {
	
	
	public static void setEditMode(Model model, boolean edit) {
		model.addAttribute("edit",edit);
	}

}
