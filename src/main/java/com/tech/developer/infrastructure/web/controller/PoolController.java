package com.tech.developer.infrastructure.web.controller;

import java.util.List;
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
import com.tech.developer.application.PoolService;
import com.tech.developer.application.StaffService;
import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Pool;
import com.tech.developer.domain.Staff;
import com.tech.developer.util.SecurityUtil;

@Controller
@RequestMapping(path = "/staff")
public class PoolController {
		

	@Autowired
	private StaffService staffService;
	
	@Autowired
	private PoolService poolService;
	
	@GetMapping(path = "/user/pool")
	public String UserPool(Model model) {		
		starter(model,new Pool(),false);
		return "group";
	}
	
	@GetMapping(path = "/user/pools")
	public String UserPools(@ModelAttribute("staff") Staff staff, Model model) {
		staff = staffService.findStaffById(SecurityUtil.loggedStaff().getId());
		List<Pool> staffPool = poolService.findAllPoolByStaff(staff);		
		staff.setPools(staffPool);
		model.addAttribute("staff",staff);
		return "work_group";
	}
	
	@GetMapping(path = "/pool/setup")
	public String setupPool(@RequestParam Optional<String> id, Model model) {
		try {
			String value = id.orElseThrow();
			Pool pool = poolService.findPoolById(Integer.parseInt(value));
			starter(model, pool, true);
		} catch (Exception e) {
			model.addAttribute("error",e.getMessage());
		}
		
		return "group";
	}
	
	@Transactional
	@GetMapping(path = "/pool/delete")
	public String deletePool(@RequestParam Optional<String> id, Model model) {
		try {
			String value = id.orElseThrow();			
			Pool pool = poolService.findPoolById(Integer.parseInt(value));			
			Staff staff = staffService.findStaffById(pool.getStaff().getId());	
			staff.getPools().remove(pool);
			poolService.deletePool(pool);
			model.addAttribute("staff",staff);
			model.addAttribute("msg", "Group has been deleted successfully");
		} catch (Exception e) {
			model.addAttribute("error",e.getMessage());
		}
		
		return "work_group";
	}
	
	
	private void starter(Model model,Pool pool,boolean edit) {
		ControllerHelper.setEditMode(model, edit);
		model.addAttribute("pool",pool);			
		model.addAttribute("values",staffService.getAllStaffs());		
	}
	

	@PostMapping(path = "/user/pool/update")
	public String update(@ModelAttribute("pool") @Valid Pool pool, Errors errors, Model model) {
		return forward(pool, errors, model,SecurityUtil.loggedStaff());
	}
	
	@Transactional
	private String forward(@Valid Pool pool, Errors errors, Model model,Staff staff) {
		
		
		try {
			
			staff = staffService.findStaffById(staff.getId());
			List<Pool> pools = poolService.findAllPoolByStaff(staff);
			staff.setPools(pools);
			
			if(!errors.hasErrors()) {
				
				if(pool.getId() != null) {					
					Pool poolDB = poolService.findPoolById(pool.getId());
					pool.setFolder(poolDB.getFolder());
					pool.setStaff(staff);					
					pool = poolService.savePool(pool);
					staff.getPools().add(pool);					
					model.addAttribute("msg","Group has been updated successfully");					
				}else {
					pool.setStaff(staff);
					pool = poolService.savePool(pool);
					staff.getPools().add(pool);
					staff = staffService.saveStaff(staff);
					model.addAttribute("msg", "Group has been created successfully");				
				}	
				starter(model,new Pool(),false);				
			}else {
				starter(model, pool, false);
				model.addAttribute("error","Ops… something wrong");
			}
			
			
		} catch (ApplicationServiceException | BusinessException e) {
			model.addAttribute("error",e.getMessage());
		}
		
		
		return "group";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
