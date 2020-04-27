package com.ai.st.microservice.managers.controllers.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.managers.business.ManagerBusiness;
import com.ai.st.microservice.managers.business.ManagerProfileBusiness;
import com.ai.st.microservice.managers.business.ManagerUserBusiness;
import com.ai.st.microservice.managers.dto.AddUserToManagerDto;
import com.ai.st.microservice.managers.dto.ErrorDto;
import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerProfileDto;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.exceptions.InputValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Managers", description = "Managers", tags = { "Managers User" })
@RestController
@RequestMapping("api/managers/v1/users")
public class ManagerUserV1Controller {

	@Autowired
	private ManagerUserBusiness managerUserBusiness;

	@Autowired
	private ManagerBusiness managerBusiness;

	@Autowired
	private ManagerProfileBusiness managerProfileBusiness;

	private final Logger log = LoggerFactory.getLogger(ManagerV1Controller.class);

	@RequestMapping(value = "/{userCode}/managers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get manager by user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Get manager by user", response = ManagerDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<ManagerDto> getManagersByUser(@PathVariable Long userCode) {

		HttpStatus httpStatus = null;
		ManagerDto managerDto = null;

		try {
			managerDto = managerUserBusiness.getManagerByUserCode(userCode);
			httpStatus = (managerDto instanceof ManagerDto) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		} catch (BusinessException e) {
			log.error("Error ManagerUserV1Controller@getManagersByUser#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error ManagerUserV1Controller@getManagersByUser#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(managerDto, httpStatus);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Add user to manager")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Add user to manager", response = ManagerDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> addUserToManager(@RequestBody AddUserToManagerDto addUserToManager) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

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
			responseDto = new ErrorDto(e.getMessage(), 1);
		} catch (BusinessException e) {
			log.error("Error ManagerUserV1Controller@addUserToManager#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ManagerUserV1Controller@addUserToManager#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

	@RequestMapping(value = "{userCode}/profiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get profiles by user")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Get profiles by user", response = ManagerProfileDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<?> getProfilesByUser(@PathVariable Long userCode) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {
			responseDto = managerProfileBusiness.getProfilesByUser(userCode);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error ManagerUserV1Controller@getProfiles#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ManagerUserV1Controller@getProfiles#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

	@RequestMapping(value = "", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Remove user to manager")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Remove user to manager", response = ManagerDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> removeUserToManager(@RequestBody AddUserToManagerDto addUserToManager) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

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

			responseDto = managerBusiness.removeUserToManager(userCode, managerId, profileId);
			httpStatus = HttpStatus.OK;

		} catch (InputValidationException e) {
			log.error("Error ManagerUserV1Controller@removeUserToManager#Validation ---> " + e.getMessage());
			httpStatus = HttpStatus.BAD_REQUEST;
			responseDto = new ErrorDto(e.getMessage(), 1);
		} catch (BusinessException e) {
			log.error("Error ManagerUserV1Controller@removeUserToManager#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ManagerUserV1Controller@removeUserToManager#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

}
