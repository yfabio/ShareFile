package com.tech.developer.domain.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Pool;
import com.tech.developer.domain.Staff;

public interface FolderRepository extends JpaRepository<Folder, Integer>{
	
	public List<Folder> findByStaff(Staff staff);

	public Folder findByName(String name);
	
	@Query("SELECT f FROM Folder f INNER JOIN Pool p ON p.folder.id = f.id AND p.staff.id = ?1")
	public List<Folder> findFoldersByStaffId(Integer id);
	
	@Query("SELECT f FROM Folder f  WHERE f.staff.id = ?1")
	public List<Folder> findAllFoldersFromStaffId(Integer id);
	
	
	
	
	
}
