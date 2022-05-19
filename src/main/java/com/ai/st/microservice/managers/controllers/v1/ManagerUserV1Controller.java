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

import com.ai.st.microservice.managers.business.ManagerBusiness;
import com.ai.st.microservice.managers.business.ManagerProfileBusiness;
import com.ai.st.microservice.managers.business.ManagerUserBusiness;
import com.ai.st.microservice.managers.dto.AddUserToManagerDto;
import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerProfileDto;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.exceptions.InputValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Managers", tags = { "Managers User" })
@RestController
@RequestMapping("api/managers/v1/users")
public class ManagerUserV1Controller {

    private final Logger log = LoggerFactory.getLogger(ManagerV1Controller.class);

    private final ManagerUserBusiness managerUserBusiness;
    private final ManagerBusiness managerBusiness;
    private final ManagerProfileBusiness managerProfileBusiness;

    public ManagerUserV1Controller(ManagerUserBusiness managerUserBusiness, ManagerBusiness managerBusiness,
            ManagerProfileBusiness managerProfileBusiness) {
        this.managerUserBusiness = managerUserBusiness;
        this.managerBusiness = managerBusiness;
        this.managerProfileBusiness = managerProfileBusiness;
    }

    @GetMapping(value = "/{userCode}/managers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get manager by user")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Get manager by user", response = ManagerDto.class),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<ManagerDto> getManagerByUser(@PathVariable Long userCode) {

        HttpStatus httpStatus;
        ManagerDto managerDto = null;

        try {

            SCMTracing.setTransactionName("getManagerByUser");

            managerDto = managerUserBusiness.getManagerByUserCode(userCode);
            httpStatus = (managerDto != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        } catch (BusinessException e) {
            log.error("Error ManagerUserV1Controller@getManagerByUser#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerUserV1Controller@getManagerByUser#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(managerDto, httpStatus);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add user to manager")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "User added to manager", response = ManagerDto.class),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> addUserToManager(@RequestBody AddUserToManagerDto addUserToManager) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("addUserToManager");
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, addUserToManager.toString());

            // validation user code
            Long userCode = addUserToManager.getUserCode();
            if (userCode == null || userCode <= 0) {
                throw new InputValidationException("El código de usuario es inválido.");
            }

            // validation manager id
            Long managerId = addUserToManager.getManagerId();
            if (managerId == null || managerId <= 0) {
                throw new InputValidationException("El gestor es inválido.");
            }

            // validation profile id
            Long profileId = addUserToManager.getProfileId();
            if (profileId == null || profileId <= 0) {
                throw new InputValidationException("El perfil es inválido.");
            }

            responseDto = managerBusiness.addUserToManager(userCode, managerId, profileId);
            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error ManagerUserV1Controller@addUserToManager#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (BusinessException e) {
            log.error("Error ManagerUserV1Controller@addUserToManager#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerUserV1Controller@addUserToManager#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping(value = "{userCode}/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get profiles by user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Get profiles by user", response = ManagerProfileDto.class),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> getProfilesByUser(@PathVariable Long userCode) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("getProfilesByUser");

            responseDto = managerProfileBusiness.getProfilesByUser(userCode);
            httpStatus = HttpStatus.OK;

        } catch (BusinessException e) {
            log.error("Error ManagerUserV1Controller@getProfiles#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerUserV1Controller@getProfiles#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @DeleteMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove user to manager")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "User removed to manager", response = ManagerDto.class),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> removeUserToManager(@RequestBody AddUserToManagerDto removeUserToManager) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("removeUserToManager");
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, removeUserToManager.toString());

            // validation user code
            Long userCode = removeUserToManager.getUserCode();
            if (userCode == null || userCode <= 0) {
                throw new InputValidationException("El código de usuario es inválido.");
            }

            // validation manager id
            Long managerId = removeUserToManager.getManagerId();
            if (managerId == null || managerId <= 0) {
                throw new InputValidationException("El gestor es inválido.");
            }

            // validation profile id
            Long profileId = removeUserToManager.getProfileId();
            if (profileId == null || profileId <= 0) {
                throw new InputValidationException("El perfil es inválido.");
            }

            responseDto = managerBusiness.removeUserToManager(userCode, managerId, profileId);
            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error ManagerUserV1Controller@removeUserToManager#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (BusinessException e) {
            log.error("Error ManagerUserV1Controller@removeUserToManager#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerUserV1Controller@removeUserToManager#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
