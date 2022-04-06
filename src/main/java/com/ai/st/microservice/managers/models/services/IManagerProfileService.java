package com.ai.st.microservice.managers.models.services;

import java.util.List;

import com.ai.st.microservice.managers.entities.ManagerProfileEntity;

public interface IManagerProfileService {

    ManagerProfileEntity createManagerProfile(ManagerProfileEntity managerProfileEntity);

    Long getCount();

    ManagerProfileEntity getManagerProfileById(Long id);

    List<ManagerProfileEntity> getAllProfiles();

    ManagerProfileEntity updateManagerProfile(ManagerProfileEntity managerProfileEntity);

    void deleteById(Long profileId);

}
