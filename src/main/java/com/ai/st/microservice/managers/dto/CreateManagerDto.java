package com.ai.st.microservice.managers.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "CreateManagerDto", description = "Create Manger Dto")
public class CreateManagerDto implements Serializable {

    private static final long serialVersionUID = 8630363838327832666L;

    @ApiModelProperty(required = true, notes = "Manager name")
    private String name;

    @ApiModelProperty(required = true, notes = "Manager tax identification number")
    private String taxIdentificationNumber;

    @ApiModelProperty(notes = "Manager Alias")
    private String alias;

    public CreateManagerDto() {

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "CreateManagerDto{" + "name='" + name + '\'' + ", taxIdentificationNumber='" + taxIdentificationNumber
                + '\'' + ", alias='" + alias + '\'' + '}';
    }
}
