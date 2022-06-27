package com.ai.st.microservice.managers.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ManagerDto", description = "Manger Dto")
public class ManagerDto implements Serializable {

    private static final long serialVersionUID = 8630363838327832665L;

    @ApiModelProperty(required = true, notes = "Manager ID")
    private Long id;

    @ApiModelProperty(required = true, notes = "Manager name")
    private String name;

    @ApiModelProperty(notes = "Manager Alias")
    private String alias;

    @ApiModelProperty(notes = "Sinic Group ID")
    private String groupId;

    @ApiModelProperty(required = true, notes = "Manager tax identification number")
    private String taxIdentificationNumber;

    @ApiModelProperty(required = true, notes = "Date creation")
    private Date createdAt;

    @ApiModelProperty(required = true, notes = "Manager State")
    private ManagerStateDto managerState;

    public ManagerDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ManagerStateDto getManagerState() {
        return managerState;
    }

    public void setManagerState(ManagerStateDto managerState) {
        this.managerState = managerState;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
