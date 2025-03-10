package com.example.mapper;

import com.example.entity.Stock;

import java.util.List;

public interface StockMapper {
    List<Stock> selectByDocId(Integer doctorId);
}
