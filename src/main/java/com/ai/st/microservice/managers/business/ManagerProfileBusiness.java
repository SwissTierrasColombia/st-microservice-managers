package com.ai.st.microservice.managers.business;

import java.util.ArrayList;
import java.util.List;

import com.ai.st.microservice.managers.services.tracing.SCMTracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.managers.dto.ManagerProfileDto;
import com.ai.st.microservice.managers.entities.ManagerProfileEntity;
import com.ai.st.microservice.managers.entities.ManagerUserEntity;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.services.IManagerProfileService;
import com.ai.st.microservice.managers.services.IManagerUserService;

@Component
public class ManagerProfileBusiness {

    private final Logger log = LoggerFactory.getLogger(ManagerProfileBusiness.class);

    @Autowired
    private IManagerProfileService managerProfileService;

    @Autowired
    private IManagerUserService managerUserService;

    public static final Long MANAGER_PROFILE_DIRECTOR = (long) 1;
    public static final Long MANAGER_PROFILE_SINIC = (long) 2;

    public List<ManagerProfileDto> getProfiles() throws BusinessException {

        List<ManagerProfileDto> profilesDto = new ArrayList<>();

        List<ManagerProfileEntity> profilesEntity = managerProfileService.getAllProfiles();

        for (ManagerProfileEntity profileEntity : profilesEntity) {

            ManagerProfileDto profileDto = new ManagerProfileDto();
            profileDto.setDescription(profileEntity.getDescription());
            profileDto.setId(profileEntity.getId());
            profileDto.setName(profileEntity.getName());

            profilesDto.add(profileDto);
        }

        return profilesDto;
    }

    public List<ManagerProfileDto> getProfilesByUser(Long userCode) throws BusinessException {

        List<ManagerProfileDto> profilesDto = new ArrayList<>();

        List<ManagerUserEntity> listManagerUsers = managerUserService.getManagersUsersByUserCode(userCode);

        for (ManagerUserEntity managerUser : listManagerUsers) {

            ManagerProfileEntity profileEntity = managerUser.getManagerProfile();

            ManagerProfileDto profileDto = new ManagerProfileDto();
            profileDto.setDescription(profileEntity.getDescription());
            profileDto.setId(profileEntity.getId());
            profileDto.setName(profileEntity.getName());

            profilesDto.add(profileDto);
        }

        return profilesDto;
    }

    public ManagerProfileDto addManagerProfile(String managerProfileName, String description) throws BusinessException {

        if (managerProfileName.isEmpty()) {
            throw new BusinessException("El perfil de gestor debe contener un nombre.");
        }

        if (description.isEmpty()) {
            throw new BusinessException("El perfil de gestor debe contener una descripción.");
        }

        ManagerProfileEntity managerProfileEntity = new ManagerProfileEntity();

        managerProfileEntity.setName(managerProfileName);
        managerProfileEntity.setDescription(description);

        managerProfileEntity = managerProfileService.createManagerProfile(managerProfileEntity);

        return this.transformEntityToDto(managerProfileEntity);
    }

    protected ManagerProfileDto transformEntityToDto(ManagerProfileEntity managerProfileEntity) {

        ManagerProfileDto managerProfileDto = new ManagerProfileDto();

        managerProfileDto.setId(managerProfileEntity.getId());
        managerProfileDto.setName(managerProfileEntity.getName());
        managerProfileDto.setDescription(managerProfileEntity.getDescription());

        return managerProfileDto;
    }

    public void deleteManagerProfile(Long profileId) throws BusinessException {

        // verify manager profile exists
        ManagerProfileEntity managerProfileEntity = managerProfileService.getManagerProfileById(profileId);
        if (managerProfileEntity == null) {
            throw new BusinessException("Perfil no encontrado.");
        }

        try {
            managerProfileService.deleteById(profileId);
        } catch (Exception e) {
            String messageError = String.format("Error eliminando el perfil %d: %s", profileId, e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new BusinessException("Error eliminando el perfil.");
        }
    }

    public ManagerProfileDto updateManagerProfile(Long managerProfileId, String managerProfileName, String description)
            throws BusinessException {

        if (managerProfileId <= 0) {
            throw new BusinessException("El perfil de gestor debe contener un id.");
        }

        if (managerProfileName.isEmpty()) {
            throw new BusinessException("El perfil de gestor debe contener un nombre.");
        }

        if (description.isEmpty()) {
            throw new BusinessException("El perfil de gestor debe contener una descripción.");
        }

        // verify manager profile exists
        ManagerProfileEntity managerProfileEntity = managerProfileService.getManagerProfileById(managerProfileId);
        if (managerProfileEntity == null) {
            throw new BusinessException("Perfil no encontrado.");
        }

        managerProfileEntity.setName(managerProfileName);
        managerProfileEntity.setDescription(description);

        managerProfileEntity = managerProfileService.updateManagerProfile(managerProfileEntity);

        return this.transformEntityToDto(managerProfileEntity);
    }

}
