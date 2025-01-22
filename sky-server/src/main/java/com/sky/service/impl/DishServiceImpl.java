package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        if(dish.getStatus() == null){
            dish.setStatus(0);
        }

        dishMapper.insert(dish);
        Long id = dish.getId();

        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();

        if(dishFlavorList != null && dishFlavorList.size() > 0){
            dishFlavorList.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            dishFlavorMapper.insert(dishFlavorList);
        }
    }

    @Override
    public PageResult getDishPage(DishPageQueryDTO dishPageQueryDTO){
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.selectDishWithCategoryPage(dishPageQueryDTO);

        long total = page.getTotal();
        List<DishVO> dishList = page.getResult();

        return new PageResult(total, dishList);
    }
}
