package com.ai.st.microservice.managers.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UpdateManagerDto", description = "Update Manger Dto")
public class UpdateManagerDto implements Serializable {

	private static final long serialVersionUID = 8630363838327832666L;

	@ApiModelProperty(required = true, notes = "Manager ID")
	private Long id;

	@ApiModelProperty(required = true, notes = "Manager name")
	private String name;

	@ApiModelProperty(required = true, notes = "Manager tax identification number")
	private String taxIdentificationNumber;

	@ApiModelProperty(required = false, notes = "Manager Alias")
	private String alias;

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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}
