package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.entity.Statistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("SELECT * FROM orders WHERE number = #{number}")
    Orders getByNumber(String number);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Orders selectById(Long id);

    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status}")
    Integer countByStatus(Integer status);

    void update(Orders orders);

    @Select("SELECT * FROM orders WHERE status = #{status} AND order_time < #{orderTime}")
    List<Orders> selectByStatusAndTime(Integer status, LocalDateTime orderTime);

    /**
     * 选取指定时间段营业额
     * @param begin
     * @param end
     * @return
     */
    List<Statistics> selectDateAndTurnover(LocalDate begin, LocalDate end);

    /**
     * 订单数量统计
     * @param map
     * @return
     */
    List<Statistics> selectOrdersByTime(Map map);

    /**
     * 根据动态条件统计订单数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}
