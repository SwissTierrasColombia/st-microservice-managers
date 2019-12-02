package com.ai.st.microservice.managers.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.managers.dto.ManagerProfileDto;
import com.ai.st.microservice.managers.entities.ManagerProfileEntity;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.services.IManagerProfileService;

@Component
public class ManagerProfileBusiness {

	@Autowired
	private IManagerProfileService managerProfileService;

	public static final Long MANAGER_PROFILE_DIRECTOR = (long) 1;
	public static final Long MANAGER_PROFILE_INTEGRATOR = (long) 2;

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

}
