package com.nals.hrm.controller;

import com.nals.hrm.dto.RequestDevicesDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import com.nals.hrm.service.RequestDeviceService;
import com.nals.hrm.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class RequestDeviceController {

    private final RequestDeviceService requestDeviceService;
    private final ResponseService responseService;

    @Autowired
    public RequestDeviceController(RequestDeviceService requestDeviceService, ResponseService responseService) {
        this.requestDeviceService = requestDeviceService;
        this.responseService = responseService;
    }

    /**
     * @api {post} /request-devices/ Add Request Device
     * @apiName addRequestDevice
     * @apiGroup RequestDevices
     * @apiParam {String} project_name Project name of Request Device
     * @apiParam {String} device_name Device name of Request Device
     * @apiParam {Integer} number_device Number device of Request Device
     * @apiParam {Date} expected_delivery_date Expected delivery date of Request Device
     * @apiParam {String} technical_specification Technical specification of Request Device
     * @apiParam {String} reason Reason of Request Device
     * @apiParam {Integer} user_id User Id
     * @apiSuccessExample Success Response
     * HTTP/1.1 201 CREATED
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "Created",
     *         "errors": {}
     *     },
     *     "data": {
     *         "id": 7,
     *         "updated_at": "2020-11-13T16:34:15.757",
     *         "created_at": "2020-11-13T16:34:15.757",
     *         "updated_by": null,
     *         "project_name": "hrm",
     *         "technical_specification": "Apple A14",
     *         "reason": "No have laptop",
     *         "assigned_by": null,
     *         "assigned_at": null,
     *         "status": 0,
     *         "device_name": "Macbook pro 15",
     *         "expected_delivery_date": "10/10/2020",
     *         "number_device": 1,
     *         "user_id": 1
     *     }
     * }
     * @apiErrorExample Create error
     * HTTP/1.1 400 BAD REQUEST
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "Bad Request",
     *         "errors": {
     *             // validation
     *         }
     *     },
     *     "data": null
     * }
     */
    @PostMapping("/request-devices")
    public ResponseEntity<Response<RequestDevicesDTO>> addRequestDevice(@Valid @RequestBody RequestDevicesDTO requestDeviceDTO,
                                                                        BindingResult bindingResult) throws ViolatedException, ConflictException {
        Response<RequestDevicesDTO> requestDevicesDTOResponse = requestDeviceService.save(requestDeviceDTO, bindingResult);
        return new ResponseEntity<>(requestDevicesDTOResponse, HttpStatus.CREATED);
    }

    /**
     * @api {get} /request-devices/ Get Request Device list
     * @apiName GetRequestDevicesList
     * @apiGroup RequestDevices
     * @apiParam {Integer} page Pagination of list Request Devices
     * @apiParam {String} fullName Name of User
     * @apiParam {String} deviceName Name of Device
     * @apiParam {Integer} status Status of Request Device, All: status=0,1,2
     * @apiParam {Date} fromDate Date of search from
     * @apiParam {Date} toDate Date of search to
     * @apiSuccessExample Success Response
     * HTTP/1.1 200 OK
     *{
     *     "meta": {
     *         "pagination": {
     *             "current_page": 0,
     *             "per_page": 10,
     *             "total": 1,
     *             "last_page": 0
     *         },
     *         "message": "OK",
     *         "errors": {}
     *     },
     *     "data": [
     *         {
     *             "id": 15,
     *             "created_at": "2020-11-13T11:23:25.847",
     *             "project_name": "hrm",
     *             "technical_specification": null,
     *             "reason": "No have laptop",
     *             "status": 1,
     *             "device_name": "Iphone",
     *             "expected_delivery_date": "10/10/2020",
     *             "number_device": 1,
     *             "email": "huy@gmail.com",
     *             "full_name": "Huy",
     *             "user_id": 2
     *         }
     *     ]
     * }
     * @apiErrorExample Get List error
     * HTTP/1.1 404 NOT FOUND
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "Record is not found",
     *         "errors": {}
     *     },
     *     "data": null
     * }
     */
    @GetMapping(path = "/request-devices")
    public ResponseEntity<Response<RequestDevicesDTO>> getRequestDevicesList(@PageableDefault(value = 10) Pageable page,
                                                                             @RequestParam(defaultValue = "") String fullName,
                                                                             @RequestParam(defaultValue = "") String deviceName,
                                                                             @RequestParam(defaultValue = "0,1,2") List<Integer> status,
                                                                             @RequestParam(defaultValue = "01/01/2020") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fromDate,
                                                                             @RequestParam(defaultValue = "31/12/2300") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate toDate)
            throws ResourceNotFoundException {
        Response<RequestDevicesDTO> requestDevicesList = requestDeviceService.findByRequestDevice(page, fullName, deviceName, status, fromDate, toDate);
        return new ResponseEntity(requestDevicesList, HttpStatus.OK);
    }

    /**
     * @api {get} /request-devices/:id Get detail of a Request Device
     * @apiName getRequestDeviceById
     * @apiGroup RequestDevices
     * @apiParam {Integer} id Request Device ID
     * @apiSuccessExample Success Response
     * HTTP/1.1 200 OK
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "OK",
     *         "errors": {}
     *     },
     *     "data": {
     *         "id": 1,
     *         "created_at": "2020-11-12T14:10:39.581",
     *         "project_name": "hrm",
     *         "technical_specification": null,
     *         "reason": "No have laptop",
     *         "status": 0,
     *         "device_name": "Macbook",
     *         "expected_delivery_date": "10/10/2020",
     *         "number_device": 1,
     *         "email": null,
     *         "full_name": null,
     *         "user_id": 1
     *     }
     * }
     * @apiErrorExample Get Detail error
     * HTTP/1.1 404 NOT FOUND
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "Record does not exist",
     *         "errors": {}
     *     },
     *     "data": null
     * }
     **/
    @GetMapping(path = "/request-devices/{id}")
    public ResponseEntity<RequestDevicesDTO> getRequestDeviceById(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        Response<RequestDevicesDTO> requestDeviceResponse = requestDeviceService.findById(id);
        return new ResponseEntity(requestDeviceResponse, HttpStatus.OK);
    }

    /**
     * @api {put} /request-devices/:id Edit Request Device
     * @apiName editRequestDevice
     * @apiGroup RequestDevices
     * @apiParam {Integer} id Id of Request Device
     * @apiParam {String} project_name Project name of Request Device
     * @apiParam {String} device_name Device name of Request Device
     * @apiParam {Integer} number_device Number device of Request Device
     * @apiParam {Date} expected_delivery_date Expected delivery date of Request Device
     * @apiParam {String} technical_specification Technical specification of Request Device
     * @apiParam {String} reason Reason of Request Device
     * @apiParam {Integer} user_id User Id
     * @apiSuccessExample Success Response
     * HTTP/1.1 200 OK
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "OK",
     *         "errors": {}
     *     },
     *     "data": {
     *         "id": 8,
     *         "updated_at": "2020-11-13T19:36:38.059",
     *         "created_at": "2020-11-13T19:33:32.753",
     *         "updated_by": null,
     *         "project_name": "hrm33",
     *         "technical_specification": "Macbook pro 15",
     *         "reason": "No have laptop",
     *         "assigned_by": null,
     *         "assigned_at": null,
     *         "status": 0,
     *         "device_name": "Macbook",
     *         "expected_delivery_date": "10/10/2020",
     *         "number_device": 1,
     *         "user_id": 1
     *     }
     * }
     * @apiErrorExample Edit Bad Request error
     * HTTP/1.1 422 BAD REQUEST
     * @apiErrorExample Edit Not Found error
     * HTTP/1.1 422 UNPROCESSABLE ENTITY
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "Record does not exist",
     *         "errors": {}
     *     },
     *     "data": null
     * }
     */
    @PutMapping("/request-devices/{id}")
    public ResponseEntity<Response<RequestDevicesDTO>> editRequestDevice(@Validated @RequestBody RequestDevicesDTO requestDevicesDTO,
                                                                         @PathVariable("id") Integer id, BindingResult bindingResult) throws ViolatedException, ConflictException, UnprocessableEntityException {
        Response<RequestDevicesDTO> requestDevicesDTOResponse = requestDeviceService.update(requestDevicesDTO, id, bindingResult);
        return new ResponseEntity<>(requestDevicesDTOResponse, HttpStatus.OK);
    }

    /**
     * @api {delete} /request-devices/:id Remove Request Device
     * @apiName removeRequestDevice
     * @apiGroup RequestDevices
     * @apiParam {Integer} id
     * @apiSuccessExample Success Response
     * HTTP/1.1 204 NO CONTENT
     * @apiErrorExample Remove error
     * HTTP/1.1 422 UNPROCESSABLE ENTITY
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "Record does not exist",
     *         "errors": {}
     *     },
     *     "data": null
     * }
     */
    @DeleteMapping(path = "/request-devices/{id}")
    public ResponseEntity<RequestDevicesDTO> removeRequestDevice(@PathVariable("id") Integer id) throws UnprocessableEntityException {
        requestDeviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
