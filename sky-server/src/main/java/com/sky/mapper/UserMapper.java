package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE openid = #{openid}")
    public User selectByOpenid(String openid);

    @Select("SELECT * FROM user WHERE id = #{id}")
    public User getById(Long id);

    public void insert(User user);
}
