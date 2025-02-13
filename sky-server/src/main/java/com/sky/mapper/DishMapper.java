package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("SELECT count(id) FROM dish WHERE category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 分页查询菜品，以及对应的分类名称
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> selectDishWithCategoryPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 查询指定id菜品的状态
     * @param id
     * @return
     */
    @Select("SELECT status FROM dish WHERE id = #{id}")
    Integer selectStatus(Long id);

    /**
     * 删除指定id菜品
     * @param id
     */
    @Delete("DELETE FROM dish WHERE id = #{id}")
    void delete(Long id);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Select("SELECT * FROM dish WHERE id = #{id}")
    Dish select(Long id);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Select("SELECT * FROM dish WHERE category_id = #{categoryId} AND status = #{status}")
    List<Dish> selectByCategoryId(Long categoryId, Integer status);

    /**
     *修改菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void update(Dish dish);

    @Select("SELECT category_id FROM dish WHERE id = #{id}")
    Long selectCategoryId(Long id);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);


}
