package com.ai.st.microservice.managers.models.services;

import java.util.List;

import com.ai.st.microservice.managers.entities.ManagerEntity;
import com.ai.st.microservice.managers.entities.ManagerProfileEntity;
import com.ai.st.microservice.managers.entities.ManagerUserEntity;

public interface IManagerUserService {

    ManagerUserEntity createManagerUser(ManagerUserEntity managerUserEntity);

    List<ManagerUserEntity> getManagersUsersByUserCode(Long userCode);

    ManagerUserEntity getManagerUserByUserCodeAndManagerAndProfile(Long userCode, ManagerEntity managerEntity,
            ManagerProfileEntity profileEntity);

    List<ManagerUserEntity> getManagersUsersByManagerAndProfiles(ManagerEntity managerEntity,
            List<ManagerProfileEntity> profiles);

    List<ManagerUserEntity> getManagersUsersByManager(ManagerEntity managerEntity);

    void deleteManagerUserById(Long id);

}
