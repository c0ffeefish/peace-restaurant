package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitVO implements Serializable {
    private Long id;                    //订单id

    private String orderNumber;         //订单号

    private BigDecimal orderAmount;     //订单金额

    private LocalDateTime orderTime;    //下单时间
}
