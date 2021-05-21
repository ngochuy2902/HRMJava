package com.nals.hrm.service;

import com.nals.hrm.model.Pagination;
import com.nals.hrm.model.Response;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResponseService {

    <D> Pagination<D> mapEntityPageIntoEntityPagination(Page<D> entities);

    <D, T> Response<D> dataResponse(Page<T> data, String status, Class<D> classDTO);

    <D, T> Response<D> dataResponseOne(T entity, String status, Class<D> classDTO);

    <D, T> Response<D> dataResponseList(List<T> entity, String status, Class<D> classDTO);

    <D, T> Response<D> dataResponseNoPagination(List<T> entity, String status);
}
