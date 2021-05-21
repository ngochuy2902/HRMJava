package com.nals.hrm.service.impl;

import com.nals.hrm.dto.RequestDevicesDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.RequestDevices;
import com.nals.hrm.model.Response;
import com.nals.hrm.model.Users;
import com.nals.hrm.repository.RequestDeviceRepository;
import com.nals.hrm.repository.UserRepository;
import com.nals.hrm.service.RequestDeviceService;
import com.nals.hrm.service.ResponseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RequestDeviceServiceImpl implements RequestDeviceService {

    private final RequestDeviceRepository requestDeviceRepository;
    private final UserRepository userRepository;
    private final ResponseService responseService;
    private final ModelMapper modelMapper;

    @Autowired
    public RequestDeviceServiceImpl(RequestDeviceRepository requestDeviceRepository, UserRepository userRepository, ResponseService responseService, ModelMapper modelMapper) {
        this.requestDeviceRepository = requestDeviceRepository;
        this.userRepository = userRepository;
        this.responseService = responseService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response<RequestDevicesDTO> save(RequestDevicesDTO requestDeviceDTO, BindingResult bindingResult) throws ViolatedException{
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        if (requestDeviceDTO.getId() != null) {
            requestDeviceDTO.setId(null);
        }
        RequestDevices requestDevice = modelMapper.map(requestDeviceDTO, RequestDevices.class);
        Integer userId = requestDeviceDTO.getUserId();
        Users user = userRepository.findById(userId).get();
        requestDevice.setUser(user);
        requestDevice = requestDeviceRepository.save(requestDevice);
        return responseService.dataResponseOne(requestDevice, HttpStatus.CREATED.getReasonPhrase(), RequestDevicesDTO.class);
    }

    @Override
    public void delete(Integer id) throws UnprocessableEntityException {
        RequestDevices requestDevice = requestDeviceRepository.findByIdAndDeletedAtNull(id);
        if (requestDevice == null) {
            throw new UnprocessableEntityException("Record does not exist", id);
        }
        requestDevice.setDeletedAt(LocalDateTime.now());
        requestDeviceRepository.save(requestDevice);
    }

    @Override
    public Response<RequestDevicesDTO> findByRequestDevice(Pageable page, String fullName, String deviceName, List<Integer> status, LocalDate fromDate, LocalDate toDate) throws ResourceNotFoundException {
        List<Users> usersList = userRepository.findAllByFullNameContainingAndDeletedAtNull(fullName);
        Page<RequestDevices> requestDevicesList = requestDeviceRepository.findByUserInAndDeviceNameContainingAndStatusInAndExpectedDeliveryDateGreaterThanEqualAndAndExpectedDeliveryDateLessThanEqualAndDeletedAtNull(page, usersList, deviceName, status, fromDate, toDate);

        List<RequestDevicesDTO> requestDevicesDTOList = new ArrayList<>();

        for (RequestDevices requestDevices : requestDevicesList.getContent()) {
            RequestDevicesDTO requestDevicesDTO = modelMapper.map(requestDevices, RequestDevicesDTO.class);
            requestDevicesDTO.setFullName(requestDevices.getUser().getFullName());
            requestDevicesDTO.setEmail(requestDevices.getUser().getEmail());
            requestDevicesDTOList.add(requestDevicesDTO);
        }
        Page<RequestDevicesDTO> requestDevicesDTOPage = new PageImpl<>(requestDevicesDTOList, page, requestDevicesList.getTotalElements());
        return responseService.dataResponse(requestDevicesDTOPage, HttpStatus.OK.getReasonPhrase(), RequestDevicesDTO.class);
    }

    @Override
    public Response<RequestDevicesDTO> findById(Integer id) throws ResourceNotFoundException {
        RequestDevices requestDevices = requestDeviceRepository.findByIdAndDeletedAtNull(id);
        if (requestDevices == null) {
            throw new ResourceNotFoundException("Record does not exist", id);
        }
        Users users = userRepository.findByIdAndDeletedAtNull(requestDevices.getUser().getId());
        if (users == null) {
            throw new ResourceNotFoundException("Record does not exist", id);
        }
        RequestDevicesDTO requestDevicesDTO = modelMapper.map(requestDevices, RequestDevicesDTO.class);
        requestDevicesDTO.setFullName(users.getFullName());
        requestDevicesDTO.setEmail(users.getEmail());
        return responseService.dataResponseOne(requestDevicesDTO, HttpStatus.OK.getReasonPhrase(), RequestDevicesDTO.class);
    }

    @Override
    public Response<RequestDevicesDTO> update(RequestDevicesDTO requestDeviceDTO, Integer id, BindingResult bindingResult) throws ViolatedException, ConflictException, UnprocessableEntityException {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        RequestDevices requestDevices = findByIdAndDeletedAtNull(id);
        if(requestDevices == null) {
            throw new UnprocessableEntityException("Record does not exist", id);
        }
        LocalDateTime oldUpdateAt = requestDevices.getUpdatedAt();

        requestDevices.setProjectName(requestDeviceDTO.getProjectName());
        requestDevices.setExpectedDeliveryDate(requestDeviceDTO.getExpectedDeliveryDate());
        requestDevices.setDeviceName(requestDeviceDTO.getDeviceName());
        requestDevices.setNumberDevice(requestDeviceDTO.getNumberDevice());
        requestDevices.setTechnicalSpecification(requestDeviceDTO.getTechnicalSpecification());
        requestDevices.setReason(requestDeviceDTO.getReason());

        requestDeviceRepository.save(requestDevices);
        if (requestDevices.getUpdatedAt().equals(oldUpdateAt)) {
            throw new ConflictException("Record already exists", new HashMap<>());
        }
        return responseService.dataResponseOne(requestDevices, HttpStatus.OK.getReasonPhrase(), RequestDevicesDTO.class);
    }

    @Override
    public RequestDevices findByIdAndDeletedAtNull(Integer id) {
        return requestDeviceRepository.findByIdAndDeletedAtNull(id);
    }
}
