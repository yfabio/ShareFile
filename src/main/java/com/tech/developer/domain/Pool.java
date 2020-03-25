package com.tech.developer.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pool implements Serializable {

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "Name is required!")
	@Column(name = "cl_name", length = 40,unique = true,nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "folder_id")
	private Folder folder;
	
	@ManyToOne
	@JoinColumn(name = "staff_id")
	private Staff staff;
	
	@NotNull(message = "Permission is required")
	@Enumerated(EnumType.STRING)
	private Permission permission;
	
	@Size(min = 1,max = 5,message = "It is required at least 1")
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> names = new ArrayList<>();
	
	
	private transient String folderPatternDir;
	
}
