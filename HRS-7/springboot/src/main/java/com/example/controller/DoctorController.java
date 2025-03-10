package com.example.controller;

import com.example.DTO.ConsultationRecordDTO;
import com.example.common.Result;
import com.example.entity.Consultation;
import com.example.entity.Doctor;
import com.example.entity.Hospital;
import com.example.entity.RegistrationDetail;
import com.example.service.DoctorService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/doctor")
@RestController
public class DoctorController {
    @Resource
    private DoctorService doctorService;

	@GetMapping("/selectDoc")
    public Result selectDocByHidAndDepId(@RequestParam String hospitalId, @RequestParam String departmentId) {
        //System.out.println(hospitalId + " // " + departmentId);
        List<Doctor> doctors = doctorService.selectDocByHidAndDepId(hospitalId, departmentId);
        return Result.success(doctors);
    }

    @GetMapping("/selectDoctorById")
    public Result selectDoctorById(@RequestParam String doctorId) {
        Doctor doctor = doctorService.selectDoctorById(doctorId);
        return Result.success(doctor);
    }

    @GetMapping("/selectAppointments")
    public Result selectAppointments(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam Integer userId){
        PageInfo<RegistrationDetail> page = doctorService.selectAppointmentsPage(pageNum, pageSize,userId);
        return Result.success(page);
    }

    @GetMapping("/getConsultationRecords")
    public Result getConsultationRecords(@RequestParam String userName){
        List<Consultation> registrationDetails = doctorService.getConsultationRecords(userName);
        return Result.success(registrationDetails);
    }

    @PostMapping("/addConsultationRecord")
    public Result addConsultationRecord(@RequestBody ConsultationRecordDTO consultationRecordDTO){
        System.out.println("名字"+consultationRecordDTO.getUserName()+"内容"+consultationRecordDTO.getContent());
        Consultation consultation = doctorService.addConsultationRecord(consultationRecordDTO.getOrderId(),consultationRecordDTO.getContent());
        return Result.success(consultation);
    }

    @DeleteMapping("/deleteConsultationRecord/{recordId}")
    public Result deleteConsultationRecord(@PathVariable String recordId) {
        System.out.println("删除记录ID: " + recordId);
        doctorService.deleteConsultationRecord(recordId);
        return Result.success();
    }

    @PostMapping("/cancelConsultation/{registerId}")
    public Result cancelConsultation(@PathVariable Long registerId) {
        doctorService.cancelConsultation(registerId);
        return Result.success();
    }

    @PostMapping("/completeConsultation/{registerId}")
    public Result completeConsultation(@PathVariable Long registerId) {
        doctorService.completeConsultation(registerId);
        return Result.success();
    }

    @PostMapping("/submitOrder/{registerId}")
    public Result submitOrder(@PathVariable Long registerId) {
        doctorService.submitOrder(registerId);
        return Result.success();
    }
}
