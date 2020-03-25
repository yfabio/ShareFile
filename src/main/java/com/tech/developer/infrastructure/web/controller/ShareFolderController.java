package com.tech.developer.infrastructure.web.controller;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tech.developer.application.ApplicationServiceException;
import com.tech.developer.application.FileService;
import com.tech.developer.application.FolderService;
import com.tech.developer.application.PoolService;
import com.tech.developer.application.StaffService;
import com.tech.developer.domain.File;
import com.tech.developer.domain.Folder;
import com.tech.developer.domain.Pool;
import com.tech.developer.domain.Staff;
import com.tech.developer.util.SecurityUtil;
import com.tech.developer.util.StringUtil;

@Controller
@RequestMapping(path = "/staff")
public class ShareFolderController {

	@Autowired
	private PoolService poolService;

	@Autowired
	private StaffService staffService;

	@Autowired
	private FileService fileService;

	@Autowired
	private FolderService folderService;

	@GetMapping(path = "/user/sharedfolder")
	public String shareFolder(Model model) {
		starter(model, SecurityUtil.loggedStaff());
		return "sharedfolder";
	}

	private void starter(Model model, Staff staff) {
		staff = staffService.findStaffById(staff.getId());
		List<Pool> groups = getSharedFoldersFromPool(staff);
		staff.setGroups(groups);
		model.addAttribute("staff", staff);
	}

	@PostMapping(path = "/user/folders/share")
	public String update(@ModelAttribute("staff") @Valid Staff staff, Errors errors, Model model) {
		return forward(staff, errors, model);
	}
	
	
	@GetMapping(path = "/user/folders/delete")
	public String delete(@RequestParam Optional<String> id, Model model) {
		
		File file = null;
		
		try {
			
			String sourceDir = folderService.getSourceDir();
			
			String fileID = id.orElseThrow();
			
			file = fileService.findFileByID(Integer.parseInt(fileID));
			
			String extension = file.getName().substring(file.getName().lastIndexOf(".")+1);
			
			String filePatternDir = StringUtil.fileFormatPattern(file.getId(), extension);
			
			Path target = Paths.get(sourceDir);
			
			
			try(Stream<Path> tree = Files.walk(target)){
				
				Iterator<Path> iter = tree.iterator();
				
				while (iter.hasNext()) {
					
					Path path = iter.next();
					
					String fileName =  path.toAbsolutePath().getFileName().toString();
					
					if(fileName.equals(filePatternDir)) {						
						folderService.deleteDir(path.toString());
						fileService.deleteFile(file);	
						break;
					}	
					
				}
				
			}
			
			model.addAttribute("msg", "File has been deleted successfully");
			starter(model, SecurityUtil.loggedStaff());
			
		} catch (ApplicationServiceException | IOException e) {
			starter(model, SecurityUtil.loggedStaff());
			model.addAttribute("error", StringUtil.getFullPath("The ", file.getName(), e.getMessage()));
		}
		
		return "sharedfolder";   
	}
	

	private String forward(@Valid Staff staff, Errors errors, Model model) {

		try {

			Pool pool = staff.getPool();

			pool = poolService.findPoolById(pool.getId());

			Folder folder = pool.getFolder();

			List<File> files = fileService.findFilesByFolder(folder);

			folder.setFiles(files);
			pool.setFolder(folder);

			String folderPatternDir = StringUtil.dirFormatPattern(folder.getId(), folder.getName());

			pool.setFolderPatternDir(folderPatternDir);

			model.addAttribute("pool", pool);
			starter(model, SecurityUtil.loggedStaff());

		} catch (ApplicationServiceException e) {
			starter(model, SecurityUtil.loggedStaff());
			model.addAttribute("error", e.getMessage());
		}

		return "sharedfolder";
	}

	private List<Pool> getSharedFoldersFromPool(Staff staff) {

		List<Pool> groups = new ArrayList<>();

		try {

			List<Pool> pools = getPools();

			for (Pool pool : pools) {

				if (pool != null) {

					Folder folder = pool.getFolder();

					if (folder != null) {

						List<String> names = pool.getNames();

						for (String name : names) {

							if (name.equals(staff.getName())) {
								groups.add(pool);
								break;
							}

						}

					}

				}

			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return groups;

	}

	public List<Pool> getPools() {

		List<Pool> pools = new ArrayList<>();

		try {

			List<Folder> folders = folderService.findAll();

			for (Folder folder : folders) {

				Pool pool = poolService.findPoolByFolder(folder);

				if (pool != null) {
					pools.add(pool);
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return pools;

	}

}
