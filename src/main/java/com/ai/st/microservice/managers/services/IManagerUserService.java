package com.ai.st.microservice.managers.services;

import java.util.List;

import com.ai.st.microservice.managers.entities.ManagerUserEntity;

public interface IManagerUserService {

	public ManagerUserEntity createManagerUser(ManagerUserEntity managerUserEntity);

	public List<ManagerUserEntity> getManagersUsersByUserCode(Long userCode);

}
