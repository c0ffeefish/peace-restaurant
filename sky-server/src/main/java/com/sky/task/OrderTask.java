package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void processOrderTimeOut(){
        log.info("处理超时订单, {}", LocalDateTime.now());

        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.selectByStatusAndTime(Orders.PENDING_PAYMENT, orderTime);

        if(ordersList != null && ordersList.size() > 0){
            for(Orders orders : ordersList){
                Orders order = new Orders();
                order.setId(orders.getId());
                order.setCancelReason("订单超时");
                order.setCancelTime(LocalDateTime.now());
                order.setStatus(Orders.CANCELLED);

                orderMapper.update(order);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processOrderInDelivery(){
        log.info("每天处理派送中的订单, {}", LocalDateTime.now());

        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.selectByStatusAndTime(Orders.DELIVERY_IN_PROGRESS, orderTime);

        if(ordersList != null && ordersList.size() > 0){
            for(Orders orders : ordersList){
                Orders order = new Orders();
                order.setId(orders.getId());
                order.setStatus(Orders.COMPLETED);

                orderMapper.update(order);
            }
        }
    }

}
