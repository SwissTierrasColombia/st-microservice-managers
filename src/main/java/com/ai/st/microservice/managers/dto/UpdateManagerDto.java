package com.ai.st.microservice.managers.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ManagerDto", description = "Create Manger Dto")
public class UpdateManagerDto implements Serializable {

	private static final long serialVersionUID = 8630363838327832666L;
	
	@ApiModelProperty(required = true, notes = "Manager ID")
	private Long id;

	@ApiModelProperty(required = true, notes = "Manager name")
	private String name;

	@ApiModelProperty(required = true, notes = "Manager tax identification number")
	private String taxIdentificationNumber;

	public UpdateManagerDto() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxIdentificationNumber() {
		return taxIdentificationNumber;
	}

	public void setTaxIdentificationNumber(String taxIdentificationNumber) {
		this.taxIdentificationNumber = taxIdentificationNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
