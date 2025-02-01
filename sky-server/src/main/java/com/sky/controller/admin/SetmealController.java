package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags="套餐相关接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealMapper setmealMapper;

    final public static String cacheName = "setmeal";

    /**
     * 添加套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping("")
    @CacheEvict(cacheNames = cacheName, key = "#setmealDTO.categoryId")
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> getSetmealPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.getSetmealPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id批量删除套餐
     * @param ids
     * @return
     */
    @Delete("")
    @CacheEvict(cacheNames = cacheName, allEntries = true)
    public Result delete(String ids){
        setmealService.delete(ids);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getSetmealById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping("")
    @CacheEvict(cacheNames = cacheName, allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = cacheName, allEntries = true)
    public Result updateStatus(@PathVariable Integer status, Long id){
        setmealService.updateStatus(status, id);
        return Result.success();
    }
}
