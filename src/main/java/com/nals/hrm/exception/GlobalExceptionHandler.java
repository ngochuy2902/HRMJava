package com.nals.hrm.exception;


import com.nals.hrm.model.ErrorCodes;
import com.nals.hrm.model.Errors;
import com.nals.hrm.model.Meta;
import com.nals.hrm.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex) {
        Response<?> responseException = getResponseExceptionIdNotFound(ex.getMessage(), ex.getId());
        logger.warn(responseException.getMeta().getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnprocessableEntityException.class})
    public ResponseEntity<?> unprocessableEntityException(UnprocessableEntityException ex) {
        Response<?> responseException = getResponseExceptionIdNotFound(ex.getMessage(), ex.getId());
        logger.warn(responseException.getMeta().getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex) {
        System.out.println(ex.getMessage());
        Response<?> responseException = responseError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), new HashMap<>());
        logger.warn(responseException.getMeta().getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ViolatedException.class)
    public ResponseEntity<?> handleViolatedException(ViolatedException ex) {
        Map<String, Errors> errors = setViolatedErrors(ex.getBindingResult().getFieldErrors());
        Response<?> responseException = responseError(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
        logger.warn(responseException.getMeta().getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<?> conflictException(ConflictException ex) {
        Map<String, Errors> errors = setConflictError(ex);
        Response<?> responseException = responseError(ex.getMessage(), errors);
        logger.warn(responseException.getMeta().getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.CONFLICT);
    }

    public Map<String, Errors> setConflictError(ConflictException ex) {
        Map<String, Errors> errorsMap = new HashMap<>();
        Set<String> set = ex.getCauses().keySet();
        for (String c : set) {
            Errors errors = new Errors();
            errors.setMessage(ex.getCauses().get(c) + " maybe already exist");
            errors.setCode(ErrorCodes.code.get("Conflict"));
            errorsMap.put(c.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase(), errors);
        }
        return errorsMap;
    }

    @ExceptionHandler({ConflictDayOffDataException.class})
    public ResponseEntity<?> conflictDayOffDataException(ConflictDayOffDataException ex) {
        Map<String, Errors> errors = setConflictDayOffError(ex);
        Response<?> responseException = responseError(ex.getMessage(), errors);
        logger.warn(responseException.getMeta().getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.CONFLICT);
    }

    public Map<String, Errors> setConflictDayOffError(ConflictDayOffDataException ex) {
        Map<String, Errors> errorsMap = new HashMap<>();
        Set<String> set = ex.getCauses().keySet();
        for (String c : set) {
            Errors errors = new Errors();
            if (c.split("\\.").length == 3) {
                if (c.split("\\.")[2].equals("vacation_type_id")) {
                    errors.setMessage(ex.getCauses().get(c));
                    errors.setCode(ErrorCodes.code.get("Min"));
                } else {
                    errors.setMessage(ex.getCauses().get(c) + " maybe already exist");
                    errors.setCode(ErrorCodes.code.get("Conflict"));
                }
            } else {
                if (c.split("\\.")[1].equals("vacation_type_id")) {
                    errors.setMessage(ex.getCauses().get(c));
                    errors.setCode(ErrorCodes.code.get("Min"));
                } else {
                    errors.setMessage(ex.getCauses().get(c) + " maybe already exist");
                    errors.setCode(ErrorCodes.code.get("Conflict"));
                }
            }

            errorsMap.put(c.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase(), errors);
        }
        return errorsMap;
    }



    public Map<String, Errors> setViolatedErrors(List<FieldError> errorList) {
        HashMap<String, Errors> errorMap = new HashMap<>();
        for (FieldError fieldError : errorList) {
            Errors errors = new Errors();
            errors.setMessage(fieldError.getDefaultMessage());
            errors.setCode(ErrorCodes.code.get(Objects.requireNonNull(fieldError.getCodes())[3]));
            if (errors.getCode() == null)
                errors.setCode(ErrorCodes.code.get(Objects.requireNonNull(fieldError.getCodes())[6]));
            String fieldErrorValue = fieldError.getField();
            fieldErrorValue = fieldErrorValue.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
            fieldErrorValue = fieldErrorValue.replaceAll("(\\[)([0-9]+)(])", ".$2");
            errorMap.put(fieldErrorValue, errors);
        }
        return errorMap;
    }

    public Response<?> responseError(String status, Map<String, Errors> errors) {
        Meta<?> metaException = new Meta<>(null, status, errors);
        return new Response<>(metaException, null);
    }

    public Response<?> getResponseExceptionIdNotFound(String message, Integer id) {
        Map<String, Errors> errors = new HashMap<>();
        Errors e = new Errors();
        e.setMessage("id " + id + " does not exist");
        e.setCode(ErrorCodes.code.get("NotFound"));
        errors.put("id", e);
        return responseError(message, errors);
    }

    @ExceptionHandler({UnsupportedMediaTypeException.class})
    public ResponseEntity<?> unsupportedMediaTypeException(UnsupportedMediaTypeException ex) {
        Response<?> responseException = responseError(ex.getMessage(), new HashMap<>());
        logger.warn(responseException.getMeta().getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({InvalidPathVariableException.class})
    public ResponseEntity<?> invalidPathVariableException(InvalidPathVariableException ex) {
        Response<?> responseException = responseError(ex.getMessage(), new HashMap<>());
        logger.warn(responseException.getMeta().getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.BAD_REQUEST);
    }
}
