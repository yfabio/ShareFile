package com.tech.developer.application;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tech.developer.domain.File;
import com.tech.developer.domain.Folder;
import com.tech.developer.domain.repositories.FileRepository;
import com.tech.developer.util.IOUtil;

@Service
public class FileService {

	
	@Autowired
	private FileRepository fileRepository;
	
	
	public List<File> findFilesByFolder(Folder folder){
		return fileRepository.findAllByFolder(folder);
	}
	
	public void deleteFile(File file) {
		fileRepository.delete(file);
	}
	
	public File saveFile(File file) {
		return fileRepository.save(file);
	}
	
	public File findFileByID(Integer id) {
		return fileRepository.findById(id).orElseThrow();
	}
	
	
	
		
}
