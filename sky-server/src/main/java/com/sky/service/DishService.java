package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {

    void addDish(DishDTO dish);

    PageResult getDishPage(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(String ids);

    DishVO selectById(Long id);

    void update(DishDTO dishDTO);
}
