package com.tech.developer.application;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Pool;
import com.tech.developer.domain.Staff;
import com.tech.developer.domain.repositories.FolderRepository;
import com.tech.developer.domain.repositories.StaffRepository;
import com.tech.developer.util.SecurityUtil;


@Service
public class StaffService {
	
	@Autowired
	private StaffRepository staffRepository;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private FileService fileService;
	
	

	public Staff saveStaff(Staff staff) throws BusinessException {
		try {		
			return staffRepository.save(staff);			
		} catch (Exception e) {
			throw new BusinessException("Email was already in use, try again!",e);
		}
	}
	
	
	public Staff findStaffByEmaill(String email) {
		return staffRepository.findByEmail(email);
	}
	
	public Staff findStaffById(Integer id) throws ApplicationServiceException {
		 try {
			return staffRepository.findById(id).orElseThrow();
		} catch (Exception e) {
			throw new ApplicationServiceException("Could not find the Staff");
		}
		 
	}
	
	public List<Staff> getAllStaffs() {
		final Staff staff = SecurityUtil.loggedStaff();
		List<Staff> staffs = staffRepository.findAll();		
		return staffs.stream().filter(e -> !e.getId().equals(staff.getId())).sorted().collect(Collectors.toList());
	}
	
	
	public int getAllFiles(Staff staff) {		
		List<Folder> folders = folderService.findAllFoldersByStaff(staff);
		int max = 0;
		for (Folder folder : folders) {
			max+= fileService.findFilesByFolder(folder).size();
		}
		return max;		
	}


	public List<Staff> findAllStaffs() {
		return staffRepository.findAllStaffs();	
	}
	
	
	
	
	
	
	
	

}
