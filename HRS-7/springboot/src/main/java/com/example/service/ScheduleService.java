package com.example.service;

import com.example.entity.Schedule;
import com.example.mapper.HospitalMapper;
import com.example.mapper.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;
    @Resource
    private HospitalMapper hospitalMapper;

    public int addSchedule(Schedule schedule) {
        Integer userId = schedule.getHospitalId();
        Integer hospitalId = hospitalMapper.getHospitalById(userId);
        schedule.setHospitalId(hospitalId);
        LocalDateTime now = LocalDateTime.now();
        schedule.setCreateTime(now);
        return scheduleMapper.insertSchedule(schedule);
    }

    public List<Schedule> getSchedulesByHospital(int userId, Date startDate, Date endDate) {
        int hospitalId = hospitalMapper.getHospitalById(userId);
        return scheduleMapper.getSchedulesByHospitalAndDateRange(hospitalId, startDate, endDate);
    }

    public List<Schedule> getSchedulesByDepartment(int userId, int departmentId, Date startDate, Date endDate) {
        int hospitalId = hospitalMapper.getHospitalById(userId);
        return scheduleMapper.getSchedulesByHospitalAndDepartment(hospitalId, departmentId, startDate, endDate);
    }

    public int deleteSchedule(int id) {
        return scheduleMapper.deleteSchedule(id);
    }

    public int updateSchedule(Schedule schedule) {
        return scheduleMapper.updateSchedule(schedule);
    }

    public List<Schedule> getSchedulesByDoctor(int doctorId, Date startDate, Date endDate) {
        return scheduleMapper.getSchedulesByDoctor(doctorId, startDate, endDate);
    }

    public Schedule getScheduleByDoctorId(int doctorId, int hospitalId, int departmentId, Date scheduleDate) {
        return scheduleMapper.getScheduleByDoctorId(doctorId, hospitalId, departmentId, scheduleDate);
    }

    public Schedule getScheduleByDoctorName(String doctorName, String hospitalName, String departmentName, Date scheduleDate) {
        return scheduleMapper.getScheduleByDoctorName(doctorName, hospitalName, departmentName, scheduleDate);
    }

    public List<Schedule> getSchedulesAndDocByDepartment(int hospitalId, int departmentId, Date startDate, Date endDate) {
        return scheduleMapper.getSchedulesByHospitalAndDepartment(hospitalId, departmentId, startDate, endDate);
    }
}