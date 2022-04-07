package com.ai.st.microservice.managers.models.services;

import java.util.List;

import com.ai.st.microservice.managers.entities.ManagerStateEntity;

public interface IManagerStateService {

    ManagerStateEntity createManagerState(ManagerStateEntity managerState);

    List<ManagerStateEntity> getAllManagerStates();

    Long getCount();

    ManagerStateEntity getManagerStateById(Long id);

}
