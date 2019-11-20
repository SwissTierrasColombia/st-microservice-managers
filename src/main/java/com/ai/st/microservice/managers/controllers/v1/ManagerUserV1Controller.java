package com.ai.st.microservice.managers.controllers.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.managers.business.ManagerUserBusiness;
import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.exceptions.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Managers", description = "Managers", tags = { "Managers" })
@RestController
@RequestMapping("api/managers/v1/users")
public class ManagerUserV1Controller {

	@Autowired
	private ManagerUserBusiness managerUserBusiness;

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

}
