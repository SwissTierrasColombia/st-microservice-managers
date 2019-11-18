package com.ai.st.microservice.managers.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerStateDto;
import com.ai.st.microservice.managers.entities.ManagerEntity;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.services.IManagerService;

@Component
public class ManagerBusiness {

	@Autowired
	private IManagerService managerService;

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

}
