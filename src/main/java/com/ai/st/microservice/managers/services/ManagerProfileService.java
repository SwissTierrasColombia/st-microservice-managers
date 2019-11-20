package com.ai.st.microservice.managers.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.managers.entities.ManagerProfileEntity;
import com.ai.st.microservice.managers.repositories.ManagerProfileRepository;

@Service
public class ManagerProfileService implements IManagerProfileService {

	@Autowired
	private ManagerProfileRepository managerProfileRepository;

	@Override
	@Transactional
	public ManagerProfileEntity createManagerProfile(ManagerProfileEntity managerProfileEntity) {
		return managerProfileRepository.save(managerProfileEntity);
	}

	@Override
	public Long getCount() {
		return managerProfileRepository.count();
	}

	@Override
	public ManagerProfileEntity getManagerProfileById(Long id) {
		return managerProfileRepository.findById(id).orElse(null);
	}

}
