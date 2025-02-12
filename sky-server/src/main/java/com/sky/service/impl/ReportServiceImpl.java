package com.sky.service.impl;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.SalesTop10Report;
import com.sky.entity.Statistics;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<Statistics> turnovers = orderMapper.selectDateAndTurnover(begin, end);

        List<LocalDate> dateList = getDateList(begin, end);
        List<Double> turnoverList = new ArrayList<>();

        /*for (Turnover turnover : turnovers) {
            log.info("date: {}, sum: {}", turnover.getDate(), turnover.getSum());
        }*/

        int idx = 0;
        int len = turnovers.size();

        while(!begin.equals(end.plusDays(1))){
            if(idx < len && begin.equals(turnovers.get(idx).getDate())){
                turnoverList.add(turnovers.get(idx).getSum());
                idx++;
            }else{
                turnoverList.add(0.0);
            }

            begin = begin.plusDays(1);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<Statistics> Users = userMapper.selectNewUser(begin, end);
        Integer sumUser = userMapper.selectUserByDay(begin.plusDays(-1));
        List<Integer> allUsers = new ArrayList<>();
        List<Integer> newUsers = new ArrayList<>();
        List<LocalDate> dateList = getDateList(begin, end);

        log.info("用户数量{}", sumUser);

        int idx = 0;

        while(!begin.equals(end.plusDays(1))){
            if(idx < Users.size() && begin.equals(Users.get(idx).getDate())){
                sumUser += Users.get(idx).getUserSum();
                newUsers.add(Users.get(idx).getUserSum());
                idx++;
            }else{
                newUsers.add(0);
            }

            allUsers.add(sumUser);
            begin = begin.plusDays(1);
        }
        log.info("{}", allUsers);

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUsers, ","))
                .totalUserList(StringUtils.join(allUsers, ","))
                .build();
    }

    @Override
    public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        List<Statistics> orders = orderMapper.selectOrdersByTime(map);

        map.put("status", Orders.COMPLETED);
        List<Statistics> validOrders = orderMapper.selectOrdersByTime(map);

        List<LocalDate> dateList = getDateList(begin, end);
        Integer orderSum = 0;
        Integer validOrderSum = 0;
        List<Integer> orderList = new ArrayList<>();
        List<Integer> validOrderList = new ArrayList<>();
        int idx1 = 0;
        int idx2 = 0;

        while(!begin.equals(end.plusDays(1))){
            if(idx1 < orders.size() && begin.equals(orders.get(idx1).getDate())){
                orderList.add(orders.get(idx1).getOrderSum());
                orderSum += orders.get(idx1).getOrderSum();
                idx1++;
            }else{
                orderList.add(0);
            }

            if(idx2 < validOrders.size() && begin.equals(validOrders.get(idx2).getDate())){
                validOrderList.add(validOrders.get(idx2).getOrderSum());
                validOrderSum += validOrders.get(idx2).getOrderSum();
                idx2++;
            }else{
                validOrderList.add(0);
            }

            begin = begin.plusDays(1);
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderList, ","))
                .totalOrderCount(orderSum)
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .validOrderCount(validOrderSum)
                .orderCompletionRate(1.0 * validOrderSum / orderSum)
                .build();
    }

    @Override
    public SalesTop10ReportVO top10Statistics(LocalDate begin, LocalDate end) {
        List<SalesTop10Report> salesTop10ReportList = orderDetailMapper.selectTop10(begin, end);

        String nameList = StringUtils.join(salesTop10ReportList.stream()
                .map(SalesTop10Report::getName)
                .collect(Collectors.toList()), ",");

        String numberList = StringUtils.join(salesTop10ReportList.stream()
                .map(SalesTop10Report::getNumbers)
                .collect(Collectors.toList()), ",");

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    @CachePut(cacheNames = "dateCache", key = "'#{begin}'+'#{end}'")
    public List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while(!begin.equals(end.plusDays(1))){
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        return dateList;
    }
}
