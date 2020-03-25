package com.tech.developer.infrastructure.web.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tech.developer.application.ApplicationServiceException;
import com.tech.developer.application.BusinessException;
import com.tech.developer.application.FolderService;
import com.tech.developer.application.PoolService;
import com.tech.developer.application.StaffService;
import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Pool;
import com.tech.developer.domain.Staff;
import com.tech.developer.util.SecurityUtil;
import com.tech.developer.util.StringUtil;

@Controller
@RequestMapping(path = "/staff")
public class PoolsController {
	
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private PoolService poolService;
	
	@Autowired
	private FolderService folderService;
	
	@GetMapping(path = "/user/sharepools")
	public String poolsShare(Model model) {
		starter(model, SecurityUtil.loggedStaff());	
		return "groups";
	}	
	
	public void starter(Model model,Staff staff) {
		staff = staffService.findStaffById(staff.getId());
		List<Pool> pools = poolService.findPoolsByStaffId(staff.getId());
		staff.setPools(pools);
		List<Folder> folders = getAvailableFolders(staff);
		staff.setFolders(folders);
		staff.setGroups(poolService.findSharedPools(staff.getId()));
		model.addAttribute("staff",staff);		
	}
	
	@PostMapping(path = "/user/pools/share")
	public String share(@ModelAttribute("staff") @Valid Staff staff, Errors errors, Model model) {
		return forward(staff,errors,model);
	}
	
	@GetMapping(path = "/user/pools/unshare")
	public String unshare(@RequestParam Optional<String> id, Model model) {
		
		try {
			String poolId = id.orElseThrow();			
			Pool pool = poolService.findPoolById(Integer.parseInt(poolId));
			String message = StringUtil.getFullPath("The folder ",pool.getFolder().getName()," has been unshared successfully");
			pool.setFolder(null);
			pool = poolService.savePool(pool);			
			model.addAttribute("msg",message);
			starter(model, SecurityUtil.loggedStaff());
		} catch (ApplicationServiceException | NumberFormatException | BusinessException e) {
			model.addAttribute("error",e.getMessage());
		}
		
		return "groups";
	}

	private String forward(@Valid Staff staff, Errors errors, Model model) {
		
		try {
						
			Pool pool = poolService.findPoolById(staff.getPool().getId());			
			Folder folder = folderService.findFolderById(staff.getFolder().getId());
						
			pool.setFolder(folder);
			pool =  poolService.savePool(pool);
					
			model.addAttribute("msg",StringUtil.getFullPath("Folder ",folder.getName()," has been shared successfully"));			
			starter(model, SecurityUtil.loggedStaff());
						
		} catch (ApplicationServiceException | BusinessException e) {
			model.addAttribute("error",e.getMessage());
		}
		
		return "groups";
	}
	
	
	public List<Folder> getAvailableFolders(Staff staff){
		
		try {
			List<Pool> pools = poolService.findAllPoolByStaff(staff);
			
			List<Folder> folders = folderService.findAllFoldersByStaff(staff);
			
			for (Pool pool : pools) {
				
				Folder fold = pool.getFolder();
									
				if(fold!=null) {					
					folders.remove(fold);					
				}
			}
			
			return folders;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	
	}
	
	
}
