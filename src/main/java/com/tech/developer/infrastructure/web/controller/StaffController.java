package com.tech.developer.infrastructure.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tech.developer.application.FolderService;

import com.tech.developer.application.PoolService;
import com.tech.developer.application.StaffService;
import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Pool;
import com.tech.developer.domain.Staff;
import com.tech.developer.util.SecurityUtil;

@Controller
@RequestMapping(path = "/staff")
public class StaffController {

	@Autowired
	private StaffService staffService;

	@Autowired
	private FolderService folderService;

	@Autowired
	private PoolService poolService;

	@GetMapping(path = "/user/home")
	public String user(Model model) {
		starter(model, SecurityUtil.loggedStaff());
		return "index";
	}

	private void starter(Model model, Staff staff) {
		List<Pool> pools = poolService.findAllPoolByStaff(staff);
		List<Folder> folders = folderService.findAllFoldersByStaff(staff);
		staff.setFolders(folders);
		staff.setMaxFiles(staffService.getAllFiles(staff));
		staff.setPools(pools);
		staff.getWelcomeMessage();
		staff.setGroups(poolService.findSharedPools(staff.getId()));
		staff.setStaffs(staffService.findAllStaffs());		
		model.addAttribute("staff", staff);
	}

	

}
