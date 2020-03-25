package com.tech.developer.util;

public class Constants {
	
	public static String TEMP_VALUE = "volatile"; 

	public static enum Level {
		DEBUG,WARNING,ERROR,
	}
	
	public static class Log {
		
		public static void d(String text) {
			String message  = String.format("%s: %s",Level.DEBUG.name(),text);
			System.err.println(message);
		}
		
		public static void w(String text) {
			String message  = String.format("%s: %s",Level.WARNING.name(),text);
			System.err.println(message);
		}
		
		public static void e(String text) {
			String message  = String.format("%s: %s",Level.ERROR.name(),text);
			System.err.println(message);
		}
		
		
	}
	
	
	
	
	
	
}
