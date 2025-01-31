package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public static String DishKEY = "dish_";

    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(0);

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

    @Override
    public void deleteDish(String ids){
        List<Long> idList = Arrays.stream(ids.split(",")).
                                        map(String::trim).
                                        map(Long::valueOf).
                                        collect(Collectors.toList());

        for(Long id : idList){
            if(setmealDishMapper.countDish(id) == 0 && dishMapper.selectStatus(id) == 0){
                dishFlavorMapper.deleteByDishId(id);
                dishMapper.delete(id);
            }
        }
    }

    @Override
    public DishVO selectById(Long id){
        Dish dish = dishMapper.select(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavorMapper.selectByDishId(id));

        return dishVO;
    }

    @Override
    @Transactional
    public void update(DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //清除对应套餐缓存
        String key = DishKEY + dish.getCategoryId();
        redisTemplate.delete(key);

        dishFlavorMapper.deleteByDishId(dish.getId());
        dishMapper.update(dish);
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        Long id = dish.getId();

        if(dishFlavorList != null && dishFlavorList.size() > 0){
            dishFlavorList.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            dishFlavorMapper.insert(dishFlavorList);
        }
    }

    @Override
    public List<DishVO> list(Long categoryId){
        List<Dish> dishList = dishMapper.selectByCategoryId(categoryId, StatusConstant.ENABLE);
        List<DishVO> dishVOList = new ArrayList<>();

        for(Dish dish : dishList){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);
            dishVO.setFlavors(dishFlavorMapper.selectByDishId(dishVO.getId()));
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        redisTemplate.delete(dishMapper.selectCategoryId(id));
        dishMapper.update(dish);
    }
}
