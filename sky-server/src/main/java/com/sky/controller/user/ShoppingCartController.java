package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShoppingCartController")
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车相关接口")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        if(shoppingCartDTO.getSetmealId() != null && shoppingCartDTO.getSetmealId() != null){
            return Result.error("菜品和套餐只能选择一个来添加");
        }
        if(shoppingCartDTO.getSetmealId() == null && shoppingCartDTO.getSetmealId() == null){
            return Result.error("菜品和套餐必须选择一个来添加");
        }

        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

}
