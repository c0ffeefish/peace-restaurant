package com.sky.mapper;

import com.sky.entity.Statistics;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE openid = #{openid}")
    public User selectByOpenid(String openid);

    @Select("SELECT * FROM user WHERE id = #{id}")
    public User getById(Long id);

    public void insert(User user);

    @Select("SELECT IFNULL(COUNT(*), 0) FROM user WHERE DATE(create_time) = #{date}")
    Integer selectUserByDay(LocalDate date);

    /**
     * 选择特定时间端的新用户
     * @param begin
     * @param end
     * @return
     */
    List<Statistics> selectNewUser(LocalDate begin, LocalDate end);


}
