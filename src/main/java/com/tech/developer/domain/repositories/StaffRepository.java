package com.tech.developer.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tech.developer.domain.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer>{
	
	public Staff findByEmail(String email);
	
	@Query("SELECT s FROM Staff s ORDER BY s.id")
	public List<Staff> findAllStaffs();
	
		
}
