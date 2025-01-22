package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void insert(List<DishFlavor> dishFlavorList);

    @Delete("DELETE FROM dish_flavor WHERE dish_id = #{dishId}")
    void delete(Long dishId);
}
