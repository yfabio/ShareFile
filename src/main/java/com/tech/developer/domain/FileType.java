package com.tech.developer.domain;

public enum FileType {
	
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document","docx"),
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","xlsx"),
	PPT("application/vnd.ms-powerpoint","ppt"),
	TXT("text/plain","txt"),
	PDF("application/pdf","pdf"),
	PNG("image/png","png"),
	JPG("image/jpeg","jpg");
	
	String mineType;
	String extension;
	
	FileType(String mineType,String extension){
		this.mineType = mineType;
		this.extension = extension;
	}

	public String getMineType() {
		return mineType;
	}

	public String getExtension() {
		return extension;
	}
	
	public boolean sameOf(String mineType) {
		return this.mineType.equalsIgnoreCase(mineType);
	}
	
	public static FileType of(String mineType) {
		for(FileType fileType : values()) {
			if(fileType.sameOf(mineType)) {
				return fileType;
			}
		}
		return null;
	}
	
}
