package com.example.mapper;

import com.example.DTO.PrescribeDetailDTO;
import com.example.DTO.PrescribeUserDTO;
import com.example.entity.Consultation;
import com.example.entity.Doctor;
import com.example.entity.Register;
import com.example.entity.RegistrationDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface DoctorMapper {

    @Select("select * from `cq-hospital`.doctor where hospital_id = #{hid} and department_id = #{depId}")
    public List<Doctor> selectDocByHidAndDepId(String hid, String depId);

    @Select("select * from `cq-hospital`.doctor where id = #{doctorId}")
    Doctor selectDoctorById(String doctorId);

    List<RegistrationDetail> selectAppointments(Integer id , String date);

    @Select("select * from `cq-hospital`.doctor where user_id = #{id}")
    Doctor selectDoctorByUserId(Integer id);

    List<Consultation> getConsultationRecords(String userName);

    @Insert("insert into `cq-hospital`.consultation (doctor_id, user_id, register_id, consultation_date, content) VALUES (#{doctorId}, #{userId}, #{registerId}, #{consultationDate}, #{content})")
    void addConsultationRecord(Consultation consultation);

    @Delete("delete from `cq-hospital`.consultation where id = #{recordId}")
    void deleteConsultationRecord(String recordId);

    @Update("update `cq-hospital`.register set status = 4 where `order` = #{registerId}")
    void cancelConsultation(Long registerId);

    @Update("update `cq-hospital`.register set status = 1 where `order` = #{registerId}")
    void completeConsultation(Long registerId);

    @Update("update `cq-hospital`.register set status = 2 where `order` = #{order}")
    void prescribeMedications(String order);

    @Update("update `cq-hospital`.register set status = 3 where `order` = #{registerId}")
    void submitOrder(Long registerId);

    List<PrescribeDetailDTO> selectPrescribe(Integer id);

    List<PrescribeUserDTO> selectRegisterByDocId(Integer id, String date);
}
