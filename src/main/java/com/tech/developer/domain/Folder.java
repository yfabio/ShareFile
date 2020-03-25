package com.tech.developer.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Folder implements Serializable {

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "Name is required, please try again!")
	@Column(name = "cl_name",length = 40, nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "staff_id")
	private Staff staff;
	
	@OneToMany(mappedBy = "folder",cascade = CascadeType.REMOVE)
	private List<File> files = new ArrayList<>();
	
	@OneToMany(mappedBy = "folder")	
	private List<Pool> pools = new ArrayList<>();
	 
	
	private transient String folderPatternDir;
	
	
	
}
