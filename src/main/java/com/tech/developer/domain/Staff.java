package com.tech.developer.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tech.developer.util.StringUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Staff implements Serializable, Comparable<Staff> {

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "Name is required, please try again!")
	@Column(name = "cl_name",length = 40, nullable = false)
	private String name;
	
	@NotBlank(message = "Email is required, please try again!")
	@Column(name = "cl_email",length = 40, nullable = false)
	private String email;
	
	@NotEmpty(message = "Phone is required, please try again!")
	@Column(name = "cl_phone",length = 40, nullable = false)
	private String phone;
	
	@NotBlank(message = "Password is required, please try again!")
	@Column(name = "cl_password",length = 80, nullable = false)
	private String password;
	
	@NotBlank(message = "ConfirmPassword is required, please try again!")	
	private transient String confirmPassword = "default";
	
	private transient String welcome;
	
	@Enumerated(EnumType.STRING)
	private Profile profile;
	
	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn(name = "sector_id")
	private Sector sector;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "staff")
	private List<Pool> pools = new ArrayList<>();
	
	@ToString.Exclude
	@OneToMany(mappedBy = "staff",fetch = FetchType.EAGER)
	private List<Folder> folders = new ArrayList<>();
	
	
	private transient Folder folder = new Folder(); 
	
	private transient Pool pool = new Pool();
			
	private transient List<Pool> groups = new ArrayList<>(); 
		
	private transient Integer maxFiles;
	
	private transient List<Staff> staffs = new ArrayList<>(); 

		
	public void encryptPassword() {
		this.password = StringUtil.encrypt(this.password);
	}
	
	
	public void getWelcomeMessage() {
		welcome = StringUtil.getWelcomeMessage(name);		
	}

	@Override
	public int compareTo(Staff obj) {
		return this.id == obj.id ? 0 : this.id > obj.id ? 1 : -1;
	}

	
	
	
}
