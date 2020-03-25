package com.tech.developer.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Pool;
import com.tech.developer.domain.Staff;

public interface PoolRepository extends JpaRepository<Pool, Integer> {

	public List<Pool> findByStaff(Staff staff);
	
	public Pool findPoolByFolder(Folder folder);
	
	@Query("SELECT p FROM Pool p WHERE p.folder.id IS NULL AND p.staff.id = ?1")
	public List<Pool> findByStaff_Id (Integer poolId);
	
	@Query("SELECT p FROM Pool p INNER JOIN Folder f ON p.folder.id = f.id AND p.staff.id = ?1")
	public List<Pool> findShareFPool(Integer id);
	
	
}
