package com.ai.st.microservice.managers.models.services;

import java.util.List;

import com.ai.st.microservice.managers.entities.ManagerEntity;

public interface IManagerService {

    ManagerEntity createManager(ManagerEntity manager);

    Long getCount();

    ManagerEntity updateManager(ManagerEntity manager);

    List<ManagerEntity> getAllManagers();

    List<ManagerEntity> getManagersByStateId(Long managerStateId);

    ManagerEntity getManagerById(Long id);

}
