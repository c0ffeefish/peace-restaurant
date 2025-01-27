package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealMapper.insert(setmeal);
        Long id = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(id);
            });
            setmealDishMapper.insert(setmealDishes);
        }
    }

    public PageResult getSetmealPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.selectSetmealPage(setmealPageQueryDTO);

        Long total = page.getTotal();
        List<SetmealVO> records = page.getResult();

        return new PageResult(total, records);
    }

    @Override
    public void delete(String ids) {
        List<Long> idList = Arrays.stream(ids.split(",")).
                                    map(String::trim).map(Long::valueOf).collect(Collectors.toList());

        for(Long id : idList){
            if(setmealMapper.selectStatus(id) == 0){
                setmealDishMapper.deleteBySetmealId(id);
                setmealMapper.delete(id);
            }
        }

    }

    @Override
    public SetmealVO getSetmealById(Long id) {
        Setmeal setmeal = setmealMapper.select(id);
        SetmealVO setmealVO = new SetmealVO();

        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishMapper.selectBySetmealId(setmealVO.getId()));

        return setmealVO;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealDishMapper.deleteBySetmealId(setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDTO.getId());
            });
            setmealDishMapper.insert(setmealDishes);
        }

        setmealMapper.update(setmeal);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        setmealMapper.updateStatus(status, id);
    }

    @Override
    public List<Setmeal> getSetmealByCategoryId(Long categoryId) {
        List<Setmeal> setmealList = setmealMapper.selectByCategoryId(categoryId);
        return setmealList;
    }

    @Override
    public List<SetmealDish> getSetmealDishBySetmealId(Long SetmealId) {
        List<SetmealDish> setmealDishList = setmealDishMapper.selectBySetmealId(SetmealId);
        return setmealDishList;
    }
}
