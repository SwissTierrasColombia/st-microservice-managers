package com.ai.st.microservice.managers.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerStateDto;
import com.ai.st.microservice.managers.entities.ManagerEntity;
import com.ai.st.microservice.managers.entities.ManagerUserEntity;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.services.IManagerUserService;

@Component
public class ManagerUserBusiness {

    @Autowired
    private IManagerUserService managerUserService;

    public ManagerDto getManagerByUserCode(Long userCode) throws BusinessException {

        ManagerDto managerDto = null;

        List<ManagerUserEntity> listUserEntity = managerUserService.getManagersUsersByUserCode(userCode);

        for (ManagerUserEntity userEntity : listUserEntity) {

            ManagerEntity managerEntity = userEntity.getManager();

            managerDto = new ManagerDto();
            managerDto.setId(managerEntity.getId());
            managerDto.setAlias(managerEntity.getAlias());
            managerDto.setName(managerEntity.getName());
            managerDto.setTaxIdentificationNumber(managerEntity.getTaxIdentificationNumber());
            managerDto.setCreatedAt(managerEntity.getCreatedAt());
            managerDto.setManagerState(new ManagerStateDto(managerEntity.getManagerState().getId(),
                    managerEntity.getManagerState().getName()));

        }

        return managerDto;
    }

}
