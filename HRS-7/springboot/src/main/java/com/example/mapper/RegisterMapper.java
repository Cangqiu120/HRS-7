package com.example.mapper;

import com.example.entity.Register;
import com.example.entity.RegistrationDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface RegisterMapper {
    @Insert("insert into `cq-hospital`.register (`order`, hospital_id, department_id, doctor_id, user_id, order_time, register_time, price) VALUES (#{order}, #{hospitalId}, #{departmentId}, #{doctorId}, #{userId}, #{orderTime}, #{registerTime}, #{price})")
    void insert(Register register);

    @Select("select * from `cq-hospital`.register where register.order = #{order}")
    Register selectOrder(Integer order);

    RegistrationDetail selectOrderDetail(Long order);
}
