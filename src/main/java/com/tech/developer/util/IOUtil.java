package com.tech.developer.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class IOUtil {
		
	public static void copy(InputStream in,String dir) throws IOException {
		Files.copy(in, Paths.get(dir), StandardCopyOption.REPLACE_EXISTING);				
	}
	
	public static void createFolder(String dir) throws IOException {
		Files.createDirectory(Paths.get(dir));
	}
	
	public static void deletePath(String dir) throws IOException  {
		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			throw e;
		}
	}
	
	public static byte[] getBytes(Path dir) throws IOException {
		return Files.readAllBytes(dir);
	}
	

}
