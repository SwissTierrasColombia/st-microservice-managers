package com.ai.st.microservice.managers.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.managers.entities.ManagerUserEntity;

public interface ManagerUserRepository extends CrudRepository<ManagerUserEntity, Long> {

	List<ManagerUserEntity> getUsersByUserCode(Long userCode);

}
