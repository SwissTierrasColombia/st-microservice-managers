package com.ai.st.microservice.managers.services;

import java.util.List;

import com.ai.st.microservice.managers.entities.ManagerStateEntity;

public interface IManagerStateService {

    public ManagerStateEntity createManagerState(ManagerStateEntity managerState);

    public List<ManagerStateEntity> getAllManagerStates();

    public Long getCount();

    public ManagerStateEntity getManagerStateById(Long id);

}
