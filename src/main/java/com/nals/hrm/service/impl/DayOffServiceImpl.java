package com.nals.hrm.service.impl;

import com.nals.hrm.dto.DaysOffDTO;
import com.nals.hrm.exception.ConflictDayOffDataException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.*;
import com.nals.hrm.repository.DayOffRepository;
import com.nals.hrm.repository.UserRepository;
import com.nals.hrm.repository.VacationTypeRepository;
import com.nals.hrm.service.DayOffService;
import com.nals.hrm.service.EmailService;
import com.nals.hrm.service.ResponseService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DayOffServiceImpl implements DayOffService {

    private final DayOffRepository dayOffRepository;
    private final ResponseService responseService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final VacationTypeRepository vacationTypeRepository;
    private final EmailService emailService;

    @Autowired
    public DayOffServiceImpl(DayOffRepository dayOffRepository, ResponseService responseService, ModelMapper modelMapper, UserRepository userRepository, VacationTypeRepository vacationTypeRepository, EmailService emailService) {
        this.dayOffRepository = dayOffRepository;
        this.responseService = responseService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.vacationTypeRepository = vacationTypeRepository;
        this.emailService = emailService;
    }

    @Override
    public Response<DaysOffDTO> findAllByDayOff(int page, String fullName, LocalDate dateOffFrom, LocalDate dateOffTo, String sort, String direct, int pageSize, Integer userId) {
        Pageable pagination = getPagination(page, pageSize, direct, sort);
        Page<DaysOff> daysOff = getDayOffSpecification(pagination, fullName, dateOffFrom, dateOffTo, userId);
        Page<DaysOffDTO> pageDTO = daysOff.map(objectEntity -> modelMapper.map(objectEntity, DaysOffDTO.class));
        List<DaysOff> dayOffList = daysOff.getContent();
        List<DaysOffDTO> dayOffDTOList = pageDTO.getContent();
        for (int i = 0; i < dayOffList.size(); i++) {
            Users users = dayOffList.get(i).getUser();
            if (users != null) {
                dayOffDTOList.get(i).setEmail(users.getEmail());
                dayOffDTOList.get(i).setFullName(users.getFullName());
            }
        }
        Pagination<DaysOffDTO> dataPagination = responseService.mapEntityPageIntoEntityPagination(pageDTO);
        Meta<DaysOffDTO> dataMeta = new Meta<>(dataPagination, HttpStatus.OK.getReasonPhrase(), new HashMap<>());
        return new Response<>(dataMeta, dayOffDTOList);
    }

    @Override
    public void delete(Integer id) throws UnprocessableEntityException {
        DaysOff dayOff = dayOffRepository.findByIdAndDeletedAtNull(id);
        if (dayOff == null)
            throw new UnprocessableEntityException("Record does not exist", id);
        Users user = userRepository.findByIdAndDeletedAtNull(dayOff.getUser().getId());
        double sessionDayOffValue = (dayOff.getSessionDayOff()==1) ? 1.0 : 0.5;
        if (dayOff.getVacationType().getId()==1) {
            user.setRemainingDaysOff(user.getRemainingDaysOff() + sessionDayOffValue);
        }
        userRepository.save(user);
        dayOff.setActive(false);
        dayOff.setDeletedAt(LocalDateTime.now());
        dayOffRepository.save(dayOff);
    }

    @Override
    public Response<DaysOffDTO> save(DaysOffDTO daysOffDTO, BindingResult bindingResult) throws ViolatedException, MessagingException, ConflictDayOffDataException {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        checkValidDataInputCreateDayOff(daysOffDTO);
        Users users = userRepository.findByIdAndDeletedAtNull(daysOffDTO.getUserId());
        List<DaysOffDTO> daysOffDTOList = new ArrayList<>();
        for (int i=0; i<daysOffDTO.getDaysOffLists().size(); i++) {
            LocalDate vacationDay = daysOffDTO.getDaysOffLists().get(i).getVacationDay();
            int sessionDayOff = daysOffDTO.getDaysOffLists().get(i).getSessionDayOffId();
            int vacationTypeId = daysOffDTO.getDaysOffLists().get(i).getVacationTypeId();

            if (daysOffDTO.getId() != null) {
                daysOffDTO.setId(null);
            }
            DaysOff daysOff;
            VacationTypes vacationTypes = vacationTypeRepository.findByIdAndDeletedAtNull(vacationTypeId);
            daysOff = modelMapper.map(daysOffDTO, DaysOff.class);
            daysOff.setUser(users);
            daysOff.setVacationDay(vacationDay);
            daysOff.setSessionDayOff(sessionDayOff);
            daysOff.setVacationType(vacationTypes);
            daysOff.setActive(true);
            daysOff = dayOffRepository.save(daysOff);

            double sessionDayOffValue = (sessionDayOff == 1) ? 1.0 : 0.5;
            double remainingDaysOffValueAfterUpdated = users.getRemainingDaysOff() - sessionDayOffValue;
            if (vacationTypeId == 1) users.setRemainingDaysOff(remainingDaysOffValueAfterUpdated);
            userRepository.save(users);
            DaysOffDTO daysOffDTOResult = modelMapper.map(daysOff, DaysOffDTO.class);
            daysOffDTOResult.setFullName(users.getFullName());
            daysOffDTOResult.setEmail(users.getEmail());
            daysOffDTOResult.setVacationTypeName(vacationTypes.getName());
            daysOffDTOResult.setSessionDayOffName(sessionDayOff == 1 ? "Full day" : (sessionDayOff == 2 ? "Morning" : "Afternoon"));

            daysOffDTOList.add(daysOffDTOResult);
        }

        emailService.sendEmail(
                daysOffDTOList.get(0).getPoEmail(), daysOffDTOList.get(0).getEmail(), "Create Day Off for user: " + daysOffDTOList.get(0).getFullName(),
                emailService.mailContent(daysOffDTOList)
        );

        return responseService.dataResponseNoPagination(daysOffDTOList, HttpStatus.CREATED.getReasonPhrase());
    }

    @Override
    public Response<DaysOffDTO> findById(Integer id) throws ResourceNotFoundException {
        DaysOff daysOff = dayOffRepository.findByIdAndDeletedAtNull(id);
        if (daysOff == null) {
            throw new ResourceNotFoundException("Record does not exist", id);
        }
        DaysOffDTO daysOffDTO = modelMapper.map(daysOff, DaysOffDTO.class);
        Users users = userRepository.findByIdAndDeletedAtNull(daysOff.getUser().getId());
        daysOffDTO.setFullName(users.getFullName());
        daysOffDTO.setEmail(users.getEmail());
        return responseService.dataResponseOne(daysOffDTO, HttpStatus.OK.getReasonPhrase(), DaysOffDTO.class);
    }

    @Override
    public Response<DaysOffDTO> update(Integer id, DaysOffDTO daysOffDTO, BindingResult bindingResult) throws ViolatedException, UnprocessableEntityException, MessagingException, ConflictDayOffDataException {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        DaysOff oldDayOff = dayOffRepository.findByIdAndDeletedAtNull(id);
        if (oldDayOff == null) {
            throw new UnprocessableEntityException("Record does not exist", id);
        }

        daysOffDTO.setId(id);
        checkValidDataInputUpdateDayOff(oldDayOff, daysOffDTO);

        Users users = userRepository.findByIdAndDeletedAtNull(oldDayOff.getUser().getId());
        DaysOff newDayOff = new DaysOff();
        BeanUtils.copyProperties(oldDayOff, newDayOff);
        VacationTypes vacationTypes = vacationTypeRepository.findByIdAndDeletedAtNull(daysOffDTO.getDaysOffLists().get(0).getVacationTypeId());
        newDayOff.setPoName(daysOffDTO.getPoName());
        newDayOff.setPoEmail(daysOffDTO.getPoEmail());
        newDayOff.setVacationDay(daysOffDTO.getDaysOffLists().get(0).getVacationDay());
        newDayOff.setSessionDayOff(daysOffDTO.getDaysOffLists().get(0).getSessionDayOffId());
        newDayOff.setVacationType(vacationTypes);
        newDayOff.setReasons(daysOffDTO.getReasons());
        newDayOff.setNotes(daysOffDTO.getNotes());

        int oldDayOffVacationTypeId = oldDayOff.getVacationType().getId();
        int newDayOffVacationTypeId = newDayOff.getVacationType().getId();
        dayOffRepository.save(newDayOff);

        if (oldDayOffVacationTypeId == 1 && newDayOffVacationTypeId == 1) {
            double sessionNewDayOffValue = (newDayOff.getSessionDayOff() == 1) ? 1.0 : 0.5;
            double sessionOldDayOffValue = (oldDayOff.getSessionDayOff() == 1) ? 1.0 : 0.5;
            users.setRemainingDaysOff(users.getRemainingDaysOff() + sessionOldDayOffValue - sessionNewDayOffValue);
            userRepository.save(users);
        }
        else if (oldDayOffVacationTypeId != 1 && newDayOffVacationTypeId == 1) {
            double sessionNewDayOffValue = (newDayOff.getSessionDayOff() == 1) ? 1.0 : 0.5;
            users.setRemainingDaysOff(users.getRemainingDaysOff() - sessionNewDayOffValue);
            userRepository.save(users);
        }
        else if (oldDayOffVacationTypeId == 1 && newDayOffVacationTypeId != 1) {
            double sessionOldDayOffValue = (oldDayOff.getSessionDayOff() == 1) ? 1.0 : 0.5;
            users.setRemainingDaysOff(users.getRemainingDaysOff() + sessionOldDayOffValue);
            userRepository.save(users);
        }

        DaysOffDTO daysOffDTOResponse = modelMapper.map(newDayOff, DaysOffDTO.class);
        daysOffDTOResponse.setFullName(newDayOff.getUser().getFullName());
        daysOffDTOResponse.setEmail(newDayOff.getUser().getEmail());
        daysOffDTOResponse.setVacationTypeName(vacationTypes.getName());
        int sessionDayOff = newDayOff.getSessionDayOff();
        daysOffDTOResponse.setSessionDayOffName(sessionDayOff == 1 ? "Full day" : (sessionDayOff == 2 ? "Morning" : "Afternoon"));

        List<DaysOffDTO> daysOffDTOList = new ArrayList<>();
        daysOffDTOList.add(daysOffDTOResponse);

        if (!oldDayOff.getUpdatedAt().equals(newDayOff.getUpdatedAt())) {
            emailService.sendEmail(
                    daysOffDTOResponse.getPoEmail(), daysOffDTOResponse.getEmail(), "Update Day Off for user: " + daysOffDTOResponse.getFullName(),
                    emailService.mailContent(daysOffDTOList)
            );
        }

        return responseService.dataResponseOne(daysOffDTOResponse, HttpStatus.OK.getReasonPhrase(), DaysOffDTO.class);
    }

    public Page<DaysOff> getDayOffSpecification(Pageable page, String fullName, LocalDate dateOffFrom, LocalDate dateOffTo, Integer userId) {
        Specification<DaysOff> dayOff = (Specification<DaysOff>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.isNull(root.get("deletedAt")));
            if (StringUtils.isNotBlank(fullName)) {
                list.add(criteriaBuilder.like(root.get("user").get("fullName"), "%" + fullName + "%"));
            }
            if (dateOffFrom != null && dateOffTo != null) {
                list.add(criteriaBuilder.between(root.get("vacationDay"), dateOffFrom, dateOffTo));
            }
            if (dateOffFrom != null && dateOffTo == null) {
                list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("vacationDay"), dateOffFrom));
            }
            if (dateOffFrom == null && dateOffTo != null) {
                list.add(criteriaBuilder.lessThanOrEqualTo(root.get("vacationDay"), dateOffTo));
            }
            if (userId != null) {
                list.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        return dayOffRepository.findAll(dayOff, page);
    }

    public Pageable getPagination(int page, int sizePage, String direct, String sort) {
        if (sort.equals("fullName")) {
            sort = "user.fullName";
        }
        Sort sortList = Sort.by(sort).descending();
        if (direct.equals("asc")) {
            sortList = Sort.by(sort).ascending();
        }
        return PageRequest.of(--page, sizePage, sortList);
    }

    public List<Integer> checkConflictRequestData(DaysOffDTO daysOffDTO) {
        List<Integer> result = new ArrayList<>();
        for (int i=0; i<daysOffDTO.getDaysOffLists().size()-1; i++) {
            for (int j=i+1; j<daysOffDTO.getDaysOffLists().size(); j++) {
                LocalDate vacationDayI = daysOffDTO.getDaysOffLists().get(i).getVacationDay();
                LocalDate vacationDayJ = daysOffDTO.getDaysOffLists().get(j).getVacationDay();
                int sessionDayI = daysOffDTO.getDaysOffLists().get(i).getSessionDayOffId();
                int sessionDayJ = daysOffDTO.getDaysOffLists().get(j).getSessionDayOffId();
                if (vacationDayI.equals(vacationDayJ)) {
                    if (sessionDayI == sessionDayJ || sessionDayI == 1 || sessionDayJ == 1) {
                        result.add(j);
                    }
                }
            }
        }
        return result;
    }

    public boolean checkDuplicateDayOffAdd(Users users, LocalDate vacationDay, Integer sessionDayOff) {
        DaysOff daysOff = dayOffRepository.findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(users, vacationDay, sessionDayOff);
        DaysOff daysOffFullDaySession = dayOffRepository.findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(users, vacationDay, 1);
        DaysOff daysOffMorningSession = dayOffRepository.findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(users, vacationDay, 2);
        DaysOff daysOffAfternoonSession = dayOffRepository.findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(users, vacationDay, 3);
        return (daysOff != null)
                || daysOffFullDaySession != null
                || ((daysOffMorningSession != null || daysOffAfternoonSession != null) && sessionDayOff == 1);
    }

    public boolean checkDuplicateDayOffUpdate(Integer dayOffId, Users users, LocalDate vacationDay, Integer sessionDayOff) {
        DaysOff daysOff = dayOffRepository.findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(users, vacationDay, sessionDayOff);
        DaysOff daysOffFullDaySession = dayOffRepository.findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(users, vacationDay, 1);
        DaysOff daysOffMorningSession = dayOffRepository.findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(users, vacationDay, 2);
        DaysOff daysOffAfternoonSession = dayOffRepository.findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(users, vacationDay, 3);
        return (daysOff != null && !daysOff.getId().equals(dayOffId))
                || (daysOffFullDaySession != null && !daysOffFullDaySession.getId().equals(dayOffId))
                || (daysOffMorningSession != null && sessionDayOff == 1 && !daysOffMorningSession.getId().equals(dayOffId))
                || (daysOffAfternoonSession != null && sessionDayOff == 1 && !daysOffAfternoonSession.getId().equals(dayOffId));
    }

    public void checkValidDataInputCreateDayOff(DaysOffDTO daysOffDTO) throws ConflictDayOffDataException {
        HashMap<String, String> responseConflictData = new HashMap<>();
        if (checkConflictRequestData(daysOffDTO).size() != 0) {
            for (Integer i : checkConflictRequestData(daysOffDTO)) {
                setResponseConflictData(daysOffDTO, responseConflictData, i);
            }
            throw new ConflictDayOffDataException("Conflict Request data", responseConflictData);
        }

        Users users = userRepository.findByIdAndDeletedAtNull(daysOffDTO.getUserId());
        double remainingDaysOffValueAfterUpdated = users.getRemainingDaysOff();
        for (int i=0; i<daysOffDTO.getDaysOffLists().size(); i++) {
            LocalDate vacationDay = daysOffDTO.getDaysOffLists().get(i).getVacationDay();
            int sessionDayOff = daysOffDTO.getDaysOffLists().get(i).getSessionDayOffId();
            int vacationTypeId = daysOffDTO.getDaysOffLists().get(i).getVacationTypeId();
            double sessionDayOffValue = (sessionDayOff == 1) ? 1.0 : 0.5;
            if (vacationTypeId == 1) {
                remainingDaysOffValueAfterUpdated -= sessionDayOffValue;
                if (remainingDaysOffValueAfterUpdated < 0) {
                    responseConflictData.put("days_off_lists." + i + ".vacation_type_id", "Not enough remaining day off for Annual leave");
                }
            }

            if (checkDuplicateDayOffAdd(users, vacationDay, sessionDayOff)) {
                setResponseConflictData(daysOffDTO, responseConflictData, i);
            }
        }

        if (responseConflictData.size() != 0) {
            throw new ConflictDayOffDataException("Conflict Database data", responseConflictData);
        }
    }

    public void setResponseConflictData(DaysOffDTO daysOffDTO, HashMap<String, String> responseConflictData, Integer i) {
        responseConflictData.put("days_off_lists." + i + ".vacation_day", daysOffDTO.getDaysOffLists().get(i).getVacationDay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        responseConflictData.put("days_off_lists." + i + ".session_day_id", (daysOffDTO.getDaysOffLists().get(i).getSessionDayOffId()==1)? "Full day" : (daysOffDTO.getDaysOffLists().get(i).getSessionDayOffId() == 2 ? "Morning" : "Afternoon"));
    }

    public void checkValidDataInputUpdateDayOff(DaysOff oldDayOff, DaysOffDTO newDaysOffDTO) throws ConflictDayOffDataException {
        HashMap<String, String> responseConflictDatabaseData = new HashMap<>();
        Users users = userRepository.findByIdAndDeletedAtNull(newDaysOffDTO.getUserId());
        if (checkDuplicateDayOffUpdate(newDaysOffDTO.getId(), users, newDaysOffDTO.getDaysOffLists().get(0).getVacationDay(), newDaysOffDTO.getDaysOffLists().get(0).getSessionDayOffId())) {
            responseConflictDatabaseData.put("days_off_lists.vacation_day", newDaysOffDTO.getDaysOffLists().get(0).getVacationDay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            responseConflictDatabaseData.put("days_off_lists.session_day_id", (newDaysOffDTO.getDaysOffLists().get(0).getSessionDayOffId()==1)? "Full day" : (newDaysOffDTO.getDaysOffLists().get(0).getSessionDayOffId() == 2 ? "Morning" : "Afternoon"));
        }

        if (oldDayOff.getVacationType().getId() == 1 && newDaysOffDTO.getDaysOffLists().get(0).getVacationTypeId() == 1) {
            double sessionDayOffValue = (oldDayOff.getSessionDayOff() == 1) ? 1.0 : 0.5;
            double sessionDayOffDTOValue = (newDaysOffDTO.getDaysOffLists().get(0).getSessionDayOffId() == 1) ? 1.0 : 0.5;
            double remainingDaysOffValueAfterUpdated = users.getRemainingDaysOff() + sessionDayOffValue - sessionDayOffDTOValue;
            if (remainingDaysOffValueAfterUpdated < 0) {
                responseConflictDatabaseData.put("days_off_lists.vacation_type_id", "Not enough remaining day off for Annual leave");
            }
        }
        else if (oldDayOff.getVacationType().getId() != 1 && newDaysOffDTO.getDaysOffLists().get(0).getVacationTypeId() == 1) {
            double sessionDayOffDTOValue = (newDaysOffDTO.getDaysOffLists().get(0).getSessionDayOffId() == 1) ? 1.0 : 0.5;
            double remainingDaysOffValueAfterUpdated = users.getRemainingDaysOff() - sessionDayOffDTOValue;
            if (remainingDaysOffValueAfterUpdated < 0) {
                responseConflictDatabaseData.put("days_off_lists.vacation_type_id", "Not enough remaining day off for Annual leave");
            }
        }

        if (responseConflictDatabaseData.size() != 0) {
            throw new ConflictDayOffDataException("Conflict Database data", responseConflictDatabaseData);
        }
    }
}
