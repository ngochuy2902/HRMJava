package com.nals.hrm.service.impl;

import com.nals.hrm.model.Meta;
import com.nals.hrm.model.Pagination;
import com.nals.hrm.model.Response;
import com.nals.hrm.service.ResponseService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {

    private final ModelMapper modelMapper;

    @Autowired
    public ResponseServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public <D> Pagination<D> mapEntityPageIntoEntityPagination(Page<D> entities) {
        Pagination<D> pagination = new Pagination<>();
        int currentPage = entities.getNumber()+1;
        Long total = entities.getTotalElements();
        int perPage = entities.getPageable().getPageSize();
        int lastPage = entities.getTotalPages();
        pagination.setCurrentPage(currentPage);
        pagination.setTotal(total);
        pagination.setPerPage(perPage);
        pagination.setLastPage(lastPage);
        return pagination;
    }

    @Override
    public <D, T> Response<D> dataResponse(Page<T> data, String status, Class<D> classDTO) {
        Page<D> pageDTO = data.map(objectEntity -> modelMapper.map(objectEntity, classDTO));
        Pagination<D> dataPagination = mapEntityPageIntoEntityPagination(pageDTO);
        Meta<D> dataMeta = new Meta<>(dataPagination, status, new HashMap<>());
        return new Response<>(dataMeta, pageDTO.getContent());
    }

    @Override
    public <D, T> Response<D> dataResponseOne(T entity, String status, Class<D> classDTO) {
        D data = modelMapper.map(entity, classDTO);
        Meta<D> dataMeta = new Meta<>(null, status, new HashMap<>());
        return new Response<>(dataMeta, data);
    }

    @Override
    public <D, T> Response<D> dataResponseNoPagination(List<T> entity, String status) {
        Meta<D> dataMeta = new Meta<>(null, status, new HashMap<>());
        return new Response<>(dataMeta, entity);
    }

    @Override
    public <D, T> Response<D> dataResponseList(List<T> entity, String status,Class<D> classDTO) {
        List<D> entityDTOList = new ArrayList<>();
        for(T data : entity){
            D dataTO = modelMapper.map(data,classDTO);
            entityDTOList.add(dataTO);
        }
        Meta<D> dataMeta = new Meta<>(null, status, new HashMap<>());
        return new Response<>(dataMeta, entityDTOList);
    }
}
