package com.nals.hrm.service.impl;


import com.nals.hrm.dto.DevicesDTO;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.*;
import com.nals.hrm.repository.DeviceLogRepository;
import com.nals.hrm.repository.DeviceRepository;
import com.nals.hrm.repository.UserRepository;
import com.nals.hrm.service.DeviceService;
import com.nals.hrm.service.ResponseService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final ResponseService responseService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final DeviceLogRepository deviceLogRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, ResponseService responseService, ModelMapper modelMapper, UserRepository userRepository, DeviceLogRepository deviceLogRepository) {
        this.deviceRepository = deviceRepository;
        this.responseService = responseService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.deviceLogRepository = deviceLogRepository;
    }

    @Override
    public Response<DevicesDTO> getAllDevice(Pageable page, String name, String holder, Boolean status,
                                             LocalDate dateForm, LocalDate dateTo, String sort) {
        Pageable paging = PageRequest.of(page.getPageNumber()-1, 10, Sort.by(sort));
        Page<Devices> devices = getDeviceSpecification(paging, name, holder, status, dateForm, dateTo);
        if (devices.isEmpty()) {
            Meta<DevicesDTO> dataMeta = new Meta<>(null, HttpStatus.OK.getReasonPhrase(), new HashMap<>());
            return new Response<>(dataMeta, null);
        }
        Page<DevicesDTO> pageDTO = devices.map(objectEntity -> modelMapper.map(objectEntity, DevicesDTO.class));
        List<Devices> deviceList = devices.getContent();
        List<DevicesDTO> devicesDTOList = pageDTO.getContent();
        for (int i = 0; i < deviceList.size(); i++) {
            Users users = deviceList.get(i).getUser();
            if (users != null) {
                UserRes userRes = modelMapper.map(users, UserRes.class);
                devicesDTOList.get(i).setUserRes(userRes);
            }
        }
        Pagination<DevicesDTO> dataPagination = responseService.mapEntityPageIntoEntityPagination(pageDTO);
        Meta<DevicesDTO> dataMeta = new Meta<>(dataPagination, HttpStatus.OK.getReasonPhrase(), new HashMap<>());
        return new Response<>(dataMeta, devicesDTOList);
    }

    @Override
    public Response<DevicesDTO> getHolidayById(Integer id) throws ResourceNotFoundException {
        Devices device = deviceRepository.findByIdAndDeletedAtNull(id);
        if (device == null) {
            throw new ResourceNotFoundException("Device does not exist", id);
        }
        return responseService.dataResponseOne(device, HttpStatus.OK.getReasonPhrase(), DevicesDTO.class);
    }

    @Override
    public Response<DevicesDTO> save(DevicesDTO deviceDTO, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        if (deviceDTO.getId() != null) {
            deviceDTO.setId(null);
        }
        Devices device = modelMapper.map(deviceDTO, Devices.class);
        Devices devicesResponse = deviceRepository.save(device);
        return responseService.dataResponseOne(devicesResponse, HttpStatus.OK.getReasonPhrase(), DevicesDTO.class);
    }

    @Override
    public Response<DevicesDTO> updateDevice(Integer id, DevicesDTO deviceDTO, BindingResult bindingResult)
            throws ViolatedException, ResourceNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        Devices device = deviceRepository.findByIdAndDeletedAtNull(id);
        if (device == null) {
            throw new ResourceNotFoundException("Device does not exist", id);
        }
        Integer userIdDTO =  deviceDTO.getUserId();
        if (userIdDTO != null ) {
            Users user = userRepository.findByIdAndDeletedAtNull(deviceDTO.getUserId());
            device.setUser(user);
        }
        deviceDTO.setId(id);
        DeviceLogs deviceLog = modelMapper.map(device, DeviceLogs.class);
        deviceLog.setId(null);
        deviceLog.setDevice(device);
        deviceLogRepository.save(deviceLog);
        Devices devicesMapping = modelMapper.map(deviceDTO, Devices.class);
        devicesMapping.setUser(device.getUser());
        Devices devicesResponse = deviceRepository.save(devicesMapping);
        return responseService.dataResponseOne(devicesResponse, HttpStatus.OK.getReasonPhrase(), DevicesDTO.class);
    }

    @Override
    public void deleteDevice(Integer id) throws UnprocessableEntityException {
        Devices device = deviceRepository.findByIdAndDeletedAtNull(id);
        if (device == null) {
            throw new UnprocessableEntityException("Device does not exist", id);
        }
        device.setDeletedAt(LocalDateTime.now());
        deviceRepository.save(device);
    }

    public Page<Devices> getDeviceSpecification(Pageable page, String name, String holder, Boolean status, LocalDate dateForm, LocalDate dateTo) {
        Specification<Devices> devices = (Specification<Devices>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.isNull(root.get("deletedAt")));
            if (status != null) {
                list.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (StringUtils.isNotBlank(name)) {
                list.add(criteriaBuilder.like(root.get("deviceName"), "%" + name + "%"));
            }
            if (StringUtils.isNotBlank(holder)) {
                list.add(criteriaBuilder.like(root.get("user").get("fullName"), "%" + holder + "%"));
            }
            if (dateForm != null && dateTo != null) {
                list.add(criteriaBuilder.between(root.get("inputDate"), dateForm, dateTo));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        return deviceRepository.findAll(devices, page);
    }
}
