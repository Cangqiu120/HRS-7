package com.example.mapper;

import com.example.DTO.SubPrescripetionDTO;
import com.example.entity.Register;
import com.example.entity.RegistrationDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface RegisterMapper {
    @Insert("insert into `cq-hospital`.register (`order`, hospital_id, department_id, doctor_id, user_id, order_time, register_time, price,status) VALUES (#{order}, #{hospitalId}, #{departmentId}, #{doctorId}, #{userId}, #{orderTime}, #{registerTime}, #{price},#{status})")
    void insert(Register register);

    @Select("select * from `cq-hospital`.register where register.order = #{order}")
    Register selectOrder(String order);

    RegistrationDetail selectOrderDetail(Long order);

    @Select("select user_id AS patient_id , hospital_id, department_id, doctor_id from `cq-hospital`.register where register.order = #{order}")
    SubPrescripetionDTO selectByOrder(String order);

}
