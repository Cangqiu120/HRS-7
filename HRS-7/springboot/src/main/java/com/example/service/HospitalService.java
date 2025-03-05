package com.example.service;

import com.example.entity.Department;
import com.example.entity.Hospital;
import com.example.mapper.HospitalMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HospitalService {
    @Resource
    private HospitalMapper hospitalMapper;

    public PageInfo<Hospital> selectPage(Hospital hospital, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Hospital> list = hospitalMapper.selectAllData(hospital);
        return PageInfo.of(list);
    }

    public Hospital getHospital(int id) {
        Hospital hospital = hospitalMapper.selectById(id);
        List<Department> Departments = hospitalMapper.selectAllDepartments(id);
        hospital.setDepartments(Departments.toArray(new Department[0]));
        return hospital;
    }
}
