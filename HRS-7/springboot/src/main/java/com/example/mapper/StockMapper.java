package com.example.mapper;

import com.example.DTO.PurchaseDTO;
import com.example.DTO.StockDTO;
import com.example.entity.Stock;
import com.example.entity.Supplier;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper

public interface StockMapper {
    List<Stock> selectByDocId(Integer doctorId);

    List<StockDTO> selectStockByType(Integer type, Integer hospitalAdminId);

    @Update("update `cq-hospital`.stock set status = 2 where id = #{id}")
    void updateStatus(Integer id);

    @Update("update `cq-hospital`.stock set status = #{status} where id = #{id}")
    void updateStatusDeactivated(Integer id, Integer status);

    List<Supplier> selectSupplier(String name);

    void purchase(PurchaseDTO purchaseDTO);

    @Select("select id from `cq-hospital`.stock where name = #{name}")
    Integer selectStockByName(String name);

    @Select("select id from `cq-hospital`.supplier where name = #{supplierName}")
    Integer selectSupplierByName(String supplierName);

    @Insert("insert into `cq-hospital`.purchase (stock_id, supplier_id) values (#{stockId}, #{supplierId})")
    void insertPurchase(Integer stockId, Integer supplierId);
}
