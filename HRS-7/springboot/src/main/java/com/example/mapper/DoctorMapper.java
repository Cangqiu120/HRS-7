package com.example.mapper;

import com.example.entity.Doctor;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DoctorMapper {

    @Select("select * from `cq-hospital`.doctor where hospital_id = #{hid} and department_id = #{depId}")
    public List<Doctor> selectDocByHidAndDepId(String hid, String depId);

    @Select("select * from `cq-hospital`.doctor where id = #{doctorId}")
    Doctor selectDoctorById(String doctorId);
}
