package com.sky.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 当前订单统计
 */
@Data
public class OrderStatisticsVO implements Serializable {
    private Integer toBeConfirmed;      //待接单数量

    private Integer confirmed;          //待派送数量

    private Integer deliveryInProgress; //派送中数量
}
