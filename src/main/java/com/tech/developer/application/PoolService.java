package com.tech.developer.application;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Pool;
import com.tech.developer.domain.Staff;
import com.tech.developer.domain.repositories.PoolRepository;

@Service
public class PoolService {

	@Autowired
	private PoolRepository poolRepository;
	
	public Pool savePool(Pool pool) throws BusinessException {
		try {
			return poolRepository.save(pool);
		} catch (Exception e) {
			throw new BusinessException("Name was already in use, try again!",e);
		}
	}
		
	public Pool findPoolById(Integer id) throws ApplicationServiceException {
		try {
			return poolRepository.findById(id).orElseThrow();
		} catch (Exception e) {
			throw new ApplicationServiceException("Could not find the pool", e);
		}
	}
	
	public List<Pool> findSharedPools(Integer id){
		return poolRepository.findShareFPool(id);
	}
	
	
	@Transactional
	public Pool findPoolByFolder(Folder folder) {
		return poolRepository.findPoolByFolder(folder);
	}
	
	public List<Pool> findAllPoolByStaff(Staff staff){
		return poolRepository.findByStaff(staff);
	}
	
	public List<Pool> findPoolsByStaffId(Integer id){
		return poolRepository.findByStaff_Id(id);
	}
	
	
	public List<Pool> findAllGroupps(){
		return poolRepository.findAll(Sort.by("id"));
	}
	
	
	public void deletePool(Pool pool) {
		poolRepository.delete(pool);
	}
	
	
	
}
