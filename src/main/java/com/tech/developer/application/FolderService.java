package com.tech.developer.application;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Pool;
import com.tech.developer.domain.Staff;
import com.tech.developer.domain.repositories.FolderRepository;
import com.tech.developer.util.IOUtil;
import com.tech.developer.util.StringUtil;

@Service
public class FolderService {
	
	@Autowired
	private FolderRepository folderRepository;
	
	@Value("${com.tech.developer.path.target}")
	private String targetFolder;
		
	
	public Folder saveFolder(Folder folder) {
		return folderRepository.save(folder);
	}	
	
	public List<Folder> findAllFoldersByStaff(Staff staff){
		return folderRepository.findByStaff(staff);
	}
	
	public List<Folder> findAllFoldersByStaff(Integer id){
		return folderRepository.findAllFoldersFromStaffId(id);
	}
		
	public void deleteFolder(Folder folder) {
		folderRepository.delete(folder);
	}

	public Folder findFolderByName(String name) {
		return folderRepository.findByName(name);
	}
	
	public List<Folder> findAll(){
		return folderRepository.findAll();
	}
	
	public List<Folder> findByStafId(Integer id){
		return folderRepository.findFoldersByStaffId(id);
	}
	
	
	public void createDir(String dir)  {
		try {
			IOUtil.createFolder(dir);
		} catch (IOException e) {
			throw new ApplicationServiceException("Name was in use, try again", e);
		}
	}
	
	public Folder findFolderById(Integer id) throws ApplicationServiceException{
		try {
			return folderRepository.findById(id).orElseThrow();
		} catch (Exception e) {
			throw new ApplicationServiceException("Could not find the folder", e);
		}
	}
	
	
	public void renameDir(String source, String target) throws ApplicationServiceException  {
		try {
			Files.move(Paths.get(source),Paths.get(target),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new ApplicationServiceException("Could not rename");
		}
	}
	
	public void deleteDir(String dir) throws ApplicationServiceException {
		try {
			IOUtil.deletePath(dir);			
		}catch(NoSuchFileException e){	
			throw new ApplicationServiceException(" is not exit",e);
		}catch(DirectoryNotEmptyException e){
			throw new ApplicationServiceException(" is not empty",e);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	public String getSourceDir() {
		return StringUtil.getPath(targetFolder);
	}
	
}
