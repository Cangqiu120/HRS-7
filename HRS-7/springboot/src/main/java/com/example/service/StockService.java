package com.example.service;

import com.example.DTO.PrescripetionDataDTO;
import com.example.DTO.PrescriptionDTO;
import com.example.entity.Stock;
import com.example.mapper.StockMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StockService {
    @Resource
    private StockMapper stockMapper;

    public List<Stock> selectByDocId(Integer doctorId) {
        return stockMapper.selectByDocId(doctorId);
    }

}
