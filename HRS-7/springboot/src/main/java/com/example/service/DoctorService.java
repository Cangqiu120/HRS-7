package com.example.service;

import com.example.entity.Doctor;
import com.example.mapper.DoctorMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DoctorService {
    @Resource
    private DoctorMapper doctorMapper;

    public List<Doctor> selectDocByHidAndDepId(String hid, String depId) {
        List<Doctor> doctors = doctorMapper.selectDocByHidAndDepId(hid, depId);
        return doctors;
    }
}
