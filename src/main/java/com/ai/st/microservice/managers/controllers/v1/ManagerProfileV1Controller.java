package com.ai.st.microservice.managers.controllers.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.managers.business.ManagerProfileBusiness;
import com.ai.st.microservice.managers.dto.CreateManagerProfileDto;
import com.ai.st.microservice.managers.dto.ErrorDto;
import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerProfileDto;
import com.ai.st.microservice.managers.dto.UpdateManagerProfileDto;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.exceptions.InputValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Manager Profiles", description = "Managers Profiles", tags = { "Managers Profiles" })
@RestController
@RequestMapping("api/managers/v1/profiles")
public class ManagerProfileV1Controller {

	private final Logger log = LoggerFactory.getLogger(ManagerProfileV1Controller.class);

	@Autowired
	private ManagerProfileBusiness managerProfileBusiness;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get profiles")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get profiles", response = ManagerProfileDto.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> getManagerProfiles() {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {
			responseDto = managerProfileBusiness.getProfiles();
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error ManagerProfileV1Controller@getProfiles#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ManagerProfileV1Controller@getProfiles#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Manager Profile")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Create Manager Profile", response = ManagerDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> createManagerProfile(@RequestBody CreateManagerProfileDto requestCreateManagerProfile) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

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
			responseDto = new ErrorDto(e.getMessage(), 1);
		} catch (BusinessException e) {
			log.error("Error ManagerProfileV1Controller@createManagerProfile#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ManagerProfileV1Controller@createManagerProfile#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

	@RequestMapping(value = "", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete manager profile")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Manager prrofile deleted", response = ManagerDto.class),
			@ApiResponse(code = 404, message = "Manager profile not found"),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<ManagerProfileDto> deleteManagerProfile(@RequestBody UpdateManagerProfileDto requestUpdateManagerProfile) {

		HttpStatus httpStatus = null;
		ManagerProfileDto managerProfileDtoResponse = null;

		try {

			managerProfileDtoResponse = managerProfileBusiness.deleteManagerProfile(requestUpdateManagerProfile.getId());
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error ManagerProfileV1Controller@removeManagerProfile#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error ManagerProfileV1Controller@removeManagerProfile#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(managerProfileDtoResponse, httpStatus);
	}

	@RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update Manager Profile")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Update Manager Profile", response = ManagerDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> updateManagerProfile(@RequestBody UpdateManagerProfileDto requestUpdateManagerProfile) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

			// validation manager id
			Long managerProfileId = requestUpdateManagerProfile.getId();
			if (managerProfileId <= 0) {
				throw new InputValidationException("El id del prerfile de gestor es requerido");
			}

			// validation manager name
			String managerProfileName = requestUpdateManagerProfile.getName();
			if (managerProfileName.isEmpty()) {
				throw new InputValidationException("El nombre del perfile de gestor es requerido");
			}

			// validation observations
			String description = requestUpdateManagerProfile.getDescription();
			if (description.isEmpty()) {
				throw new InputValidationException("La descripción es requerida.");
			}

			responseDto = managerProfileBusiness.updateManagerProfile(managerProfileId, managerProfileName, description);
			httpStatus = HttpStatus.CREATED;

		} catch (InputValidationException e) {
			log.error("Error ManagerProfileV1Controller@updateManagerProfile#Validation ---> " + e.getMessage());
			httpStatus = HttpStatus.BAD_REQUEST;
			responseDto = new ErrorDto(e.getMessage(), 1);
		} catch (BusinessException e) {
			log.error("Error ManagerProfileV1Controller@updateManagerProfile#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ManagerProfileV1Controller@updateManagerProfile#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

}
