package com.tech.developer.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.tech.developer.infrastructure.web.validation.UploadConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class File implements Serializable{

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "Name is required, please try again")
	@Column(name = "cl_name", length = 40, nullable = false)
	private String name;
	
	@NotNull(message = "Folder is required, please try agin")
	@ManyToOne
	@JoinColumn(name = "folder_id")
	private Folder folder = new Folder();
	
	
	@UploadConstraint(acceptedType = {FileType.DOCX,FileType.XLSX,FileType.PDF,FileType.TXT,FileType.JPG,FileType.PNG},
	 message = "It wasn't a valid file, please try again")	
	private transient MultipartFile fileData;
	
	
	public void setExtension() {		
		if(!name.contains(".")) {
			name = name.concat(".").concat(FileType.of(fileData.getContentType()).extension);
		}		
	}
	
	
	
	
	
}
