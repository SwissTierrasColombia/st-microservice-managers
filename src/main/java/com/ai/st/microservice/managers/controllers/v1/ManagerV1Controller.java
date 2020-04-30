package com.ai.st.microservice.managers.controllers.v1;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.managers.business.ManagerBusiness;
import com.ai.st.microservice.managers.dto.CreateManagerDto;
import com.ai.st.microservice.managers.dto.ErrorDto;
import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerUserDto;
import com.ai.st.microservice.managers.dto.UpdateManagerDto;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.exceptions.InputValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Managers", description = "Managers", tags = { "Managers" })
@RestController
@RequestMapping("api/managers/v1/managers")
public class ManagerV1Controller {

	private final Logger log = LoggerFactory.getLogger(ManagerV1Controller.class);

	@Autowired
	private ManagerBusiness managerBusiness;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get managers")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get managers", response = ManagerDto.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<List<ManagerDto>> getManagers(
			@RequestParam(required = false, name = "state") Long managerStateId) {

		HttpStatus httpStatus = null;
		List<ManagerDto> listManagers = new ArrayList<ManagerDto>();

		try {

			listManagers = managerBusiness.getManagers(managerStateId);

			httpStatus = HttpStatus.OK;
		} catch (BusinessException e) {
			listManagers = null;
			log.error("Error ManagerV1Controller@getManagers#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			listManagers = null;
			log.error("Error ManagerV1Controller@getManagers#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(listManagers, httpStatus);
	}

	@RequestMapping(value = "/{managerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get manager by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get manager by id", response = ManagerDto.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<ManagerDto> getManagerById(@PathVariable Long managerId) {

		HttpStatus httpStatus = null;
		ManagerDto managerDto = null;

		try {

			managerDto = managerBusiness.getManagerById(managerId);

			httpStatus = (managerDto instanceof ManagerDto) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		} catch (BusinessException e) {
			log.error("Error ManagerV1Controller@getManagerById#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error ManagerV1Controller@getManagerById#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(managerDto, httpStatus);
	}

	@RequestMapping(value = "/{managerId}/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get users by manager")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get users by manager", response = ManagerUserDto.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<?> getUsersByManager(@PathVariable Long managerId,
			@RequestParam(required = false, name = "profiles") List<Long> profiles) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

			responseDto = managerBusiness.getUsersByManager(managerId, profiles);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error ManagerV1Controller@getManagerById#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ManagerV1Controller@getManagerById#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Manager")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Create Manager", response = ManagerDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> createManager(@RequestBody CreateManagerDto requestCreateManager) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

			// validation manager name
			String managerName = requestCreateManager.getName();
			if (managerName.isEmpty()) {
				throw new InputValidationException("El nombre del gestor es requerido");
			}

			// validation manager tax id
			String taxIdentification = requestCreateManager.getTaxIdentificationNumber();
			if (taxIdentification.isEmpty()) {
				throw new InputValidationException("El identificador de impuesto es requerido.");
			}

			responseDto = managerBusiness.addManager(managerName, taxIdentification);
			httpStatus = HttpStatus.CREATED;

		} catch (InputValidationException e) {
			log.error("Error ManagerV1Controller@createManager#Validation ---> " + e.getMessage());
			httpStatus = HttpStatus.BAD_REQUEST;
			responseDto = new ErrorDto(e.getMessage(), 1);
		} catch (BusinessException e) {
			log.error("Error ManagerV1Controller@createManager#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ManagerV1Controller@createManager#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

	@PutMapping("/{id}/activate")
	@ApiOperation(value = "Activate manager")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Manager activated", response = ManagerDto.class),
			@ApiResponse(code = 404, message = "Manager not found"),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<ManagerDto> activateManager(@PathVariable(required = true) Long id) {

		HttpStatus httpStatus = null;
		ManagerDto managerDtoResponse = null;

		try {

			managerDtoResponse = managerBusiness.activateManager(id);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error ManagerV1Controller@activateManager#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error ManagerV1Controller@activateManager#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(managerDtoResponse, httpStatus);
	}

	@PutMapping("/{id}/deactivate")
	@ApiOperation(value = "Disable manager")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Manager disabled", response = ManagerDto.class),
			@ApiResponse(code = 404, message = "Manager not found"),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<ManagerDto> deactivateManager(@PathVariable(required = true) Long id) {

		HttpStatus httpStatus = null;
		ManagerDto managerDtoResponse = null;

		try {

			managerDtoResponse = managerBusiness.deactivateManager(id);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error ManagerV1Controller@activateManager#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error ManagerV1Controller@activateManager#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(managerDtoResponse, httpStatus);
	}

	@RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update Manager")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Create Manager", response = ManagerDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> updateManager(@RequestBody UpdateManagerDto requestUpdateManager) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

			// validation manager id
			Long managerId = requestUpdateManager.getId();
			if (managerId > 0) {
				throw new InputValidationException("El id del gestor es requerido");
			}

			// validation manager name
			String managerName = requestUpdateManager.getName();
			if (managerName.isEmpty()) {
				throw new InputValidationException("El nombre del gestor es requerido");
			}

			// validation observations
			String taxIdentification = requestUpdateManager.getTaxIdentificationNumber();
			if (taxIdentification.isEmpty()) {
				throw new InputValidationException("El identificador de impuesto es requerido.");
			}

			responseDto = managerBusiness.updateManager(managerId, managerName, taxIdentification);
			httpStatus = HttpStatus.CREATED;

		} catch (InputValidationException e) {
			log.error("Error ManagerV1Controller@createManager#Validation ---> " + e.getMessage());
			httpStatus = HttpStatus.BAD_REQUEST;
			responseDto = new ErrorDto(e.getMessage(), 1);
		} catch (BusinessException e) {
			log.error("Error ManagerV1Controller@createManager#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ManagerV1Controller@createManager#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

}
