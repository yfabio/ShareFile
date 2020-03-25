package com.tech.developer.infrastructure.web.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tech.developer.application.ApplicationServiceException;
import com.tech.developer.application.BusinessException;
import com.tech.developer.application.SectorService;
import com.tech.developer.application.StaffService;
import com.tech.developer.domain.Sector;
import com.tech.developer.domain.Staff;
import com.tech.developer.util.Constants;

@Controller
@RequestMapping(path = "/public")
public class PublicController {
	
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private SectorService sectorService;
	
	@GetMapping("/account/new")
	public String newAccount(Model model) {		
		model.addAttribute("staff",new Staff());		
		return "account";
	}
	

	@PostMapping(path = "/account/save")
	public String saveAccount(@ModelAttribute("staff") @Valid Staff staff, Errors errors, Model model) {	
		
		try {
			if(!errors.hasErrors()) {						
				
				staff.setSector(sectorService.saveSector(staff.getSector()));				
				staff.encryptPassword();				
				staffService.saveStaff(staff);
				model.addAttribute("msg","User has been created successfully!");
				Constants.Log.d(staff.toString());								
							
			}
		}catch (BusinessException e) {
			errors.rejectValue("email",null,e.getMessage());
		}catch (ApplicationServiceException e) {
			model.addAttribute("error", e.getMessage());
		}
						
		return "account";
	}

}
