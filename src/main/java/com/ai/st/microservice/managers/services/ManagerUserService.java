package com.ai.st.microservice.managers.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.managers.entities.ManagerEntity;
import com.ai.st.microservice.managers.entities.ManagerProfileEntity;
import com.ai.st.microservice.managers.entities.ManagerUserEntity;
import com.ai.st.microservice.managers.repositories.ManagerUserRepository;

@Service
public class ManagerUserService implements IManagerUserService {

    @Autowired
    private ManagerUserRepository managerUserRepository;

    @Override
    @Transactional
    public ManagerUserEntity createManagerUser(ManagerUserEntity managerUserEntity) {
        return managerUserRepository.save(managerUserEntity);
    }

    @Override
    public List<ManagerUserEntity> getManagersUsersByUserCode(Long userCode) {
        return managerUserRepository.getUsersByUserCode(userCode);
    }

    @Override
    public ManagerUserEntity getManagerUserByUserCodeAndManagerAndProfile(Long userCode, ManagerEntity managerEntity,
            ManagerProfileEntity profileEntity) {
        return managerUserRepository.findByUserCodeAndManagerAndManagerProfile(userCode, managerEntity, profileEntity);
    }

    @Override
    public List<ManagerUserEntity> getManagersUsersByManagerAndProfiles(ManagerEntity managerEntity,
            List<ManagerProfileEntity> profiles) {
        return managerUserRepository.findByManagerAndManagerProfileIn(managerEntity, profiles);
    }

    @Override
    public List<ManagerUserEntity> getManagersUsersByManager(ManagerEntity managerEntity) {
        return managerUserRepository.findByManager(managerEntity);
    }

    @Override
    @Transactional
    public void deleteManagerUserById(Long id) {
        managerUserRepository.deleteById(id);
    }

}
