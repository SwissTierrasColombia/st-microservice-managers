package com.ai.st.microservice.managers.models.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.managers.entities.ManagerProfileEntity;

public interface ManagerProfileRepository extends CrudRepository<ManagerProfileEntity, Long> {

    @Override
    List<ManagerProfileEntity> findAll();

}
