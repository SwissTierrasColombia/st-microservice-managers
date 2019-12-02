package com.ai.st.microservice.managers.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AddUserToManagerDto", description = "Add User To Manager Dto")
public class AddUserToManagerDto implements Serializable {

	private static final long serialVersionUID = -7410047439578763438L;

	@ApiModelProperty(required = true, notes = "User code")
	private Long userCode;

	@ApiModelProperty(required = true, notes = "Manager ID")
	private Long managerId;

	@ApiModelProperty(required = true, notes = "Profile ID")
	private Long profileId;

	public AddUserToManagerDto() {

	}

	public Long getUserCode() {
		return userCode;
	}

	public void setUserCode(Long userCode) {
		this.userCode = userCode;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

}
