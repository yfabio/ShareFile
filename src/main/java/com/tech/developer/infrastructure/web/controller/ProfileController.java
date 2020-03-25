package com.tech.developer.infrastructure.web.controller;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.tech.developer.application.ApplicationServiceException;
import com.tech.developer.application.BusinessException;
import com.tech.developer.application.SectorService;
import com.tech.developer.application.StaffService;
import com.tech.developer.domain.Sector;
import com.tech.developer.domain.Staff;
import com.tech.developer.util.Constants;
import com.tech.developer.util.SecurityUtil;

@Controller
@RequestMapping(path = "/staff")
public class ProfileController {
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private SectorService sectorService;
	
	@GetMapping(path = "/user/profile")
	public String UserProfile(Model model) {		
		profiles(model);
		return "profile";
	}
	
	public void profiles(Model model) {
		Staff staff = SecurityUtil.loggedStaff();
		staff = staffService.findStaffById(staff.getId());		
		staff.setConfirmPassword(Constants.TEMP_VALUE);		
		model.addAttribute("staff", staff);	
	}
		
	
	@PostMapping(path = "/user/profile/update")
	public String setProfile(@ModelAttribute("staff") @Valid Staff staff, Errors errors, Model model) {
		forward(staff, errors, model);
		return "profile";
	}
	
	
	@GetMapping(path = "/user/profiles/update")
	public String updateProfile(@RequestParam() Optional<Integer> id,Model model) {
		
		try {
			Integer code = id.orElseThrow();			
			Staff staff = staffService.findStaffById(code);		
			staff.setConfirmPassword(Constants.TEMP_VALUE);
			model.addAttribute("staff", staff);
		} catch (ApplicationServiceException e) {
			model.addAttribute("error",e.getMessage());
		}
				
		return "profile";
	}
	
	
	public void forward(@ModelAttribute("staff") @Valid Staff staff, Errors errors, Model model) {
		try { 
			
			Staff  staffDB = staffService.findStaffById(staff.getId());
		
			Sector sectorDB  = sectorService.findSectorById(staffDB.getSector().getId());
						
			if (!errors.hasErrors()) {	
										
				sectorDB.setJob(staff.getSector().getJob());
				sectorDB.setFloor(staff.getSector().getFloor());
				sectorDB.setRoom(staff.getSector().getRoom());
				sectorDB.setDesk(staff.getSector().getDesk());	
								
				staff.setSector(sectorDB);			
				staff.setPassword(staffDB.getPassword());				
				Constants.Log.d(staff.toString());		
				
				staffService.saveStaff(staff);								
				model.addAttribute("msg", "User has been updated successfully");
				
			}else {
				throw new ApplicationServiceException("Ops… something wrong");
			}	
		
			
		}catch (BusinessException e){
			errors.rejectValue("email", null,e.getMessage());
			Constants.Log.e(e.getMessage());
		}catch(ApplicationServiceException e){
			model.addAttribute("error",e.getMessage());
			Constants.Log.e(e.getMessage());
		}catch (Exception e) {
			model.addAttribute("error",e.getMessage());
		}
	}
	
	

}
