package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 插入套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> selectSetmealPage(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("SELECT status FROM setmeal WHERE id = #{id}")
    Integer selectStatus(Long id);

    @Delete("DELETE FROM setmeal WHERE id = #{id}")
    void delete(Long id);

    @Select("SELECT * FROM setmeal WHERE id = #{id}")
    Setmeal select(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Update("UPDATE setmeal SET status = #{status} WHERE id = #{id}")
    void updateStatus(Integer status, Long id);
}
