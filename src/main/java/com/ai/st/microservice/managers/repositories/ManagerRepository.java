package com.ai.st.microservice.managers.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ai.st.microservice.managers.entities.ManagerEntity;

public interface ManagerRepository extends CrudRepository<ManagerEntity, Long> {

    @Override
    List<ManagerEntity> findAll();

    @Query("SELECT m FROM ManagerEntity m WHERE m.managerState.id = :managerStateId")
    List<ManagerEntity> getManagersByStateId(@Param("managerStateId") Long managerStateId);

}
