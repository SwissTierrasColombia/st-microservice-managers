package com.ai.st.microservice.managers.services;

import java.util.List;

import com.ai.st.microservice.managers.entities.ManagerEntity;
import com.ai.st.microservice.managers.entities.ManagerProfileEntity;
import com.ai.st.microservice.managers.entities.ManagerUserEntity;

public interface IManagerUserService {

	public ManagerUserEntity createManagerUser(ManagerUserEntity managerUserEntity);

	public List<ManagerUserEntity> getManagersUsersByUserCode(Long userCode);

	public ManagerUserEntity getManagerUserByUserCodeAndManagerAndProfile(Long userCode,
			ManagerEntity managerUserEntity, ManagerProfileEntity profileEntity);

}
