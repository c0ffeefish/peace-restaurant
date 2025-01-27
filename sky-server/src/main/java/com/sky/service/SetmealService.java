package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    void addSetmeal(SetmealDTO setmealDTO);

    PageResult getSetmealPage(SetmealPageQueryDTO setmealPageQueryDTO);

    void delete(String ids);

    SetmealVO getSetmealById(Long id);

    void update(SetmealDTO setmealDTO);

    void updateStatus(Integer status, Long id);

    List<Setmeal> getSetmealByCategoryId(Long categoryId);

    List<SetmealDish> getSetmealDishBySetmealId(Long SetmealId);
}
