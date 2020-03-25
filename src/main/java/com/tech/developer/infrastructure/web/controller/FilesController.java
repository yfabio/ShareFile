package com.tech.developer.infrastructure.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tech.developer.application.ApplicationServiceException;
import com.tech.developer.application.FileService;
import com.tech.developer.application.FolderService;
import com.tech.developer.application.StaffService;
import com.tech.developer.domain.File;
import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Staff;
import com.tech.developer.util.SecurityUtil;
import com.tech.developer.util.StringUtil;

@Controller
@RequestMapping(path = "/staff")
public class FilesController {
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FolderService folderService;
	
	
	@GetMapping(path = "/user/files")
	public String filesLoad(Model model) {
		started(model, SecurityUtil.loggedStaff());
		return "filemanaged";		
	}
	
	public void started(Model model,Staff staff) {
		staff = staffService.findStaffById(staff.getId());		
		model.addAttribute("staff",staff);		
	}
	
	
	@PostMapping(path = "/user/files/update")
	public String updateFiles(@ModelAttribute("staff") Staff staff, Model model) {
		return forward(staff,model);
	}
	
	@GetMapping(path = "/user/files/delete")
	public String deleteFiles(@RequestParam Optional<String> id, Model model) {
		
		File file = null;
		
		try {
			
			String sourceDir = folderService.getSourceDir();
			String code = id.orElseThrow();
			Integer fileId = Integer.parseInt(code);
			
			file = fileService.findFileByID(fileId);
			
			Folder folder = file.getFolder();
						
			String folderPatternDir = StringUtil.dirFormatPattern(folder.getId(),folder.getName());
			
			String extension = file.getName().substring(file.getName().lastIndexOf(".")+1);
			
			String filePatternDir = StringUtil.fileFormatPattern(file.getId(),extension);
			
			String dir = StringUtil.getFullPath(sourceDir,StringUtil.getSeparator(),folderPatternDir,StringUtil.getSeparator(),filePatternDir);
			
			folderService.deleteDir(dir);
			fileService.deleteFile(file);
			
			model.addAttribute("msg", "File has been deleted successfully");
			started(model, SecurityUtil.loggedStaff());
			
						
		} catch (ApplicationServiceException e) {
			started(model, SecurityUtil.loggedStaff());
			model.addAttribute("error",StringUtil.getFullPath("The ",file.getName(),e.getMessage()) );
		}
		
		
		return "filemanaged";
	}

	
	
	private String forward(Staff staff, Model model) {
		
		
		try {
			
				
			Folder folder = folderService.findFolderById(staff.getFolder().getId());
			
			List<File> files = fileService.findFilesByFolder(folder);
						
			String folderPartternDir = StringUtil.dirFormatPattern(folder.getId(),folder.getName());
						
			folder.setFolderPatternDir(folderPartternDir);			
			folder.setFiles(files);			
			
			
			model.addAttribute("folder",folder);
			
			started(model, SecurityUtil.loggedStaff());
			
		} catch (ApplicationServiceException e) {
			model.addAttribute("error",e.getMessage());
		}
		
		
		return "filemanaged";
	}

	
	
}
