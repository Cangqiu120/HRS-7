package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 请假申请表对应的实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApplication {
    private int id;          // 请假申请id
    private int doctorId;         // 医生id
    private int departmentId;     // 科室id
    private int hospitalId;       // 医院id
    private String leaveType;     // 请假类型
    private String startDate;     // 请假开始日期
    private String endDate;       // 请假结束日期
    private String applicationDate; // 请假提交日期
    private int approvalStatus;   // 请假审批状态

}