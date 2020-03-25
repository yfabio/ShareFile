package com.tech.developer.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Sector implements Serializable{

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer id;
	
	@NotBlank(message = "Job is required, please try again!")
	@Column(name = "cl_job",length = 40, nullable = false)
	private String job;
	
	@NotBlank(message = "Floor is required, please try again!")
	@Column(name = "cl_floor",length = 40, nullable = false)
	private String floor;
	
	@NotBlank(message = "Room is required, please try again!")
	@Column(name = "cl_room",length = 40, nullable = false)
	private String room;
	
	@NotBlank(message = "Desk is required, please try again!")
	@Column(name = "cl_desk",length = 40, nullable = false)
	private String desk;
	
	
	
}
