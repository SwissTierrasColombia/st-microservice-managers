package com.ai.st.microservice.managers.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ManagerUserDto", description = "Manager User Dto")
public class ManagerUserDto implements Serializable {

	private static final long serialVersionUID = 130761780294600808L;

	@ApiModelProperty(required = true, notes = "User code")
	private Long userCode;

	private List<ManagerProfileDto> profiles;

	public ManagerUserDto() {
		this.profiles = new ArrayList<ManagerProfileDto>();
	}

	public Long getUserCode() {
		return userCode;
	}

	public void setUserCode(Long userCode) {
		this.userCode = userCode;
	}

	public List<ManagerProfileDto> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<ManagerProfileDto> profiles) {
		this.profiles = profiles;
	}

}
