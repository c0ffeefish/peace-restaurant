package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    @Select("SELECT count(*) FROM setmeal_dish WHERE dish_id = #{dishID}")
    Integer countDish(Long dishId);

    void insert(List<SetmealDish> setmealDishes);
}
