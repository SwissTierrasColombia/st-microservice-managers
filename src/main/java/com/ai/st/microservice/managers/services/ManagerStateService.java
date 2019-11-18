package com.ai.st.microservice.managers.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.managers.entities.ManagerStateEntity;
import com.ai.st.microservice.managers.repositories.ManagerStateRepository;

@Service
public class ManagerStateService implements IManagerStateService {

	@Autowired
	private ManagerStateRepository managerStateRepository;

	@Override
	@Transactional
	public ManagerStateEntity createManagerState(ManagerStateEntity managerState) {
		return managerStateRepository.save(managerState);
	}

	@Override
	public List<ManagerStateEntity> getAllManagerStates() {
		return managerStateRepository.findAll();
	}

	@Override
	public Long getCount() {
		return managerStateRepository.count();
	}

	@Override
	public ManagerStateEntity getManagerStateById(Long id) {
		return managerStateRepository.findById(id).orElse(null);
	}

}
