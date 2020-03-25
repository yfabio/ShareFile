package com.tech.developer.application;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tech.developer.util.IOUtil;

@Service
public class FileUploadService {
	
	private static final String SOURCE = System.getProperty("user.home"); 
	
	@Value("${com.tech.developer.path.target}")
	private String target;
		
	public void uploadFile(MultipartFile multipartFile, String fileName)  {
		try {
			IOUtil.copy(multipartFile.getInputStream(), fileName);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	
	public byte[] getBytes(String folder, String fileName) throws IOException {		
		return IOUtil.getBytes(Paths.get(folder, fileName));
	}
	
	
}
