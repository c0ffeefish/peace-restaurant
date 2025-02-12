package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.SalesTop10Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertCarts(List<OrderDetail> orderDetailList);

    @Select("SELECT * FROM order_detail WHERE order_id = #{orderId}")
    List<OrderDetail> selectByOrderId(Long orderId);

    List<SalesTop10Report> selectTop10(LocalDate begin, LocalDate end);
}
