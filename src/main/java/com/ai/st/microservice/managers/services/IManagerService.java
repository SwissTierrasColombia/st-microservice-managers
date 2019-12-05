package com.ai.st.microservice.managers.services;

import java.util.List;

import com.ai.st.microservice.managers.entities.ManagerEntity;

public interface IManagerService {

	public ManagerEntity createManager(ManagerEntity manager);

	public Long getCount();

	public List<ManagerEntity> getAllManagers();

	public List<ManagerEntity> getManagersByStateId(Long managerStateId);

	public ManagerEntity getManagerById(Long id);

}
