package com.example.controller;

import com.example.DTO.PrescriptionDTO;
import com.example.common.Result;
import com.example.entity.Stock;
import com.example.service.StockService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Resource
    private StockService stockService;

    @GetMapping("/selectByDocId")
    public Result selectByDocId(@RequestParam Integer doctorId) {
        List<Stock> stocks = stockService.selectByDocId(doctorId);
        return Result.success(stocks);
    }

}
