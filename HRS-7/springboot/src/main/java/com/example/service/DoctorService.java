package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.DoctorMapper;
import com.example.mapper.RegisterMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DoctorService {
    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RegisterMapper registerMapper;

    public List<Doctor> selectDocByHidAndDepId(String hid, String depId) {
        List<Doctor> doctors = doctorMapper.selectDocByHidAndDepId(hid, depId);
        return doctors;
    }

    public Doctor selectDoctorById(String doctorId) {
        Doctor doctor = doctorMapper.selectDoctorById(doctorId);
        return doctor;
    }

    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.DOCTOR.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public Account selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public Doctor selectDoctorByUserId(Integer id) {
        return doctorMapper.selectDoctorByUserId(id);
    }

    public PageInfo<RegistrationDetail> selectAppointmentsPage(Integer pageNum, Integer pageSize, Integer id) {
        PageHelper.startPage(pageNum, pageSize);
        Doctor doctor = selectDoctorByUserId(id);
        // 获取当前日期
        LocalDate now = LocalDate.now();
        // 定义日期格式化模式，格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 将当前日期按照指定格式进行格式化
        String date = now.format(formatter);
        System.out.println(date);
        List<RegistrationDetail> registrationDetails = doctorMapper.selectAppointments(doctor.getId(), date);
        return PageInfo.of(registrationDetails);
    }

    public List<Consultation> getConsultationRecords(String userName) {
        List<Consultation> consultationRecords = doctorMapper.getConsultationRecords(userName);
        return consultationRecords;
    }

    public Consultation addConsultationRecord(String order, String content) {
        Consultation consultation = new Consultation();
        Register register = registerMapper.selectOrder(order);
        consultation.setRegisterId(register.getId());
        consultation.setDoctorId(register.getDoctorId());
        consultation.setUserId(register.getUserId());
        // 获取当前的日期和时间
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化日期时间
        String formattedDateTime = currentDateTime.format(formatter);
        consultation.setConsultationDate(formattedDateTime);;
        consultation.setContent(content);
        doctorMapper.addConsultationRecord(consultation);
        return consultation;
    }

    public void deleteConsultationRecord(String recordId) {
        doctorMapper.deleteConsultationRecord(recordId);
    }

    public void cancelConsultation(Long registerId) {
        doctorMapper.cancelConsultation(registerId);
    }

    public void completeConsultation(Long registerId) {
        doctorMapper.completeConsultation(registerId);
    }

    public void submitOrder(Long registerId) {
        doctorMapper.submitOrder(registerId);
    }
}
