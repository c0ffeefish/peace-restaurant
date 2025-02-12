package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private LocalDate date;     //日期

    private Double sum;         //总金额

    private Integer userSum;    //新增用户数

    private Integer orderSum;   //订单总数
}
