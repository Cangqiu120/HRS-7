package com.example.controller;

import com.example.common.Result;
import com.example.entity.Register;
import com.example.service.RegisterService;
import com.example.entity.SubRegistration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@RestController
@RequestMapping("/registration")
public class RegisterController {
    @Resource
    private RegisterService registerService;
    @PostMapping("/insert")
    public Result insert(@RequestBody SubRegistration registration) {
        Register register = registerService.insert(registration.getDoctorId(),registration.getUserId(),registration.getRegistrationDate(),
                registration.getRegistrationTimeSlot(),registration.getRegistrationPrice());
        return Result.success(register);
    }

    @GetMapping("/detail")
    public Result selectOrderDetail(@RequestParam Long orderId) {
        return Result.success(registerService.selectOrderDetail(orderId));
    }
}
