package com.ai.st.microservice.managers.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ManagerProfileDto", description = "Manager Profile Dto")
public class ManagerProfileDto implements Serializable {

	private static final long serialVersionUID = 6415729231877595623L;

	@ApiModelProperty(required = true, notes = "Profile ID")
	private Long id;

	@ApiModelProperty(required = true, notes = "Description")
	private String description;

	@ApiModelProperty(required = true, notes = "Name")
	private String name;

	public ManagerProfileDto() {

	}

	public ManagerProfileDto(Long id, String description, String name) {
		this.id = id;
		this.description = description;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
