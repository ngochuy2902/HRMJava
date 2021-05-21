package com.nals.hrm.service;

import com.nals.hrm.dto.DaysOffDTO;
import com.nals.hrm.exception.*;
import com.nals.hrm.model.*;
import com.nals.hrm.repository.DayOffRepository;
import com.nals.hrm.repository.UserRepository;
import com.nals.hrm.repository.VacationTypeRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@SpringBootTest
class DayOffServiceTest {

    @MockBean
    DayOffRepository dayOffRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    VacationTypeRepository vacationTypeRepository;

    @Autowired
    private DayOffService dayOffService;

    @BeforeEach
    public void onSetUp() {
        Integer errorId = 1;
        Integer successId = 2;
        DaysOff dayOff = prepareDaysOff();
        Users user = prepareUsers();
        VacationTypes vacationTypes = prepareVacationType();

        Mockito.when(userRepository.findByIdAndDeletedAtNull(successId)).thenReturn(user);
        Mockito.when(userRepository.save(any(Users.class))).thenReturn(user);

        Mockito.when(dayOffRepository.findByIdAndDeletedAtNull(errorId)).thenReturn(null);
        Mockito.when(dayOffRepository.save(any(DaysOff.class))).thenReturn(dayOff);
        Mockito.when(dayOffRepository.findByIdAndDeletedAtNull(successId)).thenReturn(dayOff);

        Mockito.when(vacationTypeRepository.findByIdAndDeletedAtNull(2)).thenReturn(vacationTypes);
        Mockito.when(dayOffRepository.findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(user, LocalDate.parse("2020-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")), 1)).thenReturn(dayOff);
    }

    @Test
    public void giveDaysOffDTO_whenDaysOffDTOHaveConflictData_thenReturnConflictException() {
        DaysOffDTO daysOffDTO = prepareDaysOffDTOConflictRequestData();
        ConflictDayOffDataException conflictException = null;
        try {
            dayOffService.save(daysOffDTO, bindingResult);
        } catch (ViolatedException | MessagingException | ConflictDayOffDataException e) {
            assert e instanceof ConflictDayOffDataException;
            conflictException = (ConflictDayOffDataException) e;
        }
        Assert.assertNotNull(conflictException);
    }

    @Test
    public void giveDaysOffDTO_whenUsersNotHaveEnoughRemainingDayOff_returnConflictDayOffDataException() {
        DaysOffDTO daysOffDTO = prepareDaysOffDTONotEnoughRemainingDayOff();
        ConflictDayOffDataException conflictDayOffDataException = null;
        try {
            dayOffService.save(daysOffDTO, bindingResult);
        } catch (ViolatedException | MessagingException | ConflictDayOffDataException e) {
            assert e instanceof ConflictDayOffDataException;
            conflictDayOffDataException = (ConflictDayOffDataException) e;
        }
        Assert.assertNotNull(conflictDayOffDataException);
    }

    @Test
    public void giveDaysOffDTO_whenDaysOffDTOHaveConflictDatabaseData_thenReturnConflictException() {
        DaysOffDTO daysOffDTO = prepareDaysOffDTOConflictDatabaseData();
        ConflictDayOffDataException conflictException = null;
        try {
            dayOffService.save(daysOffDTO, bindingResult);
        } catch (ViolatedException | MessagingException | ConflictDayOffDataException e) {
            assert e instanceof ConflictDayOffDataException;
            conflictException = (ConflictDayOffDataException) e;
        }
        Assert.assertNotNull(conflictException);
    }

    @Test
    public void giveDaysOffDTO_whenDaysOffDTOSuccessData_thenReturnSuccess() throws ViolatedException, MessagingException, ConflictDayOffDataException {
        DaysOffDTO daysOffDTO = prepareDaysOffDTOSuccessData();
        Response<DaysOffDTO> daysOffDTOResponse = dayOffService.save(daysOffDTO, bindingResult);
        Assert.assertNotNull(daysOffDTOResponse.getData());
    }

    @Test
    public void  givenDaysOff_whenDeleteDayOffWithIdNotExist_thenReturnUnprocessableEntityError() {
        Integer id = 1;
        UnprocessableEntityException unprocessableEntityException = null;
        try {
            dayOffService.delete(id);
        } catch (UnprocessableEntityException exception) {
            unprocessableEntityException = exception;
        }
        assert unprocessableEntityException != null;
        Assert.assertEquals("Record does not exist",unprocessableEntityException.getMessage());
    }

    @Test
    public void givenDaysOff_whenDeleteDayOffWithIdExist_thenDoNothing() throws UnprocessableEntityException {
        Integer id = 2;
        dayOffService.delete(id);
        Mockito.verify(dayOffRepository,times(1)).save(any(DaysOff.class));
        Mockito.verify(userRepository,times(1)).save(any(Users.class));
    }

    public DaysOff prepareDaysOff() {
        DaysOff dayOff = new DaysOff();
        Users user = prepareUsers();
        dayOff.setId(2);
        dayOff.setPoName("Giang Truong");
        dayOff.setPoEmail("huyhn@nal.vn");
        dayOff.setVacationDay(LocalDate.parse("06/12/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dayOff.setSessionDayOff(1);
        dayOff.setVacationType(prepareVacationType());
        dayOff.setReasons("flu");
        dayOff.setNotes("abc");
        dayOff.setActive(false);
        dayOff.setUser(user);
        return dayOff;
    }

    public DaysOffDTO prepareDaysOffDTOConflictRequestData() {
        DaysOffDTO daysOffDTO = new DaysOffDTO();
        daysOffDTO.setPoName("PO Name");
        daysOffDTO.setPoEmail("po@nal.vn");
        DaysOffList daysOffList = new DaysOffList();
        daysOffList.setVacationDay(LocalDate.parse("06/12/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        daysOffList.setSessionDayOffId(1);
        daysOffList.setVacationTypeId(2);
        List<DaysOffList> daysOffLists = new ArrayList<>();
        daysOffLists.add(daysOffList);
        daysOffList.setVacationDay(LocalDate.parse("07/12/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        daysOffList.setSessionDayOffId(1);
        daysOffList.setVacationTypeId(2);
        daysOffLists.add(daysOffList);
        daysOffDTO.setDaysOffLists(daysOffLists);
        daysOffDTO.setReasons("full");
        daysOffDTO.setNotes("abc");
        daysOffDTO.setUserId(2);
        return daysOffDTO;
    }

    public DaysOffDTO prepareDaysOffDTONotEnoughRemainingDayOff() {
        DaysOffDTO daysOffDTO = new DaysOffDTO();
        daysOffDTO.setPoName("PO Name");
        daysOffDTO.setPoEmail("po@nal.vn");
        DaysOffList daysOffList = new DaysOffList();
        daysOffList.setVacationDay(LocalDate.parse("06/12/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        daysOffList.setSessionDayOffId(1);
        daysOffList.setVacationTypeId(1);
        List<DaysOffList> daysOffLists = new ArrayList<>();
        daysOffLists.add(daysOffList);
        daysOffDTO.setDaysOffLists(daysOffLists);
        daysOffDTO.setReasons("full");
        daysOffDTO.setNotes("abc");
        daysOffDTO.setUserId(2);
        return daysOffDTO;
    }

    public DaysOffDTO prepareDaysOffDTOConflictDatabaseData() {
        DaysOffDTO daysOffDTO = new DaysOffDTO();
        daysOffDTO.setPoName("PO Name");
        daysOffDTO.setPoEmail("po@nal.vn");
        DaysOffList daysOffList = new DaysOffList();
        daysOffList.setVacationDay(LocalDate.parse("2020-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        daysOffList.setSessionDayOffId(1);
        daysOffList.setVacationTypeId(2);
        List<DaysOffList> daysOffLists = new ArrayList<>();
        daysOffLists.add(daysOffList);
        daysOffDTO.setDaysOffLists(daysOffLists);
        daysOffDTO.setReasons("full");
        daysOffDTO.setNotes("abc");
        daysOffDTO.setUserId(2);
        return daysOffDTO;
    }

    public DaysOffDTO prepareDaysOffDTOSuccessData() {
        DaysOffDTO daysOffDTO = new DaysOffDTO();
        daysOffDTO.setId(1);
        daysOffDTO.setPoName("PO Name");
        daysOffDTO.setPoEmail("po@nal.vn");
        DaysOffList daysOffList = new DaysOffList();
        daysOffList.setVacationDay(LocalDate.parse("07/12/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        daysOffList.setSessionDayOffId(1);
        daysOffList.setVacationTypeId(2);
        List<DaysOffList> daysOffLists = new ArrayList<>();
        daysOffLists.add(daysOffList);
        daysOffDTO.setDaysOffLists(daysOffLists);
        daysOffDTO.setReasons("full");
        daysOffDTO.setNotes("abc");
        daysOffDTO.setUserId(2);
        return daysOffDTO;
    }

    public Users prepareUsers() {
        Users user = new Users();
        user.setId(2);
        user.setRemainingDaysOff(0.5);
        user.setActive(true);
        return user;
    }

    public VacationTypes prepareVacationType() {
        VacationTypes vacationTypes = new VacationTypes();
        vacationTypes.setId(2);
        vacationTypes.setName("Unpaid leave");
        return vacationTypes;
    }
}