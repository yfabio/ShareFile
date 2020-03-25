package com.tech.developer.infrastructure.web.controller;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tech.developer.application.FileService;
import com.tech.developer.application.FolderService;
import com.tech.developer.domain.File;
import com.tech.developer.domain.FileType;
import com.tech.developer.util.IOUtil;
import com.tech.developer.util.StringUtil;

@Controller
public class FileLoderController {
	
	
	@Autowired
	private FileService fileService;
			
	@Autowired
	private FolderService folderService;
	

	@GetMapping(path = "/files/{folder}/{id}", produces = { "application/msword", "application/vnd.ms-excel",
			"application/vnd.ms-powerpoint", "text/plain", "application/pdf", "image/png", "image/jpeg" })
	@ResponseBody // it says to spring to put the bytes into the http's body
	public byte[] getFileBytes(@PathVariable("folder") String folder, @PathVariable("id") String id) {

		byte[] data = null;
		
		String sourceDir = folderService.getSourceDir();
		
			try {
				Integer fileId = Integer.parseInt(id);
				
				File file = fileService.findFileByID(fileId);
												
				String extension = file.getName().substring(file.getName().lastIndexOf(".")+1);
				
				String filePatternDir = StringUtil.fileFormatPattern(file.getId(),extension);                                          
				
				String dir = StringUtil.getFullPath(sourceDir,StringUtil.getSeparator(),folder,StringUtil.getSeparator(),filePatternDir);
				
				data = IOUtil.getBytes(Paths.get(dir));
				
			} catch (NumberFormatException | IOException e) {
				throw new RuntimeException(e);
			}
		
		return data;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
