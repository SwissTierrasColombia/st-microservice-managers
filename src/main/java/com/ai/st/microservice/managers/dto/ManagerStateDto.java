package com.ai.st.microservice.managers.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ManagerStateDto", description = "Manger State Dto")
public class ManagerStateDto implements Serializable {

    private static final long serialVersionUID = -1174020453923999718L;

    @ApiModelProperty(required = true, notes = "State ID")
    private Long id;

    @ApiModelProperty(required = true, notes = "State name")
    private String name;

    public ManagerStateDto() {

    }

    public ManagerStateDto(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
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

}
