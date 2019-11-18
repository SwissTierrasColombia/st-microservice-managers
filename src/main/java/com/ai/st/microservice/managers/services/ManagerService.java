package com.ai.st.microservice.managers.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.managers.entities.ManagerEntity;
import com.ai.st.microservice.managers.repositories.ManagerRepository;

@Service
public class ManagerService implements IManagerService {

	@Autowired
	private ManagerRepository managerRepository;

	@Override
	@Transactional
	public ManagerEntity createManager(ManagerEntity manager) {
		return managerRepository.save(manager);
	}

	@Override
	public Long getCount() {
		return managerRepository.count();
	}

	@Override
	public List<ManagerEntity> getAllManagers() {
		return managerRepository.findAll();
	}

	@Override
	public List<ManagerEntity> getManagersByStateId(Long managerStateId) {
		return managerRepository.getManagersByStateId(managerStateId);
	}

}
