package com.tech.developer.infrastructure.web.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.tech.developer.domain.FileType;

public class UploadValidator implements ConstraintValidator<UploadConstraint, MultipartFile> {

	private List<FileType> acceptedFileTypes;

	@Override
	public void initialize(UploadConstraint constraintAnnotation) {
		acceptedFileTypes = List.of(constraintAnnotation.acceptedType());
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		for (FileType fileType : acceptedFileTypes) {
			if(fileType.sameOf(value.getContentType())) {
				return true;
			}	
		}

		return false;
	}

}
