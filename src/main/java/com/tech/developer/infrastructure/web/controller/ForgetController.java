package com.tech.developer.infrastructure.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tech.developer.application.ApplicationServiceException;
import com.tech.developer.application.BusinessException;
import com.tech.developer.application.StaffService;
import com.tech.developer.domain.Profile;
import com.tech.developer.domain.Sector;
import com.tech.developer.domain.Staff;
import com.tech.developer.util.Constants;
import com.tech.developer.util.StringUtil;

@Controller
@RequestMapping(path = "/public")
public class ForgetController {
	
	private boolean edit = true;
	
	@Autowired
	private StaffService staffService;
	
	@GetMapping(path = "/forget")
	public String fogertPassword(Model model) {
		starter(model, new Staff());
		return "forget";
	}
	
	public void starter(Model model, Staff staff){
		staff.setName(Constants.TEMP_VALUE);		
		staff.setPhone(Constants.TEMP_VALUE);
		staff.setProfile(Profile.USER);
		Sector sector = new Sector();
		sector.setJob(Constants.TEMP_VALUE);
		sector.setFloor(Constants.TEMP_VALUE);
		sector.setRoom(Constants.TEMP_VALUE);
		sector.setDesk(Constants.TEMP_VALUE);
		staff.setSector(sector);
		model.addAttribute("staff", staff);
		ControllerHelper.setEditMode(model, edit);		
	}
	
	@GetMapping(path = "/cancel")
	public String cancel(Model model) {
		edit = true;
		return "/";
	}

	@PostMapping(path = "/find/forget")
	public String findPassword(@ModelAttribute("staff") @Valid Staff staff, Errors errors, Model model) {
		
		
		if(edit) {			
			try {				
				if(!StringUtil.isEmpty(staff.getEmail())) {
					staff = staffService.findStaffByEmaill(staff.getEmail());
					edit = false;
					if(staff == null) {
						ControllerHelper.setEditMode(model, edit);
						throw new ApplicationServiceException("We cannot find an account with that email address");
					}					
				}				
				ControllerHelper.setEditMode(model,edit);					
				staff.setPassword("");
				staff.setConfirmPassword("");
				starter(model, staff);
			} catch (ApplicationServiceException e) {
				errors.rejectValue("email", null,e.getMessage());
			}			
		}else {
			
			try {
				String first = staff.getPassword().toUpperCase();
				String second = staff.getConfirmPassword().toUpperCase();
				
				if(first.equals(second)) {
					staff = staffService.findStaffById(staff.getId());
					staff.setPassword(first.toLowerCase());
					staff.encryptPassword();	
					staff = staffService.saveStaff(staff);
					model.addAttribute("msg","Password has been set successfully");
					edit = true;
					starter(model, new Staff());
				}else {					
					starter(model, staff);
					throw new ApplicationServiceException("Passwords are not equal");
				}			
				
			} catch (ApplicationServiceException | BusinessException e) {
				model.addAttribute("error", e.getMessage());
			}		
			
		}
		
		return "forget";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
