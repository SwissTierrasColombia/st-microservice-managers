package com.ai.st.microservice.managers.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerProfileDto;
import com.ai.st.microservice.managers.dto.ManagerStateDto;
import com.ai.st.microservice.managers.dto.ManagerUserDto;
import com.ai.st.microservice.managers.entities.ManagerEntity;
import com.ai.st.microservice.managers.entities.ManagerProfileEntity;
import com.ai.st.microservice.managers.entities.ManagerUserEntity;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.services.IManagerProfileService;
import com.ai.st.microservice.managers.services.IManagerService;
import com.ai.st.microservice.managers.services.IManagerUserService;

@Component
public class ManagerBusiness {

	@Autowired
	private IManagerService managerService;

	@Autowired
	private IManagerUserService managerUserService;

	@Autowired
	private IManagerProfileService managerProfileService;

	public List<ManagerDto> getManagers(Long managerStateId) throws BusinessException {

		List<ManagerDto> listManagersDto = new ArrayList<ManagerDto>();

		List<ManagerEntity> listManagersEntity = new ArrayList<ManagerEntity>();

		if (managerStateId != null) {
			listManagersEntity = managerService.getManagersByStateId(managerStateId);
		} else {
			listManagersEntity = managerService.getAllManagers();
		}

		for (ManagerEntity managerEntity : listManagersEntity) {

			ManagerDto managerDto = new ManagerDto();
			managerDto.setId(managerEntity.getId());
			managerDto.setName(managerEntity.getName());
			managerDto.setTaxIdentificationNumber(managerEntity.getTaxIdentificationNumber());
			managerDto.setCreatedAt(managerEntity.getCreatedAt());
			managerDto.setManagerState(new ManagerStateDto(managerEntity.getManagerState().getId(),
					managerEntity.getManagerState().getName()));

			listManagersDto.add(managerDto);

		}

		return listManagersDto;
	}

	public ManagerDto getManagerById(Long id) throws BusinessException {

		ManagerDto managerDto = null;

		ManagerEntity managerEntity = managerService.getManagerById(id);
		if (managerEntity instanceof ManagerEntity) {
			managerDto = new ManagerDto();
			managerDto.setId(managerEntity.getId());
			managerDto.setName(managerEntity.getName());
			managerDto.setTaxIdentificationNumber(managerEntity.getTaxIdentificationNumber());
			managerDto.setCreatedAt(managerEntity.getCreatedAt());
			managerDto.setManagerState(new ManagerStateDto(managerEntity.getManagerState().getId(),
					managerEntity.getManagerState().getName()));
		}

		return managerDto;
	}

	public ManagerUserDto addUserToManager(Long userCode, Long managerId, Long profileId) throws BusinessException {

		// verify if manager does exits
		ManagerEntity managerEntity = managerService.getManagerById(managerId);
		if (!(managerEntity instanceof ManagerEntity)) {
			throw new BusinessException("El gestor no existe.");
		}

		// verify if profile does exists
		ManagerProfileEntity managerProfileEntity = managerProfileService.getManagerProfileById(profileId);
		if (!(managerProfileEntity instanceof ManagerProfileEntity)) {
			throw new BusinessException("El perfil no existe.");
		}

		ManagerUserEntity existsUser = managerUserService.getManagerUserByUserCodeAndManagerAndProfile(userCode,
				managerEntity, managerProfileEntity);
		if (existsUser instanceof ManagerUserEntity) {
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

		List<ManagerProfileDto> listProfiles = new ArrayList<ManagerProfileDto>();
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

		// verify if manager does exits
		ManagerEntity managerEntity = managerService.getManagerById(managerId);
		if (!(managerEntity instanceof ManagerEntity)) {
			throw new BusinessException("El gestor no existe.");
		}

		List<ManagerUserEntity> managersUsersEntity = null;

		if (profiles != null && profiles.size() > 0) {

			List<ManagerProfileEntity> profilesEntity = new ArrayList<>();
			for (Long profileId : profiles) {
				ManagerProfileEntity profileEntity = managerProfileService.getManagerProfileById(profileId);
				if (profileEntity instanceof ManagerProfileEntity) {
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
					.filter(managerUserDto -> managerUserDto.getUserCode() == managerUserEntity.getUserCode()).findAny()
					.orElse(null);

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

					if (managerUserDto.getUserCode() == managerUserEntity.getUserCode()) {
						managerUserDto.getProfiles().add(new ManagerProfileDto(managerProfileEntity.getId(),
								managerProfileEntity.getDescription(), managerProfileEntity.getName()));
					}

				}

			}

		}

		return listUsersDto;
	}

}
