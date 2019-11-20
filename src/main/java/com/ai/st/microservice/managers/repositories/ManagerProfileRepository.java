package com.ai.st.microservice.managers.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.managers.entities.ManagerProfileEntity;

public interface ManagerProfileRepository extends CrudRepository<ManagerProfileEntity, Long> {

}
