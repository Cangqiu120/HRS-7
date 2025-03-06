package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubRegistration {
    private Integer doctorId;
    private Integer userId;
    private String registrationDate;
    private String registrationTimeSlot;
    private Integer registrationPrice;
}
