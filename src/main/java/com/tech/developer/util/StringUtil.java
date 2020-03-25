package com.tech.developer.util;

import java.nio.file.FileSystems;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mysql.cj.protocol.Message;
import com.tech.developer.domain.Profile;

public class StringUtil {
	
	private static final String SOURCE = System.getProperty("user.home");
	
	public static boolean isEmpty(String text) {
		return text == null ? true : text.trim().length() == 0 ? true:false; 
	}
	
	public static String encrypt(String password) {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return encoder.encode(password);
	}	
	
	public static String fileFormatPattern(Integer id, String extension) {
		return String.format("%04d-file.%s",id,extension);
	}
	
	public static String dirFormatPattern(Integer id,String folderName) {
		return String.format("%04d-%s",id,folderName);
	}
	
	
	public static String getWelcomeMessage(String username) {
		DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
		df = df.localizedBy(Locale.CANADA);
		String today = df.format(LocalDate.now());		
		return MessageFormat.format("Welcome {0}! {1}",username,today);
	}
	
	
	
	
	
	public static String getPath(String dir) {
		return SOURCE.concat(dir);
	}
	
	
	

	public static String getFullPath(String...values) {
		StringBuilder sb = new StringBuilder();
		for (String value : values) {
			sb.append(value);
		}
		return sb.toString();
	}
	
	
	
	
	public static String getSeparator() {
		return FileSystems.getDefault().getSeparator();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
