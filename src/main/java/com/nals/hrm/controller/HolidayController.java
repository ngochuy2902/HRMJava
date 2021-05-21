package com.nals.hrm.controller;

import com.nals.hrm.dto.HolidaysDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import com.nals.hrm.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class HolidayController {

    private final HolidayService holidayService;

    @Autowired
    private HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    /**
     * @api {get} /holidays Get Holidays List
     * @apiName getAllHolidays
     * @apiGroup Holidays
     * @apiParam {Pageable} page Page of the holiday
     * @apiParam {String} year Year of the holiday
     * @apiSuccess {Integer} id ID of the holiday
     * @apiSuccess {LocalDate} date Date of the holiday
     * @apiSuccess {String} notes Name of the holiday
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * "meta": {
     *         "pagination": {
     *             "current_page": 3,
     *             "per_page": 10,
     *             "total": 22,
     *             "last_page": 3
     *         },
     *         "message": "OK",
     *         "errors": {}
     *     },
     *     "data": [
     *         {
     *             "id": 94,
     *             "date": "02/09/2019",
     *             "notes": "Ngày quốc khánh"
     *         },
     *         {
     *             "id": 95,
     *             "date": "30/04/2019",
     *             "notes": "Ngày giải phóng"
     *         }
     *     ]
     * @apiError HolidayNotFound The id of the Holiday was not found.
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 404 Not Found
     *      "meta": {
     *         "message": "Holiday does not exist",
     *         "errors": {}
     *     },
     *     "data": null
     */
    @GetMapping("/holidays")
    public ResponseEntity<Response<HolidaysDTO>> getAllHolidays(@PageableDefault(value = 10) Pageable page,
                                                                @RequestParam(defaultValue = "", required = false) String year)
            throws ResourceNotFoundException {
        Response<HolidaysDTO> holidayDTOList = holidayService.findByHoliday(page, year);
        return new ResponseEntity<>(holidayDTOList, HttpStatus.OK);
    }

    /**
     * @api {post} /holidays Create holiday
     * @apiName createHoliday
     * @apiGroup Holidays
     * @apiParam {Integer} id ID of the holiday
     * @apiParam {LocalDate} date  Date of the holiday
     * @apiParam {String} notes Name of the holidays
     * @apiSuccess {Integer} id ID of the holiday
     * @apiSuccess {LocalDate} date  Date of the holiday
     * @apiSuccess {String} notes Name of the holidays
     * @apiSuccessExample Success-Request:
     * HTTP/1.1 201 Create
     * {
     * "id": 4,
     * "date": "2021-01-01",
     * "notes": "Tết Dương lịch",
     * }
     */
    @PostMapping("/holidays")
    public ResponseEntity<Response<HolidaysDTO>> CreateHolidays(@Valid @RequestBody HolidaysDTO holidayDTO, BindingResult bindingResult)
            throws Exception {
        Response<HolidaysDTO> response = holidayService.save(holidayDTO, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * @api {get} /holidays/:id Get detail holiday
     * @apiName getHolidayById
     * @apiGroup Holidays
     * @apiParam {LocalDate} date  Date of the holiday
     * @apiParam {String} notes Name of the holiday.
     * @apiParam {Integer} updated_by ID user updated holiday
     * @apiSuccess {Integer} id ID of the holiday
     * @apiSuccess {LocalDate} date  Date of the holiday
     * @apiSuccess {String} notes Name of the holiday.
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * "meta": {
     * "message": "OK",
     * "errors": {}
     * },
     * "data":{
     * "id": 4,
     * "updated_by": 2,
     * "date": "2021-01-01",
     * "notes": "Tết Dương lịch",
     * }
     * @apiError HolidayNotFound The id of the Holiday was not found.
     * @apiErrorExample Error-Response:
     *   HTTP/1.1 404 Not Found
     *    {
     *         "message": "HolidayNotFound"
     *    }
     */
    @GetMapping("/holidays/{id}")
    public ResponseEntity<Response<HolidaysDTO>> getHolidayById(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        Response<HolidaysDTO> holidayDTO = holidayService.findHolidayById(id);
        return new ResponseEntity<>(holidayDTO, HttpStatus.OK);
    }

    /**
     * @api {put} /holidays/:id Edit holiday
     * @apiName updateHolidays
     * @apiGroup Holidays
     * @apiParam {Integer} id ID of the holiday
     * @apiSuccess {Integer} id ID of the holiday
     * @apiSuccess {LocalDate} date Date of the holiday
     * @apiSuccess {String} notes Name of the holiday
     * @apiSuccessExample Success-request:
     * HTTP/1.1 200 OK
     * {
     * "id": 4,
     * "date": "2021-01-01",
     * "notes": "Tết Dương lịch",
     * }
     */
    @PutMapping("/holidays/{id}")
    public ResponseEntity<Response<HolidaysDTO>> updateHolidays(@Valid @PathVariable("id") Integer id,
                                                                @RequestBody HolidaysDTO holidayDTO,
                                                                BindingResult bindingResult)
            throws ViolatedException, ResourceNotFoundException, ConflictException {
        Response<HolidaysDTO> responseHoliday = holidayService.updateHoliday(id, holidayDTO, bindingResult);
        return new ResponseEntity<>(responseHoliday, HttpStatus.OK);
    }

    /**
     * @api {delete} /holidays/:id Delete holiday
     * @apiName deleteHolidays
     * @apiGroup Holidays
     * @apiParam {Integer} id ID of the Holiday.
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 204 No Content
     */
    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<?> deleteHolidays(@PathVariable("id") Integer id) throws ResourceNotFoundException,
            UnprocessableEntityException {
        holidayService.deleteHoliday(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
