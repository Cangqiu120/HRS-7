package com.example.service;

import com.example.entity.Doctor;
import com.example.entity.Register;
import com.example.entity.RegistrationDetail;
import com.example.mapper.RegisterMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

@Service
public class RegisterService {
    @Resource
    private RegisterMapper registerMapper;
    @Resource
    private DoctorService doctorService;
    @Resource
    private UserService userService;
    @Resource
    private HospitalService hospitalService;

    public Register insert(Integer doctorId, Integer userId, String date, String time, Integer price) {
        Doctor doctor = doctorService.selectDoctorById(String.valueOf(doctorId));
        Register register = new Register();
        String order = generateOrderNumber();
        register.setOrder(order);
        register.setDoctorId(doctorId);
        register.setDepartmentId(doctor.getDepartmentId());
        register.setHospitalId(doctor.getHospitalId());
        register.setUserId(userId);
        // 获取当前的日期和时间
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化日期时间
        String formattedDateTime = currentDateTime.format(formatter);
        register.setOrderTime(formattedDateTime);
        String RegisterTime = date + " " + time;
        register.setRegisterTime(RegisterTime);
        register.setPrice(price);
        register.setStatus(0);
        registerMapper.insert(register);
        return register;
    }

    public static String generateOrderNumber() {
        // 获取当前日期
        LocalDate now = LocalDate.now();
        // 定义日期格式化模式，格式化为 yyMMdd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        // 将当前日期按照指定格式进行格式化
        String datePart = now.format(formatter);

        // 创建 Random 对象用于生成随机数
        Random random = new Random();
        // 生成 4 位随机数，范围是 0 - 9999
        int randomNumber = random.nextInt(10000);
        // 将随机数格式化为 4 位字符串，不足 4 位时前面补 0
        String randomPart = String.format("%04d", randomNumber);

        // 拼接日期部分和随机数部分，组成 12 位订单号
        return datePart + randomPart;
    }

    public RegistrationDetail selectOrderDetail(Long order) {
        RegistrationDetail registrationDetail = new RegistrationDetail();
        registrationDetail = registerMapper.selectOrderDetail(order);
        return registrationDetail;
    }

}
