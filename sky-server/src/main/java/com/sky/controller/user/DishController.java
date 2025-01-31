package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    public static String DishKey = "dish_";

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        String key = DishKey + categoryId;
        //查询redis中是否存在缓存数据

        List<DishVO> list = (List<DishVO>)redisTemplate.opsForValue().get(key);
        if(list != null && list.size() > 0) {
            log.info("读取id为{}的分类缓存数据", categoryId);
            return Result.success(list);
        }

        log.info("id为{}的分类没有缓存, 查询sql表", categoryId);

        list = dishService.list(categoryId);
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }

}
