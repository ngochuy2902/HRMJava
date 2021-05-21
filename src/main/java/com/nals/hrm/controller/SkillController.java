package com.nals.hrm.controller;

import com.nals.hrm.dto.SkillsDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import com.nals.hrm.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class SkillController {

    private final SkillService skillService;


    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * @api {post} /skills Registration skills
     * @apiName registerSkill
     * @apiGroup Skills
     * @apiParam {String} name Name of skill
     * @apiSuccessExample Success Response:
     * HTTP/1.1 201 CREATED
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "Created",
     *         "errors": {}
     *     },
     *     "data": {
     *         "name": "Java",
     *         "id": 1,
     *         "updated_at": "2020-11-13T19:51:54.129",
     *         "created_at": "2020-11-13T19:51:54.129",
     *         "updated_by": null
     *     }
     * }
     * @apiErrorExample Get Detail Conflict error:
     * HTTP/1.1 409 CONFLICT
     * {
     *     "meta": {
     *         "pagination": null,
     *         "message": "Record already exists",
     *         "errors": {}
     *     },
     *     "data": null
     * }
     */
    @PostMapping(path = "/skills")
    public ResponseEntity<Response<SkillsDTO>> registerSkill(@Validated @RequestBody SkillsDTO skillDTO, BindingResult bindingResult) throws ViolatedException, ConflictException {
        Response<SkillsDTO> skillsDTOResponse = skillService.save(skillDTO, bindingResult);
        return new ResponseEntity<>(skillsDTOResponse, HttpStatus.CREATED);
    }

    /**
     * @api {get} /skills Get list
     * @apiName getSkillsList
     * @apiGroup Skills
     * @apiParam {Integer} page Page number of list Skills if the page is empty, value default is 1
     * @apiParam {String} name Name of Skill limited to 45 characters, value can empty
     * @apiParam {String} direct Directed sort, value can is desc (descending direction) or asc (ascending direction)
     * @apiParam {String} sort Field need sort. If the sort is empty, value default is name
     * @apiParam {Integer} sizePage Size Page of list skills. If the sizePage is empty, value default is 10
     * @apiSuccess {Meta} meta Meta include pagination of the skill, message response status  and error response
     * @apiSuccess {String} message Response status notification include success status and errors status
     * @apiSuccess {Object} errors Response error notification include field error,message and code error
     * @apiSuccess {Pagination} pagination Pagination of the skill
     * @apiSuccess {Integer} current_page Current page of list skills
     * @apiSuccess {Integer} per_page Max number of the skill per page
     * @apiSuccess {Integer} total Total of all skill
     * @apiSuccess {Integer} last_page Last page of pagination
     * @apiSuccess {Array} data List skills, value empty when skill not found
     * @apisuccess {Integer} id Skill unique ID
     * @apiSuccess {String} name Name of skill limited to 45 characters, skill unique and not null field name
     * @apiSuccess {LocalDateTime} update_at Update time of the skill with format yyyy-MM-dd'T'HH:mm:ss.SSSz
     * @apiSuccessExample Success Response:
     * HTTP/1.1 200 OK
     * "meta": {
     *         "pagination": {
     *             "current_page": 1,
     *             "per_page": 10,
     *             "total": 2,
     *             "last_page": 1
     *         },
     *         "message": "OK",
     *         "errors": {}
     *     },
     *     "data": [
     *         {
     *             "id": 1,
     *             "name": "java",
     *             "updated_at": "2020-11-22T22:19:59"
     *         },
     *         {
     *             "id": 2,
     *             "name": "php",
     *             "updated_at": "2020-11-23T08:49:47"
     *         },
     *         ]
     */
    @GetMapping(path = "/skills")
    public ResponseEntity<Response<SkillsDTO>> getSkillsList(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int sizePage,
                                                             @RequestParam(defaultValue = "") String name,
                                                             @RequestParam(defaultValue = "desc") String direct,
                                                             @RequestParam(defaultValue = "name") String sort)
            throws ResourceNotFoundException {
        Response<SkillsDTO> skillsList = skillService.findBySkill(page, sizePage, name, direct, sort);
        return new ResponseEntity<>(skillsList, HttpStatus.OK);
    }

    /**
     * @api {get} /skills/:id Get detail
     * @apiName getSkillById
     * @apiGroup Skills
     * @apiParam {Integer} id Skill unique ID
     * @apiSuccess {Meta} meta Meta include message status and error response
     * @apiSuccess {String} message Response status notification include success status and errors status
     * @apiSuccess {Object} errors Response error notification include field error,message and code error
     * @apiSuccess {Object} data Skill detail
     * @apiSuccess {Integer} id Skill unique ID
     * @apiSuccess {String} name Name of skill limited to 45 characters, skill unique and not null field name
     * @apiSuccess {LocalDateTime} updated_at Update time of the skill with format yyyy-MM-dd'T'HH:mm:ss.SSSz
     * @apiSuccessExample Success Response:
     * HTTP/1.1 200 OK
     *     "meta": {
     *         "message": "OK",
     *         "errors": {}
     *     },
     *     "data": {
     *         "id": 1,
     *         "name": "java",
     *         "updated_at": "2020-11-13T14:20:01.891813",
     *     }
     * @apiErrorExample Get Detail error:
     * HTTP/1.1 404 NOT FOUND
     * {
     * "meta": {
     *         "message": "Record does not exist",
     *         "errors": {
     *             "id": {
     *                 "message": "id 1 does not exist",
     *                 "code": "VAL_012"
     *             }
     *         }
     *     }
     * }
     **/
    @GetMapping(path = "/skills/{id}")
    public ResponseEntity<Response<SkillsDTO>> getSkillById(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        Response<SkillsDTO> skillResponse = skillService.findById(id);
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    /**
     * @api {put} /skills/:id Edit Skill
     * @apiName editSkill
     * @apiGroup Skills
     * @apiParam {Integer} id Skill unique ID
     * @apiParam {String} name Skill Name
     * @apiParam {Integer} updated_by User unique ID updated
     * @apiSuccess {Meta} meta Meta include message status and error response
     * @apiSuccess {String} message Response status notification include success status and errors status
     * @apiSuccess {Object} errors Response error notification include field error, message and code error
     * @apiSuccess {Object} data Skill detail
     * @apiSuccess {Integer} id Skill unique ID
     * @apiSuccess {String} name Name of skill limited to 45 characters, skill unique and not null field name
     * @apiSuccess {LocalDateTime} updated_at Update time of the skill with format yyyy-MM-dd'T'HH:mm:ss.SSSz
     * @apiSuccessExample Success Response:
     * HTTP/1.1 200 OK
     * {
     *     "meta": {
     *         "message": "OK",
     *         "errors": {}
     *     },
     *     "data": {
     *         "name": ".NET",
     *         "id": 1,
     *         "updated_at": "2020-11-24T11:35:00.977"
     *     }
     * }
     * @apiErrorExample Edit Conflict error:
     * HTTP/1.1 409 CONFLICT
     * {
     *     "meta": {
     *         "message": "Record already exists",
     *         "errors": {
     *             "name": {
     *                 "message": ".NET maybe already exist",
     *                 "code": "VAL_005"
     *             }
     *         }
     *     }
     * }
     * @apiErrorExample Edit Not Unprocessable Entity error:
     * HTTP/1.1 422 UNPROCESSABLE ENTITY
     * {
     *     "meta": {
     *         "message": "Record does not exist",
     *         "errors": {
     *             "id": {
     *                 "message": "id 100 does not exist",
     *                 "code": "VAL_012"
     *             }
     *         }
     *     }
     * }
     **/
    @PutMapping("/skills/{id}")
    public ResponseEntity<Response<SkillsDTO>> editSkill(@PathVariable("id") Integer id, @Valid @RequestBody SkillsDTO skillsDTO, BindingResult bindingResult) throws ViolatedException, ResourceNotFoundException, ConflictException, UnprocessableEntityException {
        Response<SkillsDTO> responseSkill = skillService.updateSkill(id, skillsDTO, bindingResult);
        return new ResponseEntity<>(responseSkill, HttpStatus.OK);
    }

    /**
     * @api {delete} /skills/:id Remove Skill
     * @apiName removeSkill
     * @apiGroup Skills
     * @apiParam {Integer} id Skill ID
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
    @DeleteMapping(path = "/skills/{id}")
    public ResponseEntity<SkillsDTO> removeSkill(@PathVariable("id") Integer id) throws UnprocessableEntityException {
        skillService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
