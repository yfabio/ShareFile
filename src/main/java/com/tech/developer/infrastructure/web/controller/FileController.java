package com.tech.developer.infrastructure.web.controller;

import java.io.IOException;

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
import com.tech.developer.application.FileService;
import com.tech.developer.application.FolderService;
import com.tech.developer.application.StaffService;
import com.tech.developer.domain.File;
import com.tech.developer.domain.FileType;
import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Staff;
import com.tech.developer.util.IOUtil;
import com.tech.developer.util.SecurityUtil;
import com.tech.developer.util.StringUtil;

@Controller
@RequestMapping(path = "/staff")
public class FileController {
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FolderService folderService;
		
	@GetMapping(path = "/user/file")
	public String fileNew(Model model) {
		started(model, new File());
		return "file";
	}
	
	
	public void started(Model model, File file) {
		Staff staff = staffService.findStaffById(SecurityUtil.loggedStaff().getId());		
		model.addAttribute("file",file);
		model.addAttribute("folders", staff.getFolders());		
	}
	
	
	
	@PostMapping(path = "/user/file/update")
	public String updateFolder(@ModelAttribute("file") @Valid File file, Errors errors, Model model) {
		return forward(file, errors, model,SecurityUtil.loggedStaff());
	}


	private String forward(@Valid File file, Errors errors, Model model, Staff staff) {
		
		
		try {
			
			String sourceDir = folderService.getSourceDir();
			
			staff = staffService.findStaffById(staff.getId());
			
			Folder folder = folderService.findFolderById(file.getFolder().getId());
			
			if(!errors.hasErrors()) {
				
				file.setExtension();
				file.setFolder(folder);
				file = fileService.saveFile(file);	
				
				String folderPartternDir = StringUtil.dirFormatPattern(folder.getId(),folder.getName());
				
				String filePartternDir = StringUtil.fileFormatPattern(file.getId(), FileType.of(file.getFileData().getContentType()).getExtension());
				
				String dir = StringUtil.getFullPath(sourceDir,StringUtil.getSeparator(),folderPartternDir,StringUtil.getSeparator(),filePartternDir);
				
				IOUtil.copy(file.getFileData().getInputStream(), dir);
				
				model.addAttribute("msg","File has been created successfully");
				started(model, new File());
				
				
				
			}else {	
				started(model, file);
				throw new ApplicationServiceException("Ops… something wrong");			
			}
			
			
		} catch (ApplicationServiceException | IOException  e) {
			model.addAttribute("error",e.getMessage());
		}
		
		
		return "file";
	}
	
	
	

}
