package com.ai.st.microservice.managers.controllers.v1;

import java.util.ArrayList;
import java.util.List;

import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.managers.services.CryptoService;
import com.ai.st.microservice.managers.services.tracing.SCMTracing;
import com.ai.st.microservice.managers.services.tracing.TracingKeyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ai.st.microservice.managers.business.ManagerBusiness;
import com.ai.st.microservice.managers.dto.CreateManagerDto;
import com.ai.st.microservice.managers.dto.ManagerDto;
import com.ai.st.microservice.managers.dto.ManagerUserDto;
import com.ai.st.microservice.managers.dto.UpdateManagerDto;
import com.ai.st.microservice.managers.exceptions.BusinessException;
import com.ai.st.microservice.managers.exceptions.InputValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Managers", tags = { "Managers" })
@RestController
@RequestMapping("api/managers/v1/managers")
public class ManagerV1Controller {

    private final Logger log = LoggerFactory.getLogger(ManagerV1Controller.class);

    @Value("${crypto.token-igac}")
    private String tokenIGAC;

    private final ManagerBusiness managerBusiness;
    private final CryptoService cryptoService;

    public ManagerV1Controller(ManagerBusiness managerBusiness, CryptoService cryptoService) {
        this.managerBusiness = managerBusiness;
        this.cryptoService = cryptoService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get managers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get managers", response = ManagerDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<List<ManagerDto>> getManagers(
            @RequestParam(required = false, name = "state") Long managerStateId) {

        HttpStatus httpStatus;
        List<ManagerDto> listManagers;

        try {

            SCMTracing.setTransactionName("getManagers");

            listManagers = managerBusiness.getManagers(managerStateId);

            httpStatus = HttpStatus.OK;
        } catch (BusinessException e) {
            listManagers = null;
            log.error("Error ManagerV1Controller@getManagers#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            listManagers = null;
            log.error("Error ManagerV1Controller@getManagers#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(listManagers, httpStatus);
    }

    @GetMapping(value = "/{managerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get manager by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get manager by id", response = ManagerDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<ManagerDto> getManagerById(@PathVariable Long managerId) {

        HttpStatus httpStatus;
        ManagerDto managerDto = null;

        try {

            SCMTracing.setTransactionName("getManagerById");

            managerDto = managerBusiness.getManagerById(managerId);

            httpStatus = (managerDto != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        } catch (BusinessException e) {
            log.error("Error ManagerV1Controller@getManagerById#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerV1Controller@getManagerById#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(managerDto, httpStatus);
    }

    @GetMapping(value = "/{managerId}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get users by manager")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get users by manager", response = ManagerUserDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> getUsersByManager(@PathVariable Long managerId,
            @RequestParam(required = false, name = "profiles") List<Long> profiles) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("getUsersByManager");

            responseDto = managerBusiness.getUsersByManager(managerId, profiles);
            httpStatus = HttpStatus.OK;

        } catch (BusinessException e) {
            log.error("Error ManagerV1Controller@getUsersByManager#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerV1Controller@getUsersByManager#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Manager")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Manager created", response = ManagerDto.class),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> createManager(@RequestBody CreateManagerDto requestCreateManager) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("createManager");
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, requestCreateManager.toString());

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

            responseDto = managerBusiness.addManager(managerName, taxIdentification, requestCreateManager.getAlias());
            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error ManagerV1Controller@createManager#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (BusinessException e) {
            log.error("Error ManagerV1Controller@createManager#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerV1Controller@createManager#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PutMapping("/{id}/enable")
    @ApiOperation(value = "Activate manager")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Manager activated", response = ManagerDto.class),
            @ApiResponse(code = 404, message = "Manager not found"),
            @ApiResponse(code = 500, message = "Error Server") })
    public ResponseEntity<ManagerDto> activateManager(@PathVariable Long id) {

        HttpStatus httpStatus;
        ManagerDto managerDtoResponse = null;

        try {

            SCMTracing.setTransactionName("activateManager");

            managerDtoResponse = managerBusiness.activateManager(id);
            httpStatus = HttpStatus.OK;

        } catch (BusinessException e) {
            log.error("Error ManagerV1Controller@activateManager#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerV1Controller@activateManager#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(managerDtoResponse, httpStatus);
    }

    @PutMapping("/{id}/disable")
    @ApiOperation(value = "Disable manager")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Manager disabled", response = ManagerDto.class),
            @ApiResponse(code = 404, message = "Manager not found"),
            @ApiResponse(code = 500, message = "Error Server") })
    public ResponseEntity<ManagerDto> deactivateManager(@PathVariable Long id) {

        HttpStatus httpStatus;
        ManagerDto managerDtoResponse = null;

        try {

            SCMTracing.setTransactionName("deactivateManager");

            managerDtoResponse = managerBusiness.deactivateManager(id);
            httpStatus = HttpStatus.OK;

        } catch (BusinessException e) {
            log.error("Error ManagerV1Controller@deactivateManager#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerV1Controller@deactivateManager#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(managerDtoResponse, httpStatus);
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Manager")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Manager updated", response = ManagerDto.class),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> updateManager(@RequestBody UpdateManagerDto requestUpdateManager) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("updateManager");
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, requestUpdateManager.toString());

            // validation manager id
            Long managerId = requestUpdateManager.getId();
            if (managerId <= 0) {
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

            responseDto = managerBusiness.updateManager(managerId, managerName, taxIdentification,
                    requestUpdateManager.getAlias());
            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error ManagerV1Controller@updateManager#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (BusinessException e) {
            log.error("Error ManagerV1Controller@updateManager#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ManagerV1Controller@updateManager#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get public managers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get public managers", response = ManagerDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<List<ManagerDto>> getPublicManagers(
            @RequestParam(required = false, name = "state") Long managerStateId,
            @RequestHeader("st-token") String stPublicTokenEncrypted) {

        HttpStatus httpStatus;
        List<ManagerDto> listManagers;

        try {

            SCMTracing.setTransactionName("getPublicManagers");

            String token = cryptoService.decrypt(stPublicTokenEncrypted);
            if (!tokenIGAC.equals(token)) {
                throw new InputValidationException("El usuario no tiene permisos para consultar los gestores.");
            }

            log.info("Searching managers");

            listManagers = managerBusiness.getManagers(managerStateId);

            httpStatus = HttpStatus.OK;
        } catch (BusinessException e) {
            listManagers = new ArrayList<>();
            log.error("Error ManagerV1Controller@getPublicManagers#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            listManagers = new ArrayList<>();
            log.error("Error ManagerV1Controller@getPublicManagers#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(listManagers, httpStatus);
    }

}
