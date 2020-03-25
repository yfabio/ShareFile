package com.tech.developer.application;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.developer.domain.Sector;
import com.tech.developer.domain.repositories.SectorRepository;

@Service
public class SectorService {
	
	@Autowired
	private SectorRepository sectorRepository;
				
	public Sector saveSector(Sector sector)  {
		return sectorRepository.save(sector) ;
	}
	
	public Sector findSectorById(Integer id) throws ApplicationServiceException{
		try {
			return sectorRepository.findById(id).orElseThrow();
		} catch (NoSuchElementException e) {
			throw new ApplicationServiceException("Could not find the Sector!");
		}
	}
	
}
