package com.example.controller;

import com.example.DTO.AdmissionDTO;
import com.example.DTO.ReportDTO;
import com.example.common.Result;
import com.example.entity.Report;
import com.example.service.ReportService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {
    @Resource
    private ReportService reportService;

    @PostMapping("/create")
    public Result create(@RequestBody ReportDTO reportDTO) {
        reportService.create(reportDTO);
        return Result.success();
    }

    @GetMapping("/selectReportByDoctorId")
    public Result selectReportByDoctorId(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Integer doctorUserId,
            @RequestParam String reportType){
        PageInfo<Report> reports = reportService.selectReportByDoctorId(pageNum,pageSize,doctorUserId,reportType);
        return Result.success(reports);
    }

    @PostMapping("/admission")
    public Result admission(@RequestBody AdmissionDTO admissionDTO){
        log.info("admissionDTO:{}",admissionDTO);
        reportService.admission(admissionDTO);
        return Result.success();
    }
}
