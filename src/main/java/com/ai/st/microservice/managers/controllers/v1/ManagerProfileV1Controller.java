package com.ai.st.microservice.managers.controllers.v1;

import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.managers.services.tracing.SCMTracing;
import com.ai.st.microservice.managers.services.tracing.TracingKeyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ai.st.microservice.managers.business.ManagerProfileBusiness;
import com.ai.st.microservice.managers.dto.CreateManagerProfileDto;
import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerProfileDto;
import com.ai.st.microservice.managers.dto.UpdateManagerProfileDto;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.exceptions.InputValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Manager Profiles", tags = { "Managers Profiles" })
@RestController
@RequestMapping("api/managers/v1/profiles")
public class ManagerProfileV1Controller {

    private final Logger log = LoggerFactory.getLogger(ManagerProfileV1Controller.class);

    private final ManagerProfileBusiness managerProfileBusiness;

    public ManagerProfileV1Controller(ManagerProfileBusiness managerProfileBusiness) {
        this.managerProfileBusiness = managerProfileBusiness;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get profiles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get profiles", response = ManagerProfileDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> getManagerProfiles() {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("getManagerProfiles");

            responseDto = managerProfileBusiness.getProfiles();
            httpStatus = HttpStatus.OK;

        } catch (BusinessException e) {
            log.error("Error ManagerProfileV1Controller@getManagerProfiles#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerProfileV1Controller@getManagerProfiles#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Manager Profile")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Profile created", response = ManagerDto.class),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> createManagerProfile(@RequestBody CreateManagerProfileDto requestCreateManagerProfile) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("createManagerProfile");
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, requestCreateManagerProfile.toString());

            // validation manager profile name
            String managerProfileName = requestCreateManagerProfile.getName();
            if (managerProfileName.isEmpty()) {
                throw new InputValidationException("El nombre del perfil de gestor es requerido");
            }

            // validation manager profile description
            String description = requestCreateManagerProfile.getDescription();
            if (description.isEmpty()) {
                throw new InputValidationException("La descripción es requerida.");
            }

            responseDto = managerProfileBusiness.addManagerProfile(managerProfileName, description);
            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error ManagerProfileV1Controller@createManagerProfile#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (BusinessException e) {
            log.error("Error ManagerProfileV1Controller@createManagerProfile#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerProfileV1Controller@createManagerProfile#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @DeleteMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete manager profile")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Profile deleted", response = ManagerDto.class),
            @ApiResponse(code = 404, message = "Manager profile not found"),
            @ApiResponse(code = 500, message = "Error Server") })
    public ResponseEntity<?> deleteManagerProfile(@RequestBody UpdateManagerProfileDto requestUpdateManagerProfile) {

        HttpStatus httpStatus;
        BasicResponseDto response = null;

        try {

            SCMTracing.setTransactionName("deleteManagerProfile");
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, requestUpdateManagerProfile.toString());

            managerProfileBusiness.deleteManagerProfile(requestUpdateManagerProfile.getId());
            response = new BasicResponseDto("Perfil eliminado");

            httpStatus = HttpStatus.OK;

        } catch (BusinessException e) {
            log.error("Error ManagerProfileV1Controller@deleteManagerProfile#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerProfileV1Controller@deleteManagerProfile#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Manager Profile")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Profile updated", response = ManagerDto.class),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> updateManagerProfile(@RequestBody UpdateManagerProfileDto requestUpdateManagerProfile) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("updateManagerProfile");
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, requestUpdateManagerProfile.toString());

            // validation manager id
            Long managerProfileId = requestUpdateManagerProfile.getId();
            if (managerProfileId <= 0) {
                throw new InputValidationException("El id del perfil de gestor es requerido");
            }

            // validation manager name
            String managerProfileName = requestUpdateManagerProfile.getName();
            if (managerProfileName.isEmpty()) {
                throw new InputValidationException("El nombre del perfil de gestor es requerido");
            }

            // validation observations
            String description = requestUpdateManagerProfile.getDescription();
            if (description.isEmpty()) {
                throw new InputValidationException("La descripción es requerida.");
            }

            responseDto = managerProfileBusiness.updateManagerProfile(managerProfileId, managerProfileName,
                    description);
            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error ManagerProfileV1Controller@updateManagerProfile#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (BusinessException e) {
            log.error("Error ManagerProfileV1Controller@updateManagerProfile#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerProfileV1Controller@updateManagerProfile#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
