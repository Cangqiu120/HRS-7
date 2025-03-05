package com.example.mapper;

import com.example.entity.Department;
import com.example.entity.Hospital;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HospitalMapper {
    @Select("select * from `cq-hospital`.hospital")
    List<Hospital> selectAllData(Hospital hospital);

    @Select("select * from `cq-hospital`.hospital where id = #{id}")
    Hospital  selectById(int id);

    @Select("select * from `cq-hospital`.department where hid = #{id}")
    List<Department> selectAllDepartments(int id);
}
