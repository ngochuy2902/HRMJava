package com.nals.hrm.controller;

import com.nals.hrm.dto.DevicesDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import com.nals.hrm.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * @api {get} /devices Get Devices List
     * @apiGroup Devices
     * @apiName getAllDevices
     * @apiParam {Pageable} page Page of the device
     * @apiParam {String} name Name of the device
     * @apiParam {String} holder FullName of the User assigned
     * @apiParam {Boolean} status Status assigned (true) or available (false) of the device
     * @apiParam {LocalDate} dateFrom Input date of the device
     * @apiParam {LocalDate} dateTo Input date of the device
     * @apiParam {String} sort Sort device by deviceName or inputDate
     * @apiSuccess {Integer} id ID of the device
     * @apiSuccess {String} device_name Name of the device
     * @apiSuccess {String} serial_number Serial number of the device
     * @apiSuccess {LocalDate} input_date Input date of the device
     * @apiSuccess {Boolean} status Status assigned (true) or available (false) of the device
     * @apiSuccess {Integer} user_id ID of the User assigned
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     *"meta": {
     *         "pagination": {
     *             "current_page": 2,
     *             "per_page": 10,
     *             "total": 12,
     *             "last_page": 2
     *         },
     *         "message": "OK",
     *         "errors": {}
     *     },
     *     "data": [
     *         {
     *             "id": 25,
     *             "device_name": "phone Xs",
     *             "serial_number": "1236",
     *             "input_date": "2020-11-17",
     *             "status": false
     *         },
     *         {
     *             "id": 26,
     *             "device_name": "Iphone 6",
     *             "serial_number": "1236",
     *             "input_date": "2020-11-17",
     *             "status": false
     *         }
     *     ]
     * @apiError DeviceNotFound The id of the Device was not found.
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 404 Not Found
     *    {
     *     "meta": {
     *         "message": "Device does not found",
     *         "errors": {}
     *     },
     *     "data": null
     * }
     */
    @GetMapping("/devices")
    public ResponseEntity<Response<DevicesDTO>> getAllDevices(@PageableDefault(value = 10,page = 1) Pageable page,
                                                               @RequestParam(defaultValue = "", required = false) String name,
                                                               @RequestParam(defaultValue = "", required = false) String holder,
                                                               @RequestParam(required = false) Boolean status,
                                                               @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
                                                               @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
                                                               @RequestParam(defaultValue = "id", required = false) String sort)
            throws ResourceNotFoundException {
        Response<DevicesDTO> DeviceDTOList = deviceService.getAllDevice(page, name, holder, status, dateFrom, dateTo, sort);
        return new ResponseEntity<>(DeviceDTOList, HttpStatus.OK);
    }

    /**
     * @api {get} /devices/:id Get Devices detail
     * @apiGroup Devices
     * @apiParam {Integer} id ID of the device
     * @apiSuccess {Integer} id ID of the device
     * @apiSuccess {String} device_name Name of the device
     * @apiSuccess {String} serial_number Serial number of the device
     * @apiSuccess {LocalDate} input_date input date of the device
     * @apiSuccess {Boolean} status Status assigned (true) or available (false) of the device
     * @apiSuccess {Integer} user_id ID of the User assigned
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     *"meta": {
     *         "message": "OK",
     *         "errors": {}
     *     },
     *     "data": [
     *         {
     *             "id": 25,
     *             "device_name": "phone Xs",
     *             "serial_number": "1236",
     *             "input_date": "2020-11-17",
     *             "status": true
     *             "user_id": 6
     *         },
     *     ]
     * @apiError DeviceNotFound The id of the Device was not found.
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 404 Not Found
     *     {
     *       "message": "DeviceNotFound"
     *     }
     */
    @GetMapping("/devices/{id}")
    public ResponseEntity<Response<DevicesDTO>> getHolidayById(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        Response<DevicesDTO> deviceDTO = deviceService.getHolidayById(id);
        return new ResponseEntity<>(deviceDTO, HttpStatus.OK);
    }

    /**
     * @api {post} /devices Create Device
     * @apiName createDevice
     * @apiGroup Devices
     * @apiParam  {String} device_name Name of the device
     * @apiParam  {String} serial_number Serial number of the device
     * @apiParam  {LocalDate} input_date Input date of the device
     * @apiParam  {Boolean} status Status assigned (true) or available (false) of the device
     * @apiSuccess {Integer} id ID of the device
     * @apiSuccess {String} device_name Name of the device
     * @apiSuccess {String} serial_number Serial number of the device
     * @apiSuccess {LocalDate} input_date Input date of the device
     * @apiSuccess {Boolean} status Status assigned (true) or available (false) of the device
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 201 Create
     * {
     * "id": 1,
     * "updated_by": 2,
     * "device_name": "Iphone X",
     * "serial_number": "123",
     * "input_date": 2020-11-10,
     * "status": false,
     * }
     */
    @PostMapping("/devices")
    public ResponseEntity<Response<DevicesDTO>> CreateHolidays(@Valid @RequestBody DevicesDTO devicesDTO, BindingResult bindingResult)
            throws Exception {
        Response<DevicesDTO> response = deviceService.save(devicesDTO, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * @api {put} /devices/:id Update Device
     * @apiName   updateDevice
     * @apiParam  {Integer} id ID of the device
     * @apiParam  {String} device_name Name of the device
     * @apiParam  {String} serial_number Serial number of the device
     * @apiParam  {LocalDate} input_date input date of the device
     * @apiParam  {Boolean} status Status assigned (true) or available (false) of the device
     * @apiParam  {Integer} user_id ID of the User assigned
     * @apiSuccess {Integer} id ID of the device
     * @apiSuccess {String} device_name Name of the device
     * @apiSuccess {String} serial_number Serial number of the device
     * @apiSuccess {LocalDate} input_date input date of the device
     * @apiSuccess {Boolean} status Status assigned (true) or available (false) of the device
     * @apiSuccess {Integer} user_id ID of the User assigned
     * @apiGroup Devices
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "id": 1,
     * "updated_by": 3,
     * "device_name": "Iphone X",
     * "serial_number": "123",
     * "input_date": 2020-11-10,
     * "status": true,
     * "user_id": 3,
     * }
     */
    @PutMapping("/devices/{id}")
    public ResponseEntity<Response<DevicesDTO>> updateDevice(@Valid @PathVariable("id") Integer id,
                                                             @RequestBody DevicesDTO deviceDTO,
                                                             BindingResult bindingResult)
            throws ViolatedException, ResourceNotFoundException, ConflictException {
        Response<DevicesDTO> responseDevice = deviceService.updateDevice(id, deviceDTO, bindingResult);
        return new ResponseEntity<>(responseDevice, HttpStatus.OK);
    }

    /**
     * @api {delete} /devices/:id Delete Devices
     * @apiName deleteDevices
     * @apiGroup Devices
     * @apiParam {Integer} id ID of the Device.
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 204 No Content
     */
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") Integer id) throws ResourceNotFoundException,
            UnprocessableEntityException {
        deviceService.deleteDevice(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
