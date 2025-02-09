package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        /*ShoppingCart shoppingCart = new ShoppingCart();

        //判断数据库是否已经存在该数据
        if ((shoppingCartDTO.getDishId() != null && shoppingCartMapper.getByDishId(shoppingCartDTO) != null)
            || (shoppingCartDTO.getSetmealId() != null &&shoppingCartMapper.getBySetmealId(shoppingCartDTO) != null))
        {
            shoppingCartMapper.updateOne(shoppingCart);
            return;
        }*/

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> cartList = shoppingCartMapper.list(shoppingCart);

        if(cartList != null && cartList.size() > 0) {
            ShoppingCart shoppingCart1 = cartList.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            log.info("数量+1");
            shoppingCartMapper.updateOne(shoppingCart1);
        }else{
            if (shoppingCartDTO.getDishId() != null) {
                Dish dish = dishMapper.select(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else if(shoppingCartDTO.getSetmealId() != null){
                Setmeal setmeal = setmealMapper.select(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            log.info("插入新菜品");
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> get() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        log.info("查询购物车");
        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCartMapper.clean(userId);
    }

}
