package com.tech.developer.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tech.developer.domain.File;
import com.tech.developer.domain.Sector;

public interface SectorRepository extends JpaRepository<Sector, Integer> {

}
