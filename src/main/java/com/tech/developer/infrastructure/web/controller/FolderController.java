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
public class FolderController {
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private PoolService poolService;
	
	
	@GetMapping(path = "/user/folder")
	public String newFolder(Model model) {
		starter(model,new Folder(), false);
		return "folder";
	}
	
	
	private void starter(Model model,Folder folder,boolean edit) {
		model.addAttribute("folder", folder);
		ControllerHelper.setEditMode(model, edit);		
	}
	
	@GetMapping(path = "/user/folders")
	public String UserPools(@ModelAttribute("staff") Staff staff, Model model) {
		staff = staffService.findStaffById(SecurityUtil.loggedStaff().getId());
		model.addAttribute("staff",staff);
		return "folder_work";
	}
	
	
	@GetMapping(path = "/folder/setup")
	public String setupFolder(@RequestParam Optional<String> id, Model model) {	
		try {			
			String value = id.orElseThrow();			
			Folder folder = folderService.findFolderById(Integer.parseInt(value));						
			starter(model, folder, true);
		} catch (Exception e) {
			model.addAttribute("error",e.getMessage());
		}
		
		return "folder";
	}
	
	
	@Transactional
	@GetMapping(path = "/folder/delete")
	public String deletePool(@RequestParam Optional<String> id, Model model) {
		
		List<Folder> folders = null;
		Staff staff = null;
		Folder folder = null;
		
		try {
			
			String value = id.orElseThrow();			
			folder = folderService.findFolderById(Integer.parseInt(value));
			staff = staffService.findStaffById(folder.getStaff().getId());					
			Pool pool = poolService.findPoolByFolder(folder);
			
			if(pool == null) {
								
				String sourceDir = folderService.getSourceDir();
				String folderPartternDir = StringUtil.dirFormatPattern(folder.getId(),folder.getName());
				String dir = StringUtil.getFullPath(sourceDir,StringUtil.getSeparator(),folderPartternDir);
				
				staff = staffService.findStaffById(folder.getStaff().getId());
										
				folderService.deleteDir(dir);
				folderService.deleteFolder(folder);
				staff.getFolders().remove(folder);
				
				
							
				folders = folderService.findAllFoldersByStaff(staff);
				staff.setFolders(folders);
				model.addAttribute("staff",staff);
				model.addAttribute("msg", "Folder has been deleted successfully");
			}else {
				throw new ApplicationServiceException("Connot delete the folder "+folder.getName()+" because It belongs to " + pool.getName() + " group");
			}
			
		} catch (ApplicationServiceException e) {
			model.addAttribute("staff", staff);
			model.addAttribute("error",StringUtil.getFullPath("The ",folder.getName(),e.getMessage()));
		}
		
		return "folder_work";
	}
	
	
	
	@PostMapping(path = "/user/folder/update")
	public String updateFolder(@ModelAttribute("folder") @Valid Folder folder, Errors errors, Model model) {
		return forward(folder, errors, model,SecurityUtil.loggedStaff());
	}


	private String forward(@Valid Folder folder, Errors errors, Model model, Staff staff) {
		
		try {
			
			// /home/user/sharefile	
			String sourceDir = folderService.getSourceDir();
			
			 staff = staffService.findStaffById(staff.getId());
			
			
			 if(!errors.hasErrors()) {
				
				if(folder.getId() != null) {
					
					Folder folderDB = folderService.findFolderById(folder.getId());
					
					String sourcePartternDir = StringUtil.dirFormatPattern(folderDB.getId(),folderDB.getName());
					String source = StringUtil.getFullPath(sourceDir,StringUtil.getSeparator(),sourcePartternDir);
					
					String targetPartternDir = StringUtil.dirFormatPattern(folder.getId(),folder.getName());
					String target = StringUtil.getFullPath(sourceDir,StringUtil.getSeparator(),targetPartternDir);
					
					folderService.renameDir(source, target);
					folder.setStaff(staff);
					folderService.saveFolder(folder);	
					
					
					
					model.addAttribute("msg","Folder has been updated successfully");
					
				}else{
					
					folder.setStaff(staff);
					staff.getFolders().add(folder);
					folder = folderService.saveFolder(folder);
					staffService.saveStaff(staff);
					
					// 0001-Finances
					String folderPartternDir = StringUtil.dirFormatPattern(folder.getId(),folder.getName());
					
					String dir = StringUtil.getFullPath(sourceDir,StringUtil.getSeparator(),folderPartternDir);
					folderService.createDir(dir);
					model.addAttribute("msg","Folder has been created successfully");
					
					
				}
				
				starter(model,new Folder(),false);
			}else {
				model.addAttribute("error","Ops… something wrong");
			}
			
		} catch (ApplicationServiceException | BusinessException   e) {
			model.addAttribute("error",e.getMessage());
		} 
		
		return "folder";
	}


	

}
