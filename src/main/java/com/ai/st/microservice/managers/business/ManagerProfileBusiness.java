package com.ai.st.microservice.managers.business;

import java.util.ArrayList;
import java.util.List;

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

	@Autowired
	private IManagerProfileService managerProfileService;
	
	@Autowired
	private IManagerUserService managerUserService;

	public static final Long MANAGER_PROFILE_DIRECTOR = (long) 1;
	public static final Long MANAGER_PROFILE_SINIC = (long) 2;

	public List<ManagerProfileDto> getProfiles() throws BusinessException {

		List<ManagerProfileDto> profilesDto = new ArrayList<ManagerProfileDto>();

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

		List<ManagerProfileDto> profilesDto = new ArrayList<ManagerProfileDto>();
		
		List<ManagerUserEntity> listManagerUsers =  managerUserService.getManagersUsersByUserCode(userCode);
		
		for (ManagerUserEntity managerUser: listManagerUsers) {
			
			ManagerProfileEntity profileEntity =  managerUser.getManagerProfile();
			
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

		ManagerProfileDto managerProfileDto = this.transformEntityToDto(managerProfileEntity);

		return managerProfileDto;
	}

	protected ManagerProfileDto transformEntityToDto(ManagerProfileEntity managerProfileEntity) {

		ManagerProfileDto managerProfileDto = new ManagerProfileDto();
		
		managerProfileDto.setId(managerProfileEntity.getId());
		managerProfileDto.setName(managerProfileEntity.getName());
		managerProfileDto.setDescription(managerProfileEntity.getDescription());
		
		return managerProfileDto;
	}

	public ManagerProfileDto deleteManagerProfile(Long profileId) throws BusinessException {

		ManagerProfileDto managerProfileDto = null;

		// verify manager profile exists
		ManagerProfileEntity managerProfileEntity = managerProfileService.getManagerProfileById(profileId);
		if (!(managerProfileEntity instanceof ManagerProfileEntity)) {
			throw new BusinessException("Manager profile not found.");
		}

		try {
			managerProfileService.deleteById(profileId);
		} catch (Exception e) {
			throw new BusinessException("The manager profile could not be updated.");
		}

		return managerProfileDto;
	}

	public ManagerProfileDto updateManagerProfile(Long managerProfileId, String managerProfileName, String description) throws BusinessException {

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
		if (!(managerProfileEntity instanceof ManagerProfileEntity)) {
			throw new BusinessException("Manager profile not found.");
		}

		managerProfileEntity.setName(managerProfileName);
		managerProfileEntity.setDescription(description);

		managerProfileEntity = managerProfileService.updateManagerProfile(managerProfileEntity);

		ManagerProfileDto managerProfileDto = this.transformEntityToDto(managerProfileEntity);

		return managerProfileDto;
	}

}
