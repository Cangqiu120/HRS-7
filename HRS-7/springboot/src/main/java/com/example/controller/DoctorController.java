package com.example.controller;

import com.example.common.Result;
import com.example.entity.Doctor;
import com.example.service.DoctorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/doctor")
@RestController
public class DoctorController {
    @Resource
    private DoctorService doctorService;

	@RequestMapping("/selectDoc")
    public Result selectDocByHidAndDepId(@RequestParam String hospitalId, @RequestParam String departmentId) {
        //System.out.println(hospitalId + " // " + departmentId);
        List<Doctor> doctors = doctorService.selectDocByHidAndDepId(hospitalId, departmentId);
        return Result.success(doctors);
    }
}
