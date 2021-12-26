package com.ai.st.microservice.managers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.managers.business.ManagerProfileBusiness;
import com.ai.st.microservice.managers.business.ManagerStateBusiness;
import com.ai.st.microservice.managers.entities.ManagerEntity;
import com.ai.st.microservice.managers.entities.ManagerProfileEntity;
import com.ai.st.microservice.managers.entities.ManagerStateEntity;
import com.ai.st.microservice.managers.services.IManagerProfileService;
import com.ai.st.microservice.managers.services.IManagerService;
import com.ai.st.microservice.managers.services.IManagerStateService;

@Component
public class StMicroserviceManagersApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(StMicroserviceManagersApplicationStartup.class);

    @Autowired
    private IManagerStateService managerStateService;

    @Autowired
    private IManagerService managerService;

    @Autowired
    private IManagerProfileService managerProfileService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("ST - Loading Domains ... ");
        this.initManagerStates();
        this.initProfiles();
        this.initManagers();
    }

    public void initManagerStates() {

        Long countStates = managerStateService.getCount();
        if (countStates == 0) {

            try {

                ManagerStateEntity stateActive = new ManagerStateEntity();
                stateActive.setId(ManagerStateBusiness.MANAGER_STATE_ACTIVE);
                stateActive.setName("ACTIVO");
                managerStateService.createManagerState(stateActive);

                ManagerStateEntity stateInactive = new ManagerStateEntity();
                stateInactive.setId(ManagerStateBusiness.MANAGER_STATE_INACTIVE);
                stateInactive.setName("INACTIVO");
                managerStateService.createManagerState(stateInactive);

                log.info("The domains 'managers states' have been loaded!");
            } catch (Exception e) {
                log.error("Failed to load 'managers states' domains");
            }

        }

    }

    public void initManagers() {
        Long countManagers = managerService.getCount();
        if (countManagers == 0) {

            try {

                ManagerStateEntity stateActive = managerStateService
                        .getManagerStateById(ManagerStateBusiness.MANAGER_STATE_ACTIVE);

                ManagerEntity managerIGAC = new ManagerEntity();
                managerIGAC.setName("INSTITUTO GEOGRÁFICO AGUSTÍN CODAZZI");
                managerIGAC.setAlias("IGAC");
                managerIGAC.setTaxIdentificationNumber("8999990049");
                managerIGAC.setCreatedAt(new Date());
                managerIGAC.setManagerState(stateActive);
                managerService.createManager(managerIGAC);

                log.info("The domains 'managers' have been loaded!");
            } catch (Exception e) {
                log.error("Failed to load 'managers' domains");
            }

        }
    }

    public void initProfiles() {
        Long countProfiles = managerProfileService.getCount();
        if (countProfiles == 0) {

            try {

                ManagerProfileEntity profileDirector = new ManagerProfileEntity();
                profileDirector.setId(ManagerProfileBusiness.MANAGER_PROFILE_DIRECTOR);
                profileDirector.setName("DIRECTOR");
                managerProfileService.createManagerProfile(profileDirector);

                ManagerProfileEntity profileSinic = new ManagerProfileEntity();
                profileSinic.setId(ManagerProfileBusiness.MANAGER_PROFILE_SINIC);
                profileSinic.setName("SINIC");
                managerProfileService.createManagerProfile(profileSinic);

                log.info("The domains 'profiles' have been loaded!");
            } catch (Exception e) {
                log.error("Failed to load 'profiles' domains");
            }

        }
    }

}
