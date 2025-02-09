package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        log.info("尝试添加菜品");
        if(shoppingCartDTO.getDishId() != null && shoppingCartDTO.getSetmealId() != null){
            return Result.error("菜品和套餐只能选择一个来添加");
        }
        if(shoppingCartDTO.getDishId() == null && shoppingCartDTO.getSetmealId() == null){
            return Result.error("菜品和套餐必须选择一个来添加");
        }

        log.info("添加菜品到购物车");

        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查询购物车")
    public Result<List<ShoppingCart>> get(){
        log.info("查询购物车");
        List<ShoppingCart> shoppingCartList = shoppingCartService.get();
        return Result.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    public Result clean(){
        shoppingCartService.clean();
        return Result.success();
    }

}
