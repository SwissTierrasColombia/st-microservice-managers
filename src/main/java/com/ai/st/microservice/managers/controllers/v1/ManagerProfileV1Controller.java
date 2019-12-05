package com.ai.st.microservice.managers.controllers.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.managers.business.ManagerProfileBusiness;
import com.ai.st.microservice.managers.dto.ErrorDto;
import com.ai.st.microservice.managers.dto.ManagerProfileDto;
import com.ai.st.microservice.managers.exceptions.BusinessException;

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
	public ResponseEntity<Object> getProfiles() {

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

}
