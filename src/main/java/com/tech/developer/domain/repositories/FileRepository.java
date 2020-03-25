package com.tech.developer.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tech.developer.domain.File;
import com.tech.developer.domain.Folder;

public interface FileRepository extends JpaRepository<File, Integer> {
	
	List<File> findAllByFolder(Folder folder);
	
	File findByFolder(Folder folder);
	
}
