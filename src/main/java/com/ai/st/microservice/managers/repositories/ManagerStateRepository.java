package com.ai.st.microservice.managers.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.managers.entities.ManagerStateEntity;

public interface ManagerStateRepository extends CrudRepository<ManagerStateEntity, Long> {

	@Override
	List<ManagerStateEntity> findAll();

}
