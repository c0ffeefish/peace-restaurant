package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用来封装员工分页查询参数的Bean对象
 */
@Data
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    private String name;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;

}
