package com.ai.st.microservice.managers.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ai.st.microservice.managers.services.tracing.SCMTracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerProfileDto;
import com.ai.st.microservice.managers.dto.ManagerStateDto;
import com.ai.st.microservice.managers.dto.ManagerUserDto;
import com.ai.st.microservice.managers.entities.ManagerEntity;
import com.ai.st.microservice.managers.entities.ManagerProfileEntity;
import com.ai.st.microservice.managers.entities.ManagerStateEntity;
import com.ai.st.microservice.managers.entities.ManagerUserEntity;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.models.services.IManagerProfileService;
import com.ai.st.microservice.managers.models.services.IManagerService;
import com.ai.st.microservice.managers.models.services.IManagerStateService;
import com.ai.st.microservice.managers.models.services.IManagerUserService;

@Component
public class ManagerBusiness {

    private final Logger log = LoggerFactory.getLogger(ManagerBusiness.class);

    @Autowired
    private IManagerService managerService;

    @Autowired
    private IManagerUserService managerUserService;

    @Autowired
    private IManagerProfileService managerProfileService;

    @Autowired
    private IManagerStateService managerStateService;

    public List<ManagerDto> getManagers(Long managerStateId) throws BusinessException {

        List<ManagerDto> listManagersDto = new ArrayList<>();

        List<ManagerEntity> listManagersEntity;

        if (managerStateId != null) {
            listManagersEntity = managerService.getManagersByStateId(managerStateId);
        } else {
            listManagersEntity = managerService.getAllManagers();
        }

        for (ManagerEntity managerEntity : listManagersEntity) {
            ManagerDto managerDto = transformEntityToDto(managerEntity);
            listManagersDto.add(managerDto);
        }

        return listManagersDto;
    }

    public ManagerDto getManagerById(Long id) throws BusinessException {

        ManagerDto managerDto = null;

        ManagerEntity managerEntity = managerService.getManagerById(id);
        if (managerEntity != null) {
            managerDto = transformEntityToDto(managerEntity);
        }

        return managerDto;
    }

    public ManagerUserDto addUserToManager(Long userCode, Long managerId, Long profileId) throws BusinessException {

        // verify if manager does exist
        ManagerEntity managerEntity = managerService.getManagerById(managerId);
        if (managerEntity == null) {
            throw new BusinessException("El gestor no existe.");
        }

        // verify if profile does exists
        ManagerProfileEntity managerProfileEntity = managerProfileService.getManagerProfileById(profileId);
        if (managerProfileEntity == null) {
            throw new BusinessException("El perfil no existe.");
        }

        ManagerUserEntity existsUser = managerUserService.getManagerUserByUserCodeAndManagerAndProfile(userCode,
                managerEntity, managerProfileEntity);
        if (existsUser != null) {
            throw new BusinessException("El usuario ya se encuentra registrado.");
        }

        ManagerUserEntity managerUserEntity = new ManagerUserEntity();
        managerUserEntity.setCreatedAt(new Date());
        managerUserEntity.setManager(managerEntity);
        managerUserEntity.setManagerProfile(managerProfileEntity);
        managerUserEntity.setUserCode(userCode);

        managerUserEntity = managerUserService.createManagerUser(managerUserEntity);

        ManagerUserDto managerUserDto = new ManagerUserDto();
        managerUserDto.setUserCode(managerUserEntity.getUserCode());

        List<ManagerProfileDto> listProfiles = new ArrayList<>();
        List<ManagerUserEntity> list = managerUserService.getManagersUsersByUserCode(managerUserEntity.getUserCode());
        for (ManagerUserEntity muEntity : list) {
            ManagerProfileEntity managerProfile = muEntity.getManagerProfile();
            ManagerProfileDto managerProfileDto = new ManagerProfileDto();
            managerProfileDto.setDescription(managerProfile.getDescription());
            managerProfileDto.setId(managerProfile.getId());
            managerProfileDto.setName(managerProfile.getName());
            listProfiles.add(managerProfileDto);
        }
        managerUserDto.setProfiles(listProfiles);

        return managerUserDto;
    }

    public List<ManagerUserDto> getUsersByManager(Long managerId, List<Long> profiles) throws BusinessException {

        List<ManagerUserDto> listUsersDto = new ArrayList<>();

        // verify if manager does exist
        ManagerEntity managerEntity = managerService.getManagerById(managerId);
        if (managerEntity == null) {
            throw new BusinessException("El gestor no existe.");
        }

        List<ManagerUserEntity> managersUsersEntity;

        if (profiles != null && profiles.size() > 0) {

            List<ManagerProfileEntity> profilesEntity = new ArrayList<>();
            for (Long profileId : profiles) {
                ManagerProfileEntity profileEntity = managerProfileService.getManagerProfileById(profileId);
                if (profileEntity != null) {
                    profilesEntity.add(profileEntity);
                }
            }

            managersUsersEntity = managerUserService.getManagersUsersByManagerAndProfiles(managerEntity,
                    profilesEntity);

        } else {
            managersUsersEntity = managerUserService.getManagersUsersByManager(managerEntity);
        }

        for (ManagerUserEntity managerUserEntity : managersUsersEntity) {

            ManagerUserDto managerUserDtoFound = listUsersDto.stream()
                    .filter(managerUserDto -> managerUserDto.getUserCode().equals(managerUserEntity.getUserCode()))
                    .findAny().orElse(null);

            ManagerProfileEntity managerProfileEntity = managerUserEntity.getManagerProfile();

            if (managerUserDtoFound == null) {

                ManagerUserDto managerUserDto = new ManagerUserDto();
                managerUserDto.setUserCode(managerUserEntity.getUserCode());

                List<ManagerProfileDto> profilesDto = new ArrayList<>();
                profilesDto.add(new ManagerProfileDto(managerProfileEntity.getId(),
                        managerProfileEntity.getDescription(), managerProfileEntity.getName()));
                managerUserDto.setProfiles(profilesDto);

                listUsersDto.add(managerUserDto);
            } else {

                for (ManagerUserDto managerUserDto : listUsersDto) {

                    if (managerUserDto.getUserCode().equals(managerUserEntity.getUserCode())) {
                        managerUserDto.getProfiles().add(new ManagerProfileDto(managerProfileEntity.getId(),
                                managerProfileEntity.getDescription(), managerProfileEntity.getName()));
                    }

                }

            }

        }

        return listUsersDto;
    }

    public ManagerDto addManager(String name, String taxIdentification, String alias) throws BusinessException {

        if (name.isEmpty()) {
            throw new BusinessException("El gestor debe contener un nombre.");
        }

        if (taxIdentification.isEmpty()) {
            throw new BusinessException("El gestor debe contener un identificador de impuestos.");
        }

        ManagerStateEntity managerState = managerStateService
                .getManagerStateById(ManagerStateBusiness.MANAGER_STATE_ACTIVE);

        ManagerEntity managerEntity = new ManagerEntity();

        managerEntity.setName(name.toUpperCase());
        managerEntity.setCreatedAt(new Date());
        managerEntity.setTaxIdentificationNumber(taxIdentification);
        managerEntity.setManagerState(managerState);

        if (alias != null) {
            managerEntity.setAlias(alias);
        }

        managerEntity = managerService.createManager(managerEntity);

        return this.transformEntityToDto(managerEntity);
    }

    protected ManagerDto transformEntityToDto(ManagerEntity managerEntity) {

        ManagerDto managerDto = new ManagerDto();
        managerDto.setId(managerEntity.getId());
        managerDto.setAlias(managerEntity.getAlias());
        managerDto.setCreatedAt(managerEntity.getCreatedAt());
        managerDto.setName(managerEntity.getName());
        managerDto.setTaxIdentificationNumber(managerEntity.getTaxIdentificationNumber());

        ManagerStateDto managerStateDto = new ManagerStateDto();
        managerStateDto.setId(managerEntity.getManagerState().getId());
        managerStateDto.setName(managerEntity.getManagerState().getName());

        managerDto.setManagerState(managerStateDto);

        return managerDto;
    }

    public ManagerDto activateManager(Long managerId) throws BusinessException {

        ManagerDto managerDto;

        // verify manager exists
        ManagerEntity managerEntity = managerService.getManagerById(managerId);
        if (managerEntity == null) {
            throw new BusinessException("El gestor no existe.");
        }

        // set manager state
        ManagerStateEntity managerStateEntity = managerStateService
                .getManagerStateById(ManagerStateBusiness.MANAGER_STATE_ACTIVE);
        if (managerStateEntity == null) {
            throw new BusinessException("El estado no existe.");
        }

        managerEntity.setManagerState(managerStateEntity);

        try {
            ManagerEntity managerUpdatedEntity = managerService.updateManager(managerEntity);
            managerDto = this.transformEntityToDto(managerUpdatedEntity);
        } catch (Exception e) {
            String messageError = String.format("Error activando el gestor %d : %s", managerId, e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new BusinessException("No se ha podido activar el gestor.");
        }

        return managerDto;
    }

    public ManagerDto deactivateManager(Long managerId) throws BusinessException {

        ManagerDto managerDto;

        // verify manager exists
        ManagerEntity managerEntity = managerService.getManagerById(managerId);
        if (managerEntity == null) {
            throw new BusinessException("El gestor no existe.");
        }

        // set manager state
        ManagerStateEntity managerStateEntity = managerStateService
                .getManagerStateById(ManagerStateBusiness.MANAGER_STATE_INACTIVE);
        if (managerStateEntity == null) {
            throw new BusinessException("El estado no existe.");
        }

        managerEntity.setManagerState(managerStateEntity);

        try {
            ManagerEntity managerUpdatedEntity = managerService.updateManager(managerEntity);
            managerDto = this.transformEntityToDto(managerUpdatedEntity);
        } catch (Exception e) {
            String messageError = String.format("Error desactivando el gestor %d : %s", managerId, e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new BusinessException("No se ha podido desactivar el gestor.");
        }

        return managerDto;
    }

    public ManagerDto updateManager(Long managerId, String name, String taxIdentification, String alias)
            throws BusinessException {

        if (managerId <= 0) {
            throw new BusinessException("El gestor debe contener un id.");
        }

        if (name.isEmpty()) {
            throw new BusinessException("El gestor debe contener un nombre.");
        }

        if (taxIdentification.isEmpty()) {
            throw new BusinessException("El gestor debe contener un identificador de impuestos.");
        }

        // verify manager exists
        ManagerEntity managerEntity = managerService.getManagerById(managerId);
        if (managerEntity == null) {
            throw new BusinessException("El gestor no existe.");
        }

        managerEntity.setName(name.toUpperCase());
        managerEntity.setCreatedAt(new Date());
        managerEntity.setTaxIdentificationNumber(taxIdentification);

        if (alias != null) {
            managerEntity.setAlias(alias);
        } else {
            managerEntity.setAlias(null);
        }

        managerEntity = managerService.updateManager(managerEntity);

        return this.transformEntityToDto(managerEntity);
    }

    public ManagerUserDto removeUserToManager(Long userCode, Long managerId, Long profileId) throws BusinessException {

        // verify if manager does exist
        ManagerEntity managerEntity = managerService.getManagerById(managerId);
        if (managerEntity == null) {
            throw new BusinessException("El gestor no existe.");
        }

        // verify if profile does exists
        ManagerProfileEntity managerProfileEntity = managerProfileService.getManagerProfileById(profileId);
        if (managerProfileEntity == null) {
            throw new BusinessException("El perfil no existe.");
        }

        ManagerUserEntity existsUser = managerUserService.getManagerUserByUserCodeAndManagerAndProfile(userCode,
                managerEntity, managerProfileEntity);
        if (existsUser == null) {
            throw new BusinessException("El usuario no tiene asignado el perfil.");
        }

        List<ManagerUserEntity> profilesUser = managerUserService.getManagersUsersByUserCode(userCode);
        if (profilesUser.size() <= 1) {
            throw new BusinessException("No se puede quitar el perfil al usuario porque es el único que tiene.");
        }

        managerUserService.deleteManagerUserById(existsUser.getId());

        ManagerUserDto managerUserDto = new ManagerUserDto();
        managerUserDto.setUserCode(userCode);

        List<ManagerProfileDto> listProfiles = new ArrayList<>();
        List<ManagerUserEntity> list = managerUserService.getManagersUsersByUserCode(userCode);
        for (ManagerUserEntity muEntity : list) {
            ManagerProfileEntity managerProfile = muEntity.getManagerProfile();
            ManagerProfileDto managerProfileDto = new ManagerProfileDto();
            managerProfileDto.setDescription(managerProfile.getDescription());
            managerProfileDto.setId(managerProfile.getId());
            managerProfileDto.setName(managerProfile.getName());
            listProfiles.add(managerProfileDto);
        }
        managerUserDto.setProfiles(listProfiles);

        return managerUserDto;

    }

}
