package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    List<ShoppingCart> list(ShoppingCart shoppingCart);

    void add(ShoppingCart shoppingCart);

    @Update("UPDATE shopping_cart SET number = #{number} WHERE id = #{id}")
    void updateOne(ShoppingCart shoppingCart);
}
