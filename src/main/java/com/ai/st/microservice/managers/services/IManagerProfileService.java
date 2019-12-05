package com.ai.st.microservice.managers.services;

import java.util.List;

import com.ai.st.microservice.managers.entities.ManagerProfileEntity;

public interface IManagerProfileService {

	public ManagerProfileEntity createManagerProfile(ManagerProfileEntity managerProfileEntity);

	public Long getCount();

	public ManagerProfileEntity getManagerProfileById(Long id);

	public List<ManagerProfileEntity> getAllProfiles();

}
