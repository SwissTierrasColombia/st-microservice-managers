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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.managers.business.ManagerBusiness;
import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.exceptions.BusinessException;

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

}
